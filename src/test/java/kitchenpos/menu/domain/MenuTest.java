package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import java.util.Collections;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    void 메뉴_가격은_0원_이상_이어야_한다() {
        BigDecimal 가격이_0원_미만 = new BigDecimal(-1);

        ThrowingCallable 가격이_0원_미만일_경우 = () -> new Menu("메뉴", 가격이_0원_미만, 1L,
                Collections.singletonList(new MenuProduct(new Menu(), 1L, 1)));

        assertThatIllegalArgumentException().isThrownBy(가격이_0원_미만일_경우);
    }

    @Test
    void 메뉴_가격이_null_이면_안된다() {
        BigDecimal 가격이_null = null;

        ThrowingCallable 가격이_null_일_경우 = () -> new Menu("메뉴", 가격이_null, 1L,
                Collections.singletonList(new MenuProduct(new Menu(), 1L, 1)));

        assertThatIllegalArgumentException().isThrownBy(가격이_null_일_경우);
    }
}
