package kitchenpos.menu.domain;

import java.math.BigDecimal;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

public class MenuPriceTest {

    @Test
    void 메뉴의_가격은_빈값이면_안된다() {
        ThrowingCallable 메뉴의_가격이_빈_값인_경우 = () -> new MenuPrice(null);

        Assertions.assertThatIllegalArgumentException().isThrownBy(메뉴의_가격이_빈_값인_경우);
    }

    @Test
    void 메뉴의_가격_최소_0원_이상이어야_한다() {
        ThrowingCallable 메뉴의_가격이_0원_미만인_경우 = () -> new MenuPrice(new BigDecimal(-1));

        Assertions.assertThatIllegalArgumentException().isThrownBy(메뉴의_가격이_0원_미만인_경우);
    }
}
