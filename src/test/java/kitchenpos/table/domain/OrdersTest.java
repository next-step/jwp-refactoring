package kitchenpos.table.domain;

import static kitchenpos.order.sample.OrderSample.완료된_후라이트치킨세트_두개_주문;
import static kitchenpos.order.sample.OrderSample.조리중인_후라이트치킨세트_두개_주문;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import kitchenpos.table.domain.Orders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문들")
class OrdersTest {

    @Test
    @DisplayName("생성")
    void instance() {
        assertThatNoException()
            .isThrownBy(Orders::empty);
    }

    @Test
    @DisplayName("조리 또는 식사 상태가 존재하는지 여부")
    void anyCookingOrMeal() {
        //given
        Orders orders = Orders.empty();
        orders.add(조리중인_후라이트치킨세트_두개_주문());
        orders.add(완료된_후라이트치킨세트_두개_주문());

        //when, then
        assertThat(orders.anyCookingOrMeal()).isTrue();
    }

    @Test
    @DisplayName("주문이 비어있으면 조리 또는 식사 상태가 아님")
    void anyCookingOrMeal_empty() {
        //given
        Orders orders = Orders.empty();

        //when, then
        assertThat(orders.anyCookingOrMeal()).isFalse();
    }
}
