package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductPrice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuProductTest {

    @DisplayName("메뉴상품을 추가할 수 있다")
    @Test
    public void returnMenuProducts() {
        MenuProduct menuProduct1 = MenuProduct.builder().seq(1l).build();
        MenuProduct menuProduct2 = MenuProduct.builder().seq(2l).build();
        MenuProducts menuProducts = new MenuProducts();

        menuProducts.addMenuProduct(menuProduct1);
        menuProducts.addMenuProduct(menuProduct2);

        assertThat(menuProducts.getMenuProducts()).contains(menuProduct1,menuProduct2);
    }

    @DisplayName("메뉴상품을 추가할 경우 메뉴상품 가격의 합을 구할 수 있다")
    @Test
    public void returnSumOfMenuProductsPrice() {
        Product product1 = Product.builder().price(ProductPrice.of(BigDecimal.valueOf(1000))).build();
        Product product2 = Product.builder().price(ProductPrice.of(BigDecimal.valueOf(2000))).build();
        MenuProduct menuProduct1 = MenuProduct.builder().seq(1l).product(product1).quantity(2).build();
        MenuProduct menuProduct2 = MenuProduct.builder().seq(2l).product(product2).quantity(1).build();
        MenuProducts menuProducts = new MenuProducts();
        menuProducts.addMenuProduct(menuProduct1);
        menuProducts.addMenuProduct(menuProduct2);

        assertThat(menuProducts.getSumOfMenuProductPrice()).isEqualTo(BigDecimal.valueOf(4000));
    }


}
