package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class OrderTest {

    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    @DisplayName("식사 또는 조리일땐 주문이 진행중이다")
    void 식사_또는_조리일땐_주문이_진행중이다(String status) {
        Order order = new Order(null, null, status, null, null);
        assertThat(order.isFinished()).isFalse();
    }

    @Test
    @DisplayName("결제완료일 땐 주문이 끝난것이다")
    void 결제완료일_땐_주문이_끝난것이다() {
        Order order = new Order(null, null, OrderStatus.COMPLETION.name(), null, null);
        assertThat(order.isFinished()).isTrue();
    }

    @Test
    @DisplayName("create - OrderCreate의 itemSize와 실제 Item의 Size가 틀리면 IllegalArgumentException이 발생한다")
    void OrderCreate의_ItemSize와_실제_Item의_Size가_틀리면_IllegalArgumentException이_발생한다() {
        // given
        OrderCreate orderCreate = new OrderCreate(null, null, Arrays.asList(1L, 2L, 3L));
        OrderLineItems orderLineItems = new OrderLineItems(
                Arrays.asList(
                        new OrderLineItem(),
                        new OrderLineItem()
                )
        );
        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> Order.create(orderCreate, orderLineItems, null));
    }
}