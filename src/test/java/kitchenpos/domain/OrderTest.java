package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class OrderTest {

    @Test
    @DisplayName("주문 생성")
    public void createOrderTest() {
        //when
        Order order = new Order();

        //then
        assertThat(order).isNotNull();
    }

    @ParameterizedTest
    @CsvSource(value = {"MEAL:false", "COOKING:false", "COMPLETION:true"}, delimiter = ':')
    @DisplayName("주문의 COMPLETION 상태 여부 확인")
    public void isCompletion(String status, boolean expected) {
        //when
        OrderTable orderTable = new OrderTable();
        OrderLineItems orderLineItems = new OrderLineItems();
        Order order = new Order(orderTable, OrderStatus.valueOf(status), orderLineItems);

        //then
        assertThat(order.isCompleted()).isEqualTo(expected);
    }
    @ParameterizedTest
    @ValueSource(strings = {"MEAL", "COOKING", "COMPLETION"})
    @DisplayName("주문 상태 변경 확인")
    public void changeOrderStatus(String status) {
        //when
        Order order = new Order();
        OrderStatus orderStatus = OrderStatus.valueOf(status);
        order.changeOrderStatus(orderStatus);

        //then
        assertThat(order.getOrderStatus()).isEqualTo(orderStatus);
    }
}
