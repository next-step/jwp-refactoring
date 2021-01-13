package kitchenpos.menu.domain;

import kitchenpos.common.Money;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DisplayName("메뉴")
class MenuTest {

    private MenuGroup menuGroup;
    private MenuProducts menuProducts;

    @BeforeEach
    void setUp() {
        menuGroup = new MenuGroup("추천메뉴");

        Product 강정치킨 = new Product("강정치킨", Money.valueOf(19000));
        Product 후라이드 = new Product("후라이드", Money.valueOf(15000));
        menuProducts = new MenuProducts(Arrays.asList(
                new MenuProduct(강정치킨, 2),
                new MenuProduct(후라이드, 2)));
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void create() {
        // when
        Menu menu = new Menu("치킨두마리", menuProducts.price(), menuGroup, menuProducts);

        // then
        assertThat(menu).isNotNull();
    }

    @DisplayName("메뉴 상품이 하나 이상 존재해야 한다.")
    @Test
    void requiredMenuProduct() {
        // given
        MenuProducts emptyProducts = new MenuProducts();

        // when / then
        assertThrows(IllegalArgumentException.class, () ->
                new Menu("치킨두마리", menuProducts.price(), menuGroup, emptyProducts));
    }

    @DisplayName("메뉴 가격이 메뉴 상품들의 가격 총합보다 비쌀 수 없다.")
    @Test
    void priceStrategy() {
        // given
        Money overMoney = menuProducts.price().add(Money.valueOf(1000));

        // when / then
        assertThrows(IllegalArgumentException.class, () ->
                new Menu("치킨두마리", overMoney, menuGroup, menuProducts));
    }
}
