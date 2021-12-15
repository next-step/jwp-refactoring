package kitchenpos.domain.order;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProducts;

class OrderLineItemsTest {

    @DisplayName("OrderLineItems 는 OrderLineItem 리스트로 생성한다.")
    @Test
    void creat1() {
        // given
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(OrderLineItem.of(Menu.from(1L), 1L));
        orderLineItems.add(OrderLineItem.of(Menu.from(2L), 1L));

        // when & then
        assertThatNoException().isThrownBy(() -> OrderLineItems.from(orderLineItems));
    }

    @DisplayName("OrderLineItems 생성 시, OrderLineItem 리스트가 존재하지 않으면 예외가 발생한다.")
    @ParameterizedTest
    @NullSource
    void creat2(List<OrderLineItem> orderLineItems) {
        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> OrderLineItems.from(orderLineItems))
                                            .withMessageContaining("OrderLineItems 가 존재하지 않습니다.");
    }
}