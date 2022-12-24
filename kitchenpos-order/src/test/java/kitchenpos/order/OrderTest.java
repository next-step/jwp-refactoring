package kitchenpos.order;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTest {

    private final OrderTable orderTable = new OrderTable(new NumberOfGuests(10), false);

    @DisplayName("조리중, 식사중 오류발생 체크 로직 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING","MEAL"})
    void checkCookingOrMealExceptionTest(OrderStatus orderStatus) {
        //given
        final Order order = new Order(orderTable.getId(), orderStatus);
        //when
        //then
        assertThatThrownBy(() -> order.checkCookingOrMeal())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("조리중, 식사중 변경 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING","MEAL"})
    void changeOrderStatusTest(OrderStatus changeOrderStatus) {
        //given
        OrderStatus orderStatus = OrderStatus.COOKING;
        if(changeOrderStatus == OrderStatus.COOKING) {
            orderStatus = OrderStatus.MEAL;
        }
        final Order order = new Order(orderTable.getId(), orderStatus);
        //when
        order.change(changeOrderStatus);
        //then
        assertThat(order.getOrderStatus())
                .isEqualTo(changeOrderStatus);
    }

    @DisplayName("식사완료 변경불가 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING","MEAL"})
    void changeOrderStatusCompleteExceptionTest(OrderStatus changeOrderStatus) {
        //given
        final Order order = new Order(orderTable.getId(), OrderStatus.COMPLETION);

        //when
        //then
        assertThatThrownBy(() -> order.change(changeOrderStatus))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
