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

class OrderTest {

    @Test
    void 주문_등록시_주문_수량을_추가할_수_있다() {
        Order order = new Order(new OrderTable(1L, 1L, 1, false));

        ThrowingCallable 주문_수량_추가 = () -> order
                .addLineItems(Arrays.asList(new OrderLineItem(1L, 1l), new OrderLineItem(2L, 1l)));

        assertThatNoException().isThrownBy(주문_수량_추가);
    }

    @Test
    void 주문_등록시_주문_수량이_비어_있으면_안된다() {
        Order order = new Order(new OrderTable(1L, 1L, 1, false));

        ThrowingCallable 비어있는_주문_수량_추가 = () -> order.addLineItems(Collections.emptyList());

        assertThatIllegalArgumentException().isThrownBy(비어있는_주문_수량_추가);
    }

    @Test
    void 주문_수량_정보에서_관련_메뉴_ID를_가져올_수_있다() {
        Order order = new Order(new OrderTable(1L, 1L, 1, false));
        order.addLineItems(Arrays.asList(new OrderLineItem(1L, 1l), new OrderLineItem(2L, 1l)));

        List<Long> menuIds = order.makeMenuIds();

        assertAll(
                () -> assertThat(menuIds).hasSize(2),
                () -> assertThat(menuIds).contains(1L, 2L)
        );
    }

    @Test
    void 등록_된_메뉴만_주문_등록을_할_수_있다() {
        Order order = new Order(new OrderTable(1L, 1L, 1, false));
        order.addLineItems(Arrays.asList(new OrderLineItem(1L, 1l), new OrderLineItem(2L, 1l)));

        ThrowingCallable 등록된_메뉴의_갯수가_불일치_할_경우 = () -> order.validateOrderLineItemsSizeAndMenuCount(1);

        assertThatIllegalArgumentException().isThrownBy(등록된_메뉴의_갯수가_불일치_할_경우);
    }

    @Test
    void 이미_완료된_주문은_상태를_변경할_수_없다() {
        Order order = new Order(new OrderTable(1L, 1L, 1, false));
        order.changeStatus(OrderStatus.COMPLETION.name());

        ThrowingCallable 이미_완료된_주문_상태변경_시도 = () -> order.changeStatus(OrderStatus.COMPLETION.name());

        assertThatIllegalArgumentException().isThrownBy(이미_완료된_주문_상태변경_시도);
    }

    @Test
    void 아직_완료지_않은_주문은_상태를_변경할_수_있다() {
        Order order = new Order(new OrderTable(1L, 1L, 1, false));

        ThrowingCallable 아직_완료되지_않은_주문_상태변경_시도 = () -> order.changeStatus(OrderStatus.COMPLETION.name());

        assertThatNoException().isThrownBy(아직_완료되지_않은_주문_상태변경_시도);
    }
}
