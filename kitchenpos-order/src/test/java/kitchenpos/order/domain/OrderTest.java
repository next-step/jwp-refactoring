package kitchenpos.order.domain;

import java.util.Collections;
import kitchenpos.common.exception.InvalidParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.order.domain.OrderLineItemTestFixture.짜장_탕수육_주문_항목;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 테스트")
class OrderTest {
    @Test
    @DisplayName("주문 객체를 생성한다.")
    void create() {
        // when
        Order actual = Order.of(1L, Collections.singletonList(짜장_탕수육_주문_항목));

        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isInstanceOf(Order.class)
        );
    }

    @Test
    @DisplayName("등록되지 않은 테이블로 생성")
    void createByEmptyOrderTable() {
        // when & then
        assertThatThrownBy(() -> Order.of(null, Collections.singletonList(짜장_탕수육_주문_항목)))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("등록된 테이블이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("주문 상태 변경")
    void changeStatus() {
        // given
        Order order = Order.of(1L, Collections.singletonList(짜장_탕수육_주문_항목));

        // when
        order.changeStatus(OrderStatus.COMPLETION);

        // then
        assertThat(order.orderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @Test
    @DisplayName("주문 완료된 주문 상태 변경시 에러")
    void changeStatusByCompletionOrderTable() {
        // given
        Order order = Order.of(1L, Collections.singletonList(짜장_탕수육_주문_항목));
        order.changeStatus(OrderStatus.COMPLETION);

        // when & then
        assertThatThrownBy(() -> order.changeStatus(OrderStatus.COMPLETION))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("이미 완료된 주문입니다.");
    }

    @Test
    @DisplayName("주문 상태가 조리 중 또는 식사 중일 경우 예외")
    void validateStatus() {
        // given
        Order order = Order.of(1L, Collections.singletonList(짜장_탕수육_주문_항목));

        // when & then
        assertThatThrownBy(order::validateCookingAndMeal)
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("주문 상태가 조리 중 입니다.");

        // given
        order.changeStatus(OrderStatus.MEAL);

        // when & then
        assertThatThrownBy(order::validateCookingAndMeal)
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("주문 상태가 식사 중 입니다.");
    }
}
