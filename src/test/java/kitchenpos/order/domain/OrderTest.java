package kitchenpos.order.domain;

import static kitchenpos.order.OrderFixture.주문항목;
import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static kitchenpos.table.TableFixture.일번테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    @DisplayName("주문항목이 없는경우 에러 발생")
    void noOrderLineItemException() {
        //when & then
        assertThatThrownBy(() -> new Order(1L, 일번테이블, null, null,
            Collections.emptyList()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("주문 항목이 없습니다.");
    }

    @Test
    @DisplayName("주문이 계산 완료된 이후 상태 변경시 에러 발생")
    void changeOrderStatusWhenItCompleted() {
        //given
        Order 주문 = new Order(1L, 일번테이블, COMPLETION, null,
            Collections.singletonList(주문항목));

        //when & then
        assertThatThrownBy(() -> 주문.changeOrderStatus(OrderStatus.MEAL))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("계산이 완료 되었습니다. 주문 상태 변경이 불가능 합니다.");
    }

    @Test
    @DisplayName("주문 테이블정보가 없으면 에러 발생")
    void noOrderTableException() {
        //when
        assertThatThrownBy(() -> new Order(1L, null, null, null, Collections.singletonList(주문항목)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("주문 테이블 정보가 없습니다.");
    }

    @Test
    @DisplayName("주문 테스트")
    void order() {
        //given
        Order 주문 = new Order(1L, 일번테이블, null, null,
            Collections.singletonList(주문항목));

        //when
        주문.order();

        //then
        assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(주문.getOrderedTime()).isNotNull();
    }

    @Test
    @DisplayName("주문 상태 변경 테스트")
    void changeOrderStatus() {
        //given
        Order 주문 = new Order(1L, 일번테이블, null, null,
            Collections.singletonList(주문항목));

        //when
        주문.changeOrderStatus(OrderStatus.COOKING);

        //then
        assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @Test
    @DisplayName("조리중이거나 식사중 여부 확인")
    void onCookingOrMeal() {
        //given
        Order 조리중 = new Order(1L, 일번테이블, COOKING, null,
            Collections.singletonList(주문항목));

        //when & then
        assertThat(조리중.onCookingOrMeal()).isTrue();

        //given
        Order 식사중 = new Order(1L, 일번테이블, MEAL, null,
            Collections.singletonList(주문항목));

        //when & then
        assertThat(식사중.onCookingOrMeal()).isTrue();

        //given
        Order 계산완료 = new Order(1L, 일번테이블, COMPLETION, null,
            Collections.singletonList(주문항목));

        //when & then
        assertThat(계산완료.onCookingOrMeal()).isFalse();
    }

}
