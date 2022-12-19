package kitchenpos.order.domain;

import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class OrderLineItemsTest {

    List<OrderLineItem> orderLineItemList = Arrays.asList(new OrderLineItem(1L, 1l, "메뉴명", new BigDecimal(16000)),
            new OrderLineItem(2L, 1l, "메뉴명", new BigDecimal(16000)));

    @Test
    void 주문_등록시_주문_수량을_추가할_수_있다() {
        OrderLineItems orderLineItems = new OrderLineItems();

        ThrowingCallable 비어있지_않은_주문_수량을_추가 = () -> orderLineItems.addOrderLineItems(orderLineItemList);

        Assertions.assertThatNoException().isThrownBy(비어있지_않은_주문_수량을_추가);
    }

    @Test
    void 주문_등록시_주문_수량이_비어_있으면_안된다() {
        OrderLineItems orderLineItems = new OrderLineItems();

        ThrowingCallable 비어있는_주문_수량을_추가 = () -> orderLineItems.addOrderLineItems(Collections.emptyList());

        Assertions.assertThatIllegalArgumentException().isThrownBy(비어있는_주문_수량을_추가);
    }

    @Test
    void 주문_수량_정보에서_관련_메뉴_ID를_가져올_수_있다() {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.addOrderLineItems(orderLineItemList);

        List<Long> menuIds = orderLineItems.makeMenuIds();

        assertAll(
                () -> Assertions.assertThat(menuIds).hasSize(2),
                () -> Assertions.assertThat(menuIds).contains(1L, 2L)
        );
    }
}
