package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import java.util.Arrays;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class MenuTest {

    MenuProduct 후라이드치킨 = new MenuProduct(null, new Product(1L, "후라이드치킨", new BigDecimal(16000)), 1);
    MenuProduct 양념치킨 = new MenuProduct(null, new Product(1L, "양념치킨", new BigDecimal(16000)), 1);
    MenuGroup menuGroup = new MenuGroup();

    @Test
    void 메뉴_가격은_0원_이상_이어야_한다() {
        BigDecimal 가격이_0원_미만 = new BigDecimal(-1);

        ThrowingCallable 가격이_0원_미만일_경우 = () -> new Menu("메뉴", 가격이_0원_미만, menuGroup);

        assertThatIllegalArgumentException().isThrownBy(가격이_0원_미만일_경우);
    }

    @Test
    void 메뉴_가격이_null_이면_안된다() {
        BigDecimal 가격이_null = null;

        ThrowingCallable 가격이_null_일_경우 = () -> new Menu("메뉴", 가격이_null, menuGroup);

        assertThatIllegalArgumentException().isThrownBy(가격이_null_일_경우);
    }

    @Test
    void 메뉴의_가격은_메뉴상품들_가격의_합보다_낮아야_한다() {
        Menu 메뉴 = new Menu("메뉴", new BigDecimal(40000), menuGroup);
        메뉴.addMenuProducts(Arrays.asList(후라이드치킨, 양념치킨));

        ThrowingCallable 메뉴_가격이_상품들_가격의_합보다_높은_경우 = () -> 메뉴.validateProductsPrice();

        assertThatIllegalArgumentException().isThrownBy(메뉴_가격이_상품들_가격의_합보다_높은_경우);
    }
}
