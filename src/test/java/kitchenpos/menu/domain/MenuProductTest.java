package kitchenpos.menu.domain;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @Test
    void 메뉴_상품_갯수는_0미만_일_수_없다() {
        Menu 메뉴 = new Menu("메뉴", new BigDecimal(16000), 1L,
                singletonList(new MenuProduct(new Menu("메뉴", new BigDecimal(15000), 1L,
                        singletonList(new MenuProduct(null, 1L, 1))), 1L, 1)));

        ThrowingCallable 메뉴_상품_갯수_0미만_인_경우 = () -> new MenuProduct(메뉴, 1L, -1);

        assertThatIllegalArgumentException().isThrownBy(메뉴_상품_갯수_0미만_인_경우);
    }

}
