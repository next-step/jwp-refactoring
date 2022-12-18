package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class OrderTest {

    private Order order = new Order(1L,
            Collections.singletonList(new OrderLineItem(1L, 1, "메뉴명", new BigDecimal(16000))));

    @Test
    void 주문_등록시_주문_수량을_추가할_수_있다() {
        ThrowingCallable 주문_수량_추가 = () -> order
                .addLineItems(Arrays.asList(new OrderLineItem(1L, 1l, "메뉴명", new BigDecimal(16000)),
                        new OrderLineItem(2L, 1l, "메뉴명", new BigDecimal(16000))));

        assertThatNoException().isThrownBy(주문_수량_추가);
    }

    @Test
    void 주문_등록시_주문_수량이_비어_있으면_안된다() {
        ThrowingCallable 비어있는_주문_수량_추가 = () -> order.addLineItems(Collections.emptyList());

        assertThatIllegalArgumentException().isThrownBy(비어있는_주문_수량_추가);
    }

    @Test
    void 주문_수량_정보에서_관련_메뉴_ID를_가져올_수_있다() {
        List<Long> menuIds = order.makeMenuIds();

        assertAll(
                () -> assertThat(menuIds).hasSize(1),
                () -> assertThat(menuIds).contains(1L)
        );
    }

    @Test
    void 이미_완료된_주문은_상태를_변경할_수_없다() {
        order.changeStatus(OrderStatus.COMPLETION.name());

        ThrowingCallable 이미_완료된_주문_상태변경_시도 = () -> order.changeStatus(OrderStatus.COMPLETION.name());

        assertThatIllegalArgumentException().isThrownBy(이미_완료된_주문_상태변경_시도);
    }

    @Test
    void 아직_완료지_않은_주문은_상태를_변경할_수_있다() {
        ThrowingCallable 아직_완료되지_않은_주문_상태변경_시도 = () -> order.changeStatus(OrderStatus.COMPLETION.name());

        assertThatNoException().isThrownBy(아직_완료되지_않은_주문_상태변경_시도);
    }
}
