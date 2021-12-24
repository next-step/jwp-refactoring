package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 상태 테스트")
class OrderStatusTest {

    @DisplayName("주문 완료 상태 확인 true 테스트")
    @Test
    void isCompletion_true() {
        // given
        OrderStatus orderStatus = OrderStatus.COMPLETION;

        // when & then
        assertThat(orderStatus.isCompletion()).isTrue();
    }

    @DisplayName("주문 완료 상태 확인 false 테스트")
    @Test
    void isCompletion_false() {
        // given
        OrderStatus orderStatusCooking = OrderStatus.COOKING;
        OrderStatus orderStatusMeal = OrderStatus.MEAL;

        // when & then
        assertAll(
                () -> assertThat(orderStatusCooking.isCompletion()).isFalse()
                , () -> assertThat(orderStatusMeal.isCompletion()).isFalse()
        );
    }
}
