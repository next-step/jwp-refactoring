package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MenuTest {

    private static Product product = new Product("떡볶이", new Price(BigDecimal.valueOf(15000)));
    private static MenuProduct menuProduct = new MenuProduct(product, 2);
    private static MenuGroup menuGroup = new MenuGroup("이벤트 메뉴");

    @Test
    @DisplayName("메뉴 생성")
    public void createMenuTest() {
        // given
        Price menuPrice = new Price(BigDecimal.valueOf(25000));
        MenuProducts menuProducts = new MenuProducts();
        menuProducts.add(menuProduct);

        //when
        Menu menu = new Menu("떡볶이 메뉴", menuPrice, menuGroup, menuProducts);

        //then
        assertThat(menu).isNotNull();
    }

    @Test
    @DisplayName("메뉴가격이 0보다 작으면 메뉴 생성 불가")
    public void createMenuTestFailNegativePriceTest() {
        // given
        MenuProducts menuProducts = new MenuProducts();
        menuProducts.add(menuProduct);

        //when, then
        assertThatThrownBy(
            () -> new Menu("떡볶이 메뉴", new Price(BigDecimal.valueOf(-25000)), menuGroup, menuProducts))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @DisplayName("메뉴가격이 상품들 가격 합보다 크면 메뉴 생성 불가")
    @ValueSource(longs = {31000, 50000, 100000})
    public void createMenuTestFailPriceLessThanProductsPriceSumTest(long price) {
        // given
        Price menuPrice = new Price(BigDecimal.valueOf(price));
        MenuProducts menuProducts = new MenuProducts();
        menuProducts.add(menuProduct);

        // when, then
        assertThatThrownBy(
            () -> new Menu("떡볶이 메뉴", menuPrice, menuGroup, menuProducts))
            .isInstanceOf(IllegalArgumentException.class);
    }

}
