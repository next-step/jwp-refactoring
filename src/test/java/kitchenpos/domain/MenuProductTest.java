package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @Test
    void 메뉴_상품_갯수는_0미만_일_수_없다() {
        Menu 메뉴 = new Menu("메뉴", new BigDecimal(16000), new MenuGroup(1L, "메뉴 그룹"));
        Product 상품 = new Product(1L, "상품", new BigDecimal(16000));

        ThrowingCallable 메뉴_상품_갯수_0미만_인_경우 = () -> new MenuProduct(메뉴, 상품, -1);

        assertThatIllegalArgumentException().isThrownBy(메뉴_상품_갯수_0미만_인_경우);
    }

}
