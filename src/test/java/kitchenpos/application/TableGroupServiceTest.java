package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.fixture.OrderTableFixtureFactory;
import kitchenpos.application.fixture.TableGroupFixtureFactory;
import kitchenpos.application.tablegroup.TableGroupService;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.domain.tablegroup.TableGroupRepository;
import kitchenpos.dto.tablegroup.OrderTableIdRequest;
import kitchenpos.dto.tablegroup.TableGroupRequest;
import kitchenpos.dto.tablegroup.TableGroupResponse;
import kitchenpos.exception.NotCompletionOrderException;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    private TableGroup 단체_테이블그룹;
    private OrderTable 주문1_단체테이블;
    private OrderTable 주문2_단체테이블;
    private OrderTable 손님_10명_개인테이블;
    private OrderTable 개인1_단체테이블;
    private OrderTable 개인2_단체테이블;

    @BeforeEach
    void setUp() {
        주문1_단체테이블 = OrderTableFixtureFactory.create(1L, true);
        주문2_단체테이블 = OrderTableFixtureFactory.create(2L, true);
        손님_10명_개인테이블 = OrderTableFixtureFactory.createWithGuests(3L, 10, true);
        개인1_단체테이블 = OrderTableFixtureFactory.create(4L, true);
        개인2_단체테이블 = OrderTableFixtureFactory.create(5L, true);
        단체_테이블그룹 = TableGroupFixtureFactory.create(1L);

        단체_테이블그룹.addOrderTables(Arrays.asList(주문1_단체테이블, 주문2_단체테이블));
    }

    @DisplayName("TableGroup 을 등록한다.")
    @Test
    void create1() {
        // given
        List<OrderTableIdRequest> orderTableIdRequests =
            Arrays.asList(OrderTableIdRequest.from(개인1_단체테이블.getId()), OrderTableIdRequest.from(개인2_단체테이블.getId()));
        TableGroupRequest tableGroupRequest = TableGroupRequest.from(orderTableIdRequests);

        given(orderTableRepository.findAllByIdIn(Arrays.asList(개인1_단체테이블.getId(), 개인2_단체테이블.getId())))
            .willReturn(Arrays.asList(개인1_단체테이블, 개인2_단체테이블));
        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(단체_테이블그룹);

        // when
        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);

        // then
        assertThat(tableGroupResponse).isEqualTo(TableGroupResponse.from(단체_테이블그룹));
    }

    @DisplayName("TableGroup 을 등록 시, OrderTable 이 0개면 예외가 발생한다.")
    @Test
    void create2() {
        // given
        TableGroupRequest tableGroupRequest = TableGroupRequest.from(Collections.emptyList());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroupRequest));
    }

    @DisplayName("TableGroup 을 등록 시, OrderTable 이 1개면 예외가 발생한다.")
    @Test
    void create3() {
        // given
        List<OrderTableIdRequest> orderTableIdRequests = Arrays.asList(OrderTableIdRequest.from(주문1_단체테이블.getId()));
        TableGroupRequest tableGroupRequest = TableGroupRequest.from(orderTableIdRequests);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroupRequest));
    }

    @DisplayName("TableGroup 을 등록 시, OrderTable 이 존재하지 않으면 예외가 발생한다.")
    @Test
    void create4() {
        // given
        List<OrderTableIdRequest> orderTableIdRequests =
            Arrays.asList(OrderTableIdRequest.from(주문1_단체테이블.getId()), OrderTableIdRequest.from(주문2_단체테이블.getId()));
        TableGroupRequest tableGroupRequest = TableGroupRequest.from(orderTableIdRequests);

        given(orderTableRepository.findAllByIdIn(Arrays.asList(주문1_단체테이블.getId(), 주문2_단체테이블.getId())))
            .willReturn(Collections.emptyList());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroupRequest));
    }

    @DisplayName("TableGroup 을 등록 시, OrderTable 이 빈 테이블이 아니면 예외가 발생한다.")
    @Test
    void create5() {
        // given
        List<OrderTableIdRequest> orderTableIdRequests =
            Arrays.asList(OrderTableIdRequest.from(손님_10명_개인테이블.getId()), OrderTableIdRequest.from(주문2_단체테이블.getId()));
        TableGroupRequest tableGroupRequest = TableGroupRequest.from(orderTableIdRequests);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroupRequest));
    }

    @DisplayName("TableGroup 을 등록 시, OrderTable 이 이미 그룹에 속해있으면 예외가 발생한다.")
    @Test
    void create6() {
        // given
        List<OrderTableIdRequest> orderTableIdRequests =
            Arrays.asList(OrderTableIdRequest.from(주문1_단체테이블.getId()), OrderTableIdRequest.from(주문2_단체테이블.getId()));
        TableGroupRequest tableGroupRequest = TableGroupRequest.from(orderTableIdRequests);
        주문1_단체테이블.alignTableGroup(단체_테이블그룹);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroupRequest));
    }

    @DisplayName("TableGroup 을 해제한다.")
    @Test
    void ungroup1() {
        // given
        주문1_단체테이블.alignTableGroup(단체_테이블그룹);
        주문2_단체테이블.alignTableGroup(단체_테이블그룹);

        given(tableGroupRepository.findById(anyLong())).willReturn(Optional.ofNullable(단체_테이블그룹));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(false);

        // when
        tableGroupService.ungroup(단체_테이블그룹.getId());

        // then
        assertThat(주문1_단체테이블.getTableGroup()).isNull();
        assertThat(주문2_단체테이블.getTableGroup()).isNull();
    }

    @DisplayName("TableGroup 해제 시, 주문상태가 요리중(COOKING)이거나 식사중(MEAL) 이면 예외가 발생한다.")
    @Test
    void ungroup2() {
        // given
        주문1_단체테이블.alignTableGroup(단체_테이블그룹);
        주문2_단체테이블.alignTableGroup(단체_테이블그룹);

        given(tableGroupRepository.findById(anyLong())).willReturn(Optional.ofNullable(단체_테이블그룹));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        // when & then
        assertThrows(NotCompletionOrderException.class, () -> tableGroupService.ungroup(단체_테이블그룹.getId()));
    }
}