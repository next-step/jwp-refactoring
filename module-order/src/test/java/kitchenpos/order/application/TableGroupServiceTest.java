package kitchenpos.order.application;

import kitchenpos.order.domain.*;
import kitchenpos.order.exceptions.InputOrderDataErrorCode;
import kitchenpos.order.exceptions.InputOrderDataException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@DisplayName("Table Group 서비스 테스트")
@SpringBootTest(classes = OrderTableValidator.class)
class TableGroupServiceTest {

    @Autowired
    private OrderTableValidator orderTableValidator;

    @Autowired
    public TableGroupServiceTest(OrderTableValidator orderTableValidator) {
        this.orderTableValidator = orderTableValidator;
    }

    @Test
    @DisplayName("테이블 그룹을 생성한다.")
    void createTableGroupTest() {
        //given
        OrderTable orderTable = mock(OrderTable.class);
        OrderTable orderTable2 = mock(OrderTable.class);
        TableGroup tableGroup1 = mock(TableGroup.class);

        //when
        when(tableGroup1.getOrderTables()).thenReturn(Arrays.asList(orderTable, orderTable2));

        //then
        assertThat(tableGroup1.getOrderTables()).contains(orderTable, orderTable2);
    }

    @Test
    @DisplayName("식사중이거나 조리중인 주문은 테이블 그룹을 해지할 수 없다.")
    void deleteCookingOrEatingOrderGroupTest() {
        //given
        OrderTable orderTable = new OrderTable(5, false);
        OrderTable orderTable2 = new OrderTable(5, false);
        OrderTables orderTables = new OrderTables(Arrays.asList(orderTable, orderTable2));

        OrderLineItem orderLineItem = new OrderLineItem(1L, 5);
        OrderLineItems orderLineItems = new OrderLineItems(Arrays.asList(orderLineItem));
        Order order = new Order(1L, orderLineItems);
        TableGroup tableGroup1 = new TableGroup(orderTables);

        //when
        Assertions.assertThatThrownBy(() -> {
                    tableGroup1.cancleGroup(Arrays.asList(order), orderTableValidator);
                }).isInstanceOf(InputOrderDataException.class)
                .hasMessageContaining(InputOrderDataErrorCode.THE_ORDER_IS_COOKING_OR_IS_EATING.errorMessage());
    }
}
