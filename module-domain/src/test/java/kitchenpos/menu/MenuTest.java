package kitchenpos.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.product.Product;

public class MenuTest {

    private MenuGroup menuGroup;
    private List<Product> productList;
    private Product 후라이드;
    private Product 양념;

    @BeforeEach
    void setUp() {
        menuGroup = new MenuGroup(1L, "신상");
        후라이드 = new Product(1L, "후라이드", BigDecimal.valueOf(10000));
        양념 = new Product(1L, "양념", BigDecimal.valueOf(15000));
        productList = Arrays.asList(후라이드, 양념);
    }

    @Test
    @DisplayName("Menu 정상 생성 테스트")
    void create() {

        Menu 정상 = Menu.create("후라이드+양념", BigDecimal.valueOf(1000), menuGroup);

        assertThat(정상.getMenuGroupId()).isEqualTo(menuGroup.getId());
        assertThat(정상.getName()).isEqualTo("후라이드+양념");
    }

    @Test
    @DisplayName("menuProducts가 빈 값으로 들어올 시 에러를 뱉는다. ")
    void noProducts() {
        assertThrows(IllegalArgumentException.class, () -> {
            Menu menu = Menu.create("name", BigDecimal.valueOf(100), menuGroup);
            menu.productsAssginMenu(BigDecimal.valueOf(10), MenuProducts.of(Collections.emptyList()));
        });
    }

    @Test
    @DisplayName("request 가격이 제품 가격의 합보다 크면 에러를 뱉는다. ")
    void overPrice() {
        List<MenuProduct> menuProductList = Arrays
            .asList(new MenuProduct(new Product("양념", BigDecimal.valueOf(100)), 1L),
                new MenuProduct(new Product("후라이드", BigDecimal.valueOf(100)), 1L));
        assertThrows(IllegalArgumentException.class, () -> {
            Menu menu = Menu.create("name", BigDecimal.valueOf(10000), menuGroup);
            menu.productsAssginMenu(BigDecimal.valueOf(1000000), MenuProducts.of(menuProductList));
        });
    }
}
