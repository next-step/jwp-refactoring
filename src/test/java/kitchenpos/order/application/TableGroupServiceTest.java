package kitchenpos.order.application;

import kitchenpos.order.domain.*;
import kitchenpos.order.exceptions.InputOrderDataErrorCode;
import kitchenpos.order.exceptions.InputOrderDataException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@DisplayName("Table Group 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

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
    @DisplayName("테이블 그룹을 해제한다.")
    void deleteGroupTest() {
        //given
        OrderTable orderTable = mock(OrderTable.class);
        OrderTable orderTable2 = mock(OrderTable.class);
        OrderTables orderTables = new OrderTables(Arrays.asList(orderTable, orderTable2));
        TableGroup tableGroup1 = new TableGroup(orderTables, LocalDateTime.now());

        //when
        tableGroup1.cancleGroup();

        assertThat(tableGroup1.getOrderTables().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("식사중이거나 조리중인 주문은 테이블 그룹을 해지할 수 없다.")
    void deleteCookingOrEatingOrderGroupTest() {
        //given
        OrderTable orderTable = new OrderTable(5,false);
        OrderTable orderTable2 = new OrderTable(5,false);
        OrderTables orderTables = new OrderTables(Arrays.asList(orderTable, orderTable2));

        OrderLineItem orderLineItem = new OrderLineItem(1L, 5);
        OrderLineItems orderLineItems = new OrderLineItems(Arrays.asList(orderLineItem));

        orderTable.addOrder(new Order(orderTable, orderLineItems));
        TableGroup tableGroup1 = new TableGroup(orderTables, LocalDateTime.now());

        //when
        assertThatThrownBy(() -> {
            tableGroup1.cancleGroup();
        }).isInstanceOf(InputOrderDataException.class)
                .hasMessageContaining(InputOrderDataErrorCode.THE_ORDER_IS_COOKING_OR_IS_EATING.errorMessage());
    }
}
