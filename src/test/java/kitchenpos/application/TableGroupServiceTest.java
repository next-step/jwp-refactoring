package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("단체 지정 Service 단위 테스트 - Stub")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;
    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
    }

    @DisplayName("단체 지정을 등록 한다.")
    @Test
    void create() {
        //given
        long generateTableGroupId = 1;
        OrderTable emptyTable1 = new OrderTable(1L, null, 0, true);
        OrderTable emptyTable2 = new OrderTable(2L, null, 0, true);
        TableGroup request = new TableGroup(null, null, Arrays.asList(emptyTable1, emptyTable2));

        given(orderTableDao.findAllByIdIn(Arrays.asList(emptyTable1.getId(), emptyTable2.getId())))
                .willReturn(Arrays.asList(emptyTable1, emptyTable2));
        doAnswer(invocation -> new TableGroup(generateTableGroupId, request.getCreatedDate(), request.getOrderTables()))
                .when(tableGroupDao).save(request);

        //when
        TableGroup result = tableGroupService.create(request);

        //then
        List<OrderTable> orderTables = result.getOrderTables();
        assertThat(orderTables.get(0).isEmpty()).isFalse();
        assertThat(orderTables.get(0).getTableGroupId()).isEqualTo(generateTableGroupId);
        assertThat(orderTables.get(1).isEmpty()).isFalse();
        assertThat(orderTables.get(1).getTableGroupId()).isEqualTo(generateTableGroupId);
    }

    @DisplayName("테이블이 2개 미만이거나 비어있으면 단체 지정을 등록 할 수 없다.")
    @Test
    void create_empty_or_less_then_two() {
        //given
        OrderTable emptyTable1 = new OrderTable(1L, null, 0, true);
        TableGroup request_single = new TableGroup(null, null, Collections.singletonList(emptyTable1));
        TableGroup request_empty = new TableGroup(null, null, Collections.emptyList());

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(request_single));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(request_empty));
    }

    @DisplayName("등록 되어 있지 않은 테이블이 있는 경우 단체 지정을 등록 할 수 없다.")
    @Test
    void create_not_registered_table() {
        //given
        OrderTable emptyTable1 = new OrderTable(1L, null, 0, true);
        OrderTable emptyTable2 = new OrderTable(2L, null, 0, true);
        TableGroup request = new TableGroup(null, null, Arrays.asList(emptyTable1, emptyTable2));

        given(orderTableDao.findAllByIdIn(Arrays.asList(emptyTable1.getId(), emptyTable2.getId())))
                .willReturn(Arrays.asList(emptyTable1));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(request));
    }

    @DisplayName("주문 테이블이 있는 경우 단체 지정을 등록 할 수 없다.")
    @Test
    void create_in_order_table() {
        //given
        OrderTable orderTable1 = new OrderTable(1L, null, 3, false);
        OrderTable emptyTable1 = new OrderTable(2L, null, 0, true);
        TableGroup request = new TableGroup(null, null, Arrays.asList(orderTable1, emptyTable1));

        given(orderTableDao.findAllByIdIn(Arrays.asList(orderTable1.getId(), emptyTable1.getId())))
                .willReturn(Arrays.asList(orderTable1, emptyTable1));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(request));
    }

    @DisplayName("이미 단체 지정된 테이블이 있는 경우 단체 지정 등록 할 수 없다.")
    @Test
    void create_already() {
        //given
        OrderTable already = new OrderTable(1L, 1L, 3, true);
        OrderTable emptyTable1 = new OrderTable(2L, null, 0, true);
        TableGroup request = new TableGroup(null, null, Arrays.asList(already, emptyTable1));

        given(orderTableDao.findAllByIdIn(Arrays.asList(already.getId(), emptyTable1.getId())))
                .willReturn(Arrays.asList(already, emptyTable1));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(request));
    }

    @DisplayName("단체 지정을 해제 한다.")
    @Test
    void ungroup() {
        //given
        long requestTableGroupId = 1L;
        OrderTable orderTable1 = new OrderTable(1L, 1L, 2, false);
        OrderTable orderTable2 = new OrderTable(2L, 1L, 3, false);

        given(orderTableDao.findAllByTableGroupId(requestTableGroupId))
                .willReturn(Arrays.asList(orderTable1, orderTable2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(orderTable1.getId(), orderTable2.getId())
                , Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(false);

        //when
        tableGroupService.ungroup(requestTableGroupId);

        //then
        assertThat(orderTable1.getTableGroupId()).isNull();
        assertThat(orderTable2.getTableGroupId()).isNull();
    }

    @DisplayName("주문 상태가 조리, 식사인 경우가 있으면 단체 지정 해제 할 수 없다.")
    @Test
    void ungroup_order_status_cooking_meal() {
        //given
        long requestTableGroupId = 1L;
        OrderTable orderTable1 = new OrderTable(1L, 1L, 2, false);
        OrderTable orderTable2 = new OrderTable(2L, 1L, 3, false);

        given(orderTableDao.findAllByTableGroupId(requestTableGroupId))
                .willReturn(Arrays.asList(orderTable1, orderTable2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(orderTable1.getId(), orderTable2.getId())
                , Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(true);

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.ungroup(requestTableGroupId));
    }

}
