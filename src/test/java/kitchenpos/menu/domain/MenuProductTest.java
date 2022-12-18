package kitchenpos.menu.domain;

import kitchenpos.common.Name;
import kitchenpos.common.Price;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static kitchenpos.menu.domain.MenuFixture.menuA;
import static kitchenpos.menu.domain.MenuProduct.PRODUCT_NULL_EXCEPTION_MESSAGE;
import static kitchenpos.menu.domain.MenuProduct.QUANTITY_NULL_EXCEPTION_MESSAGE;
import static kitchenpos.product.domain.ProductFixture.productA;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메뉴 상품")
class MenuProductTest {

    @DisplayName("메뉴 상품을 생성한다.")
    @Test
    void create() {
        assertThatNoException().isThrownBy(() -> new MenuProduct(menuA(), new Product(new Name("상품"), new Price(BigDecimal.ONE)), 1L));
    }

//    @Deprecated
//    @DisplayName("메뉴 상품을 생성한다. / 메뉴를 필수로 갖는다.")
//    @Test
//    void create_fail_menu() {
//        assertThatThrownBy(() -> new MenuProduct(null, 1L, 1L))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining(MENU_NULL_EXCEPTION_MESSAGE);
//    }

    @DisplayName("메뉴 상품을 생성한다. / 상품을 필수로 갖는다.")
    @Test
    void create_fail_product() {
        assertThatThrownBy(() -> new MenuProduct(menuA(), null, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(PRODUCT_NULL_EXCEPTION_MESSAGE);
    }

    @DisplayName("메뉴 상품을 생성한다. / 갯수를 필수로 갖는다.")
    @Test
    void create_fail_quantity() {
        assertThatThrownBy(() -> new MenuProduct(menuA(), productA(), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(QUANTITY_NULL_EXCEPTION_MESSAGE);
    }
}
