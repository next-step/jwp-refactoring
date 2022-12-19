package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class OrderLineItemMenuPriceTest {

    @Test
    void 메뉴의_가격은_빈값이면_안된다() {
        ThrowingCallable 메뉴의_가격이_빈_값인_경우 = () -> new OrderLineItemMenuPrice(null);

        assertThatIllegalArgumentException().isThrownBy(메뉴의_가격이_빈_값인_경우);
    }

    @Test
    void 메뉴의_가격_최소_0원_이상이어야_한다() {
        ThrowingCallable 메뉴의_가격이_0원_미만인_경우 = () -> new OrderLineItemMenuPrice(new BigDecimal(-1));

        assertThatIllegalArgumentException().isThrownBy(메뉴의_가격이_0원_미만인_경우);
    }
}
