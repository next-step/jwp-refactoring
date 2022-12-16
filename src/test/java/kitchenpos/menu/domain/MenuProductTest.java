package kitchenpos.menu.domain;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @Test
    void 메뉴_상품_갯수는_0미만_일_수_없다() {
        Product 후라이드치킨_상품 = new Product(1L, "후라이드치킨", new BigDecimal(16000.00));
        Menu 메뉴 = new Menu("메뉴", new BigDecimal(16000), 1L,
                singletonList(new MenuProduct(new Menu("메뉴", new BigDecimal(15000), 1L,
                        singletonList(new MenuProduct(null, new Product(null, "상품", new BigDecimal(16000)), 1))),
                        후라이드치킨_상품, 1)));
        Product 상품 = new Product(1L, "상품", new BigDecimal(16000));

        ThrowingCallable 메뉴_상품_갯수_0미만_인_경우 = () -> new MenuProduct(메뉴, 상품, -1);

        assertThatIllegalArgumentException().isThrownBy(메뉴_상품_갯수_0미만_인_경우);
    }

}
