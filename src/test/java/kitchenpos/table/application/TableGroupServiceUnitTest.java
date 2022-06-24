package kitchenpos.table.application;

import static kitchenpos.helper.OrderFixtures.주문_만들기;
import static kitchenpos.helper.TableFixtures.테이블_만들기;
import static kitchenpos.helper.TableFixtures.테이블_요청_만들기;
import static kitchenpos.helper.TableGroupFixtures.테이블_그룹_만들기;
import static kitchenpos.helper.TableGroupFixtures.테이블_그룹_요청_만들기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.order.consts.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("단체 지정 Service 단위 테스트 - Stub")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceUnitTest {

    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;
    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderTableRepository, tableGroupRepository);
    }

    @DisplayName("단체 지정을 등록 한다.")
    @Test
    void create() {
        //given
        long generateTableGroupId = 1;
        TableGroupRequest request = 테이블_그룹_요청_만들기(Arrays.asList(테이블_요청_만들기(1L), 테이블_요청_만들기(2L)));
        kitchenpos.table.domain.OrderTable emptyTable1 = 테이블_만들기(1L, 0, true);
        kitchenpos.table.domain.OrderTable emptyTable2 = 테이블_만들기(2L, 0, true);

        given(orderTableRepository.findAllById(request.getRequestOrderTableIds()))
                .willReturn(Arrays.asList(emptyTable1, emptyTable2));

        doAnswer(invocation -> 테이블_그룹_만들기(generateTableGroupId, Arrays.asList(emptyTable1, emptyTable2)))
                .when(tableGroupRepository).save(any());

        //when
        TableGroupResponse result = tableGroupService.create(request);

        //then
        List<OrderTableResponse> orderTables = result.getOrderTables();
        assertThat(orderTables.get(0).getEmpty()).isFalse();
        assertThat(orderTables.get(1).getEmpty()).isFalse();
    }

    @DisplayName("테이블이 2개 미만이면 단체 지정을 등록 할 수 없다.")
    @Test
    void create_less_then_two() {
        //given
        TableGroupRequest request = 테이블_그룹_요청_만들기(Arrays.asList(테이블_요청_만들기(1L)));
        OrderTable emptyTable1 = 테이블_만들기(1L, 0, true);
        given(orderTableRepository.findAllById(Arrays.asList(1L))).willReturn(Arrays.asList(emptyTable1));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(request));
    }

    @DisplayName("등록 되어 있지 않은 테이블이 있는 경우 단체 지정을 등록 할 수 없다.")
    @Test
    void create_not_registered_table() {
        //given
        TableGroupRequest request = 테이블_그룹_요청_만들기(Arrays.asList(테이블_요청_만들기(1L), 테이블_요청_만들기(2L), 테이블_요청_만들기(3L)));
        OrderTable emptyTable1 = 테이블_만들기(1L, 0, true);
        OrderTable emptyTable2 = 테이블_만들기(2L, 0, true);
        given(orderTableRepository.findAllById(request.getRequestOrderTableIds()))
                .willReturn(Arrays.asList(emptyTable1, emptyTable2));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(request));
    }

    @DisplayName("주문 테이블이 있는 경우 단체 지정을 등록 할 수 없다.")
    @Test
    void create_in_order_table() {
        //given
        TableGroupRequest request = 테이블_그룹_요청_만들기(Arrays.asList(테이블_요청_만들기(1L), 테이블_요청_만들기(2L)));
        OrderTable emptyTable1 = 테이블_만들기(1L, 0, true);
        OrderTable orderTable2 = 테이블_만들기(2L, 3, false);
        given(orderTableRepository.findAllById(request.getRequestOrderTableIds()))
                .willReturn(Arrays.asList(emptyTable1, orderTable2));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(request));
    }

    @DisplayName("이미 단체 지정된 테이블이 있는 경우 단체 지정 등록 할 수 없다.")
    @Test
    void create_already() {
        //given
        OrderTable emptyTable1 = 테이블_만들기(1L, 0, true);
        OrderTable emptyTable2 = 테이블_만들기(2L, 0, true);
        emptyTable2.setTableGroup(테이블_그룹_만들기(1L, Arrays.asList(emptyTable1, emptyTable2)));

        TableGroupRequest request = 테이블_그룹_요청_만들기(Arrays.asList(테이블_요청_만들기(2L), 테이블_요청_만들기(3L)));

        given(orderTableRepository.findAllById(request.getRequestOrderTableIds()))
                .willReturn(Arrays.asList(emptyTable1, emptyTable2));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(request));
    }

    @DisplayName("단체 지정을 해제 한다.")
    @Test
    void ungroup() {
        //given
        long requestTableGroupId = 1L;
        OrderTable emptyTable1 = 테이블_만들기(0, true);
        OrderTable orderTable2 = 테이블_만들기(0, true);
        TableGroup tableGroup = 테이블_그룹_만들기(requestTableGroupId);
        tableGroup.groupingTables(new OrderTables(Arrays.asList(emptyTable1, orderTable2)), 2);
        orderTable2.addOrder(주문_만들기(OrderStatus.COMPLETION));
        emptyTable1.addOrder(주문_만들기(null));

        given(tableGroupRepository.findById(requestTableGroupId)).willReturn(Optional.of(tableGroup));

        //when
        tableGroupService.ungroup(requestTableGroupId);

        //then
        assertThat(emptyTable1.getTableGroup()).isNull();
        assertThat(orderTable2.getTableGroup()).isNull();
    }

    @DisplayName("주문 상태가 조리, 식사인 경우가 있으면 단체 지정 해제 할 수 없다.")
    @Test
    void ungroup_order_status_cooking_meal() {
        //given
        long requestTableGroupId = 1L;
        OrderTable emptyTable1 = 테이블_만들기(0, true);
        OrderTable emptyTable2 = 테이블_만들기(0, true);
        TableGroup tableGroup1 = 테이블_그룹_만들기(requestTableGroupId);
        tableGroup1.groupingTables(new OrderTables(Arrays.asList(emptyTable1, emptyTable2)), 2);
        emptyTable1.addOrder(주문_만들기(OrderStatus.COMPLETION));
        emptyTable2.addOrder(주문_만들기(OrderStatus.MEAL));

        OrderTable emptyTable3 = 테이블_만들기(0, true);
        OrderTable emptyTable4 = 테이블_만들기(0, true);
        TableGroup tableGroup2 = 테이블_그룹_만들기(requestTableGroupId);
        tableGroup2.groupingTables(new OrderTables(Arrays.asList(emptyTable3, emptyTable4)), 2);
        emptyTable3.addOrder(주문_만들기(OrderStatus.COOKING));
        emptyTable4.addOrder(주문_만들기(OrderStatus.COMPLETION));

        given(tableGroupRepository.findById(requestTableGroupId))
                .willReturn(Optional.of(tableGroup1))
                .willReturn(Optional.of(tableGroup2));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.ungroup(requestTableGroupId));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.ungroup(requestTableGroupId));
    }

}
