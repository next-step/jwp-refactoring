package kitchenpos.tablegroup.application;

import kitchenpos.order.application.OrderService;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.Empty;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.OrderTableIdRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

import static kitchenpos.common.Messages.ORDER_TABLE_STATUS_CANNOT_UPDATE;
import static kitchenpos.table.fixture.TableFixture.*;
import static kitchenpos.tablegroup.fixture.TableGroupFixture.단체_주문_테이블_그룹;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private TableService tableService;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @Mock
    private OrderService orderService;

    @Test
    @DisplayName("테이블 그룹 등록시 정상 테이블 정보 일때 테이블 그룹이 등록 된다")
    void create() {
        // given
        OrderTable 단체_주문_테이블_2명 = OrderTable.of(1L, null, NumberOfGuests.of(2), Empty.of(true));
        OrderTable 단체_주문_테이블_3명 = OrderTable.of(2L, null, NumberOfGuests.of(3), Empty.of(true));

        TableGroup 단체_주문_테이블 = TableGroup.of(2L, LocalDateTime.now(), OrderTables.of(Arrays.asList(단체_주문_테이블_2명, 단체_주문_테이블_3명)));

        TableGroupRequest 단체_주문_테이블_요청 =
                TableGroupRequest.of(Arrays.asList(
                        OrderTableIdRequest.of(단체_주문_테이블_2명.getId()),
                        OrderTableIdRequest.of(단체_주문_테이블_3명.getId()))
                );

        // when
        when(tableService.findByIdIn(any())).thenReturn(Arrays.asList(단체_주문_테이블_2명, 단체_주문_테이블_3명));
        when(tableGroupRepository.save(any())).thenReturn(단체_주문_테이블);

        TableGroupResponse 테이블_그룹_생성_결과 = tableGroupService.create(단체_주문_테이블_요청);

        // then
        Assertions.assertThat(테이블_그룹_생성_결과).isEqualTo(TableGroupResponse.of(단체_주문_테이블));
    }

    @Test
    @DisplayName("테이블 그룹 등록시 메뉴가 1개일 경우 테이블 그룹 등록이 실패 된다")
    void create2() {
        // given
        TableGroupRequest 테이블_개수_1개 = TableGroupRequest.of(Arrays.asList(OrderTableIdRequest.of(단체_주문_테이블_4명.getId())));

        // when then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableGroupService.create(테이블_개수_1개));
    }

    @Test
    @DisplayName("테이블 그룹 등록시 조회된 테이블 개수가 다른 경우 테이블 등록 실패 된다")
    void create3() {
        // given
        TableGroupRequest 메뉴_개수_2개 = TableGroupRequest.of(
                Arrays.asList(
                        OrderTableIdRequest.of(단체_주문_테이블_4명.getId()),
                        OrderTableIdRequest.of(단체_주문_테이블_6명.getId()))
        );

        // when
        when(tableService.findByIdIn(anyList())).thenReturn(Collections.singletonList(단체_주문_테이블_6명));

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableGroupService.create(메뉴_개수_2개));
    }

    @Test
    @DisplayName("테이블 그룹 등록시 테이블 그룹이 이미 등록되어 있는 경우 실패 된다.")
    void create4() {
        // given
        TableGroupRequest 테이블_그룹이_비어있지_않음 = TableGroupRequest.of(
                Arrays.asList(
                        OrderTableIdRequest.of(단체_주문_테이블_4명.getId()),
                        OrderTableIdRequest.of(주문_테이블_여섯이_있음.getId()))
        );

        // when
        when(tableService.findByIdIn(any())).thenReturn(Arrays.asList(단체_주문_테이블_4명, 주문_테이블_여섯이_있음));

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableGroupService.create(테이블_그룹이_비어있지_않음));
    }

    @Test
    @DisplayName("테이블 그룹 해체시 테이블 그룹이 해제 가능한 상태라면 해제 한다")
    void ungroup() {
        OrderTable 단체_주문_테이블_10명 = OrderTable.of(1L, null, NumberOfGuests.of(4), Empty.of(true));
        OrderTable 단체_주문_테이블_20명 = OrderTable.of(2L, null, NumberOfGuests.of(4), Empty.of(true));

        TableGroup 요청_단체_테이블_그룹 = TableGroup.of(
                999L,
                LocalDateTime.now(),
                OrderTables.of(Arrays.asList(단체_주문_테이블_10명, 단체_주문_테이블_20명))
        );

        // when
        when(tableGroupRepository.findById(any())).thenReturn(Optional.of(요청_단체_테이블_그룹));
        when(orderService.isOrderTablesStateInCookingOrMeal(any())).thenReturn(false);

        tableGroupService.ungroup(요청_단체_테이블_그룹.getId());

        // then
        assertAll(
                () -> Assertions.assertThat(단체_주문_테이블_10명.getTableGroupId()).isNull(),
                () -> Assertions.assertThat(단체_주문_테이블_20명.getTableGroupId()).isNull()
        );
    }

    @Test
    @DisplayName("테이블 그룹 해제시 테이블 그룹 정보가 조회되지 않는다면 해제가 실패한다.")
    void ungroup2() {
        // when
        when(tableGroupRepository.findById(any())).thenReturn(Optional.empty());

        // then
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> tableGroupService.ungroup(999L));
    }

    @Test
    @DisplayName("테이블 그룹 해제시 테이블내 주문 상태가 식사, 요리 상태라면 테이블 그룹 해제가 실패한다")
    void ungroup3() {

        // when
        when(tableGroupRepository.findById(any())).thenReturn(Optional.of(단체_주문_테이블_그룹));
        when(orderService.isOrderTablesStateInCookingOrMeal(any())).thenReturn(true);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableGroupService.ungroup(단체_주문_테이블_그룹.getId()))
                .withMessage(ORDER_TABLE_STATUS_CANNOT_UPDATE);
    }
}
