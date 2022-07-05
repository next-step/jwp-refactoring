package kitchenpos.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableIdsRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.order.exception.OrderStatusException;
import kitchenpos.table.exception.OrderTableException;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import kitchenpos.table.application.TableGroupService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static kitchenpos.factory.TableGroupFixture.테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @InjectMocks
    TableGroupService tableGroupService;

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderTableRepository orderTableRepository;

    @Mock
    TableGroupRepository tableGroupRepository;

    TableGroup 단체;

    OrderTable 주문테이블1;
    OrderTable 주문테이블2;

    @Test
    @DisplayName("단체를 생성 및 지정한다.(Happy Path)")
    void create() {
        //given
        주문테이블1 = 테이블_생성(1L);
        주문테이블2 = 테이블_생성(2L);
        단체 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(주문테이블1, 주문테이블2));
        TableGroupRequest 단체Request = new TableGroupRequest(Arrays.asList(new OrderTableIdsRequest(주문테이블1.getId()),
                new OrderTableIdsRequest(주문테이블2.getId())));
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(Arrays.asList(주문테이블1, 주문테이블2));
        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(단체);

        //when
        TableGroupResponse savedTableGroup = tableGroupService.create(단체Request);

        //then
        assertThat(savedTableGroup).isNotNull()
                .satisfies(tableGroup -> {
                            tableGroup.getId().equals(단체.getId());
                            tableGroup.getOrderTableResponses()
                                        .stream()
                                        .map(OrderTableResponse::getId)
                                        .collect(Collectors.toList())
                                        .containsAll(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()));
                        }
                );
    }

    @Test
    @DisplayName("테이블 그룹에 2개 이상의 테이블이 없는 경우 테이블 그룹 생성 불가")
    void createInvalidOrderTables() {
        //given
        주문테이블1 = 테이블_생성(1L);
        TableGroupRequest 단체Request = new TableGroupRequest(Arrays.asList(new OrderTableIdsRequest(주문테이블1.getId())));
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(Arrays.asList(주문테이블1));

        //then
        assertThatThrownBy(() -> {
            tableGroupService.create(단체Request);
        }).isInstanceOf(OrderTableException.class)
        .hasMessageContaining(OrderTableException.ORDER_TABLE_SIZE_OVER_TWO_MSG);
    }

    @Test
    @DisplayName("테이블 그룹에 유효하지 않은 테이블이 존재할 경우 그룹 생성 불가")
    void createInvalidOrderTable() {
        //given
        주문테이블1 = 테이블_생성(1L);
        주문테이블2 = 테이블_생성(2L);
        OrderTable 유효하지않은테이블 = new OrderTable(3L, null, 3, false);
        단체 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(주문테이블1, 주문테이블2));
        TableGroupRequest 단체Request = new TableGroupRequest(Arrays.asList(new OrderTableIdsRequest(주문테이블1.getId()),
                new OrderTableIdsRequest(주문테이블2.getId()), new OrderTableIdsRequest(3L)));
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(Arrays.asList(주문테이블1, 주문테이블2));

        //then
        assertThatThrownBy(() -> {
            tableGroupService.create(단체Request);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹내 테이블이 다른 테이블 그룹에 포함되어 있는 경우 그룹 생성 불가")
    void createInvalidAssignedTableGroup() {
        //given
        TableGroup 다른단체 = new TableGroup(5L, LocalDateTime.now());
        주문테이블1 = 테이블_생성(1L);
        주문테이블2 = 테이블_생성(2L, 다른단체, 2, false);
        TableGroupRequest 단체Request = new TableGroupRequest(Arrays.asList(new OrderTableIdsRequest(주문테이블1.getId()),
                new OrderTableIdsRequest(주문테이블2.getId())));
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(Arrays.asList(주문테이블1, 주문테이블2));

        //then
        assertThatThrownBy(() -> {
            tableGroupService.create(단체Request);
        }).isInstanceOf(OrderTableException.class)
        .hasMessageContaining(OrderTableException.ORDER_TALBE_ALREADY_HAS_GROUP_MSG);
    }

    @Test
    @DisplayName("테이블 그룹내 테이블이 중복되어 있을 경우 그룹 생성 불가")
    void createDuplicateTable() {
        //given
        주문테이블1 = 테이블_생성(1L);
        TableGroupRequest 단체Request = new TableGroupRequest(Arrays.asList(new OrderTableIdsRequest(주문테이블1.getId()),
                new OrderTableIdsRequest(주문테이블1.getId())));
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(Arrays.asList(주문테이블1));

        //then
        assertThatThrownBy(() -> {
            tableGroupService.create(단체Request);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문테이블 그룹에 포함된 주물테이블들 그룹 해제(Happy Path)")
    void ungroup() {
        //given
        주문테이블1 = 테이블_생성(1L, 단체, 2, false);
        주문테이블2 = 테이블_생성(2L, 단체, 3, false);
        단체 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(주문테이블1, 주문테이블2));
        given(tableGroupRepository.findById(anyLong())).willReturn(Optional.of(단체));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(false);

        //when
        tableGroupService.ungroup(단체.getId());

        //then
        assertThat(주문테이블1.getTableGroup()).isNull();
        assertThat(주문테이블2.getTableGroup()).isNull();
    }

    @Test
    @DisplayName("주문테이블 그룹에 포함된 주물테이블들이 조리중/식사중일 경우 그룹 해제 불가")
    void ungroupInvalidOrderStatus() {
        //given
        //given
        주문테이블1 = 테이블_생성(1L, 단체, 2, false);
        주문테이블2 = 테이블_생성(2L, 단체, 3, false);
        단체 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(주문테이블1, 주문테이블2));
        given(tableGroupRepository.findById(anyLong())).willReturn(Optional.of(단체));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        //then
        assertThatThrownBy(() -> {
            tableGroupService.ungroup(단체.getId());
        }).isInstanceOf(OrderStatusException.class)
            .hasMessageContaining(OrderStatusException.ORDER_STATUS_CAN_NOT_UNGROUP_MSG);
    }
}