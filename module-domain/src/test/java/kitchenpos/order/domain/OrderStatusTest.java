package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderStatusTest {

    @DisplayName("계산 완료 상태 확인")
    @Test
    void isCompletion() {
        OrderStatus completion = OrderStatus.COMPLETION;
        OrderStatus meal = OrderStatus.MEAL;
        OrderStatus cooking = OrderStatus.COOKING;

        assertThat(completion.isCompletion()).isTrue();
        assertThat(meal.isCompletion()).isFalse();
        assertThat(cooking.isCompletion()).isFalse();

    }

    @DisplayName("조리 또는 식사 상태 확인")
    @Test
    void isCookingOrMeal() {
        OrderStatus completion = OrderStatus.COMPLETION;
        OrderStatus meal = OrderStatus.MEAL;
        OrderStatus cooking = OrderStatus.COOKING;

        assertThat(completion.isCookingOrMeal()).isFalse();
        assertThat(meal.isCookingOrMeal()).isTrue();
        assertThat(cooking.isCookingOrMeal()).isTrue();

    }
}
