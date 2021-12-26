package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("메뉴 상품 도메인 테스트")
public class MenuProductTest {
    @DisplayName("메뉴 상품을 생성한다.")
    @Test
    void 메뉴_상품_생성() {
        // given
        Product product = Product.of(1L, "양념치킨", Price.of(BigDecimal.valueOf(20000)));
        Quantity quantity = Quantity.of(2L);
        MenuGroup menuGroup = MenuGroup.of(1L, "튀김류");
        Menu menu = Menu.of(1L, "치킨콜라세트", Price.of(BigDecimal.valueOf(19000)), menuGroup);

        // when
        MenuProduct menuProduct = MenuProduct.of(product, quantity);
        menuProduct.addMenu(menu);

        // then
        assertAll(
                () -> assertThat(menuProduct.getQuantity()).isEqualTo(2L),
                () -> assertThat(menuProduct.getProduct()).isEqualTo(product),
                () -> assertThat(menuProduct.getMenu()).isEqualTo(menu)
        );
    }

    @DisplayName("상품의 가격과 수량을 곱한 총 합계 금액을 계산한다.")
    @Test
    void 메뉴_상품_합계_금액_계산() {
        // given
        MenuProduct menuProduct = getMenuProduct(1L, "양념치킨", 50000, 3L);

        // when
        BigDecimal totalPrice = menuProduct.totalPrice();

        // then
        assertThat(totalPrice).isEqualTo(BigDecimal.valueOf(150000));
    }

    public static MenuProduct getMenuProduct(Long productId, String productName, int price, Long productQuantity) {
        Product product = Product.of(productId, productName, Price.of(BigDecimal.valueOf(price)));
        Quantity quantity = Quantity.of(productQuantity);

        return MenuProduct.of(product, quantity);
    }
}
