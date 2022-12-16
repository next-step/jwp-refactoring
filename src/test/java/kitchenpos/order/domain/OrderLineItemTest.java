package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class OrderLineItemTest {

    @Test
    void 주문_항목_갯수가_0미만_일_수_없다() {
        ThrowingCallable 주문_항목_갯수가_0미만_인_경우 = () -> new OrderLineItem(1L, -1);

        assertThatIllegalArgumentException().isThrownBy(주문_항목_갯수가_0미만_인_경우);
    }

}
