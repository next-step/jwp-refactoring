package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductPrice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MenuTest {

    @DisplayName("메뉴가격이 없으면 예외발생")
    @Test
    public void throwsExceptionWhenNullAmount() {
        assertThatThrownBy(() -> Menu.builder().price(null).build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴가격이 0보다작으면 예외발생")
    @Test
    public void throwsExceptionWhenNetativeAmount() {
        assertThatThrownBy(() -> Menu.builder().price(BigDecimal.valueOf(-1000)).build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 메뉴상품을 추가할경우 메뉴가격이 상품의 총합보다크면 예외발생")
    @Test
    public void throwsExceptionWhenExceedMenuProductsSumPrice() {
        Product product1 = Product.builder().price(ProductPrice.of(BigDecimal.valueOf(1000))).build();
        Product product2 = Product.builder().price(ProductPrice.of(BigDecimal.valueOf(3000))).build();
        List<MenuProduct> menuProductList = Arrays.asList(
                MenuProduct.builder()
                        .product(product1)
                        .quantity(1)
                        .build(),
                MenuProduct.builder()
                        .product(product2)
                        .quantity(2)
                        .build()
        );
        Menu menu = Menu.builder().price(BigDecimal.valueOf(100000)).build();
        MenuProducts products = MenuProducts.of(menuProductList);

        assertThatThrownBy(() -> menu.addMenuProducts(products))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에서 메뉴상품을 조회할경우 메뉴에 포함되있는 메뉴상품 반환")
    @Test
    public void returnMenuProducts() {
        Product product1 = Product.builder().price(ProductPrice.of(BigDecimal.valueOf(1000))).build();
        Product product2 = Product.builder().price(ProductPrice.of(BigDecimal.valueOf(3000))).build();
        List<MenuProduct> menuProductList = Arrays.asList(
                MenuProduct.builder()
                        .product(product1)
                        .quantity(1)
                        .build(),
                MenuProduct.builder()
                        .product(product2)
                        .quantity(2)
                        .build()
        );
        Menu menu = Menu.builder().price(BigDecimal.valueOf(1000)).build();
        MenuProducts products = MenuProducts.of(menuProductList);
        menu.addMenuProducts(products);

        assertThat(menu.getMenuProducts()).containsAll(menuProductList);
    }
}
