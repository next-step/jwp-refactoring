package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class OrderLineItemsTest {

    List<OrderLineItem> orderLineItemList = Arrays.asList(new OrderLineItem(1L, 1l), new OrderLineItem(2L, 1l));

    @Test
    void 주문_등록시_주문_수량을_추가할_수_있다() {
        OrderLineItems orderLineItems = new OrderLineItems();

        ThrowingCallable 비어있지_않은_주문_수량을_추가 = () -> orderLineItems.addOrderLineItems(orderLineItemList);

        assertThatNoException().isThrownBy(비어있지_않은_주문_수량을_추가);
    }

    @Test
    void 주문_등록시_주문_수량이_비어_있으면_안된다() {
        OrderLineItems orderLineItems = new OrderLineItems();

        ThrowingCallable 비어있는_주문_수량을_추가 = () -> orderLineItems.addOrderLineItems(Collections.emptyList());

        assertThatIllegalArgumentException().isThrownBy(비어있는_주문_수량을_추가);
    }

    @Test
    void 주문_수량_정보에서_관련_메뉴_ID를_가져올_수_있다() {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.addOrderLineItems(orderLineItemList);

        List<Long> menuIds = orderLineItems.makeMenuIds();

        assertAll(
                () -> assertThat(menuIds).hasSize(2),
                () -> assertThat(menuIds).contains(1L, 2L)
        );
    }

    @Test
    void 등록_된_메뉴만_주문_등록을_할_수_있다() {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.addOrderLineItems(orderLineItemList);

        ThrowingCallable 등록된_메뉴의_갯수가_불일치_할_경우 = () -> orderLineItems.validateOrderLineItemsSizeAndMenuCount(1);

        assertThatIllegalArgumentException().isThrownBy(등록된_메뉴의_갯수가_불일치_할_경우);
    }
}
