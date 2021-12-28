package kitchenpos.order;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTest {

    @DisplayName("주문하다")
    @Test
    void createOrder() {

        //given
        OrderTable orderTable = OrderTable.setting(10);

        //when
        Order order = Order.create(orderTable);

        //then
        assertThat(order).isNotNull();
        assertThat(order.isCooking()).isTrue();
    }

    @DisplayName("주문 상태를 완료상태로 변경하다")
    @Test
    void changeOrderStatusToCompletion() {

        //given
        OrderTable orderTable = OrderTable.setting(10);
        Order order = Order.create(orderTable);

        //when
        order.completion();

        //then
        assertThat(order.isCompletion()).isTrue();
    }

    @DisplayName("주문 상태를 다음 상태로 변경한다.(조리 -> 식사)")
    @Test
    void changeOrderStatusFromCookingToMeal() {

        //given
        OrderTable orderTable = OrderTable.setting(10);
        Order order = Order.create(orderTable);

        //when
        order.nextOrderStatus();

        //then
        assertThat(order.isMeal()).isTrue();
    }

    @DisplayName("주문 상태를 다음 상태로 변경한다.(식사 -> 완료)")
    @Test
    void changeOrderStatusFromMealToCompletion() {

        //given
        OrderTable orderTable = OrderTable.setting(10);
        Order order = Order.create(orderTable);
        order.nextOrderStatus();

        //when
        order.nextOrderStatus();

        //then
        assertThat(order.isCompletion()).isTrue();
    }


    @DisplayName("주문 상태를 다음 상태로 변경한다.(완료 -> Null)")
    @Test
    void changeOrderStatusFromCompletionToNull() {

        //given
        OrderTable orderTable = OrderTable.setting(10);
        Order order = Order.create(orderTable);
        order.nextOrderStatus();
        order.nextOrderStatus();

        //then
        assertThatThrownBy(() -> order.nextOrderStatus())
                .isInstanceOf(IllegalStateException.class);
    }
}
