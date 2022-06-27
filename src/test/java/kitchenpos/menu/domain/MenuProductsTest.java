package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuProductsTest {
    @Test
    @DisplayName("메뉴 상품의 가격 총합을 구한다.")
    void calculateTotalPrice() {
        Product product = Product.of("허니콤보", 19_000L);
        Product otherProduct = Product.of("레드콤보", 20_000L);
        MenuProduct menuProduct = MenuProduct.createMenuProduct(product, 1L);
        MenuProduct otherMenuProduct = MenuProduct.createMenuProduct(otherProduct, 1L);
        MenuProducts menuProducts = new MenuProducts(Lists.list(menuProduct, otherMenuProduct));

        Price totalPrice = menuProducts.calculateTotalPrice();

        assertThat(totalPrice.getPrice()).isEqualTo(BigDecimal.valueOf(39_000L));
    }
}
