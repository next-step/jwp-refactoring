package kitchenpos.menu;

import static org.junit.jupiter.api.Assertions.assertTrue;

import kitchenpos.common.domain.Price;
import kitchenpos.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("메뉴상품 일급 컬렉션 테스트")
class MenuProductsTest {

    @Test
    @DisplayName("메뉴상품의 총 금액")
    void getTotalPrice() {
        MenuProducts menuProducts = new MenuProducts();

        Product 후라이드치킨 = Product.of("후라이드치킨", 10000);
        Product 양념치킨 = Product.of("양념치킨", 11000);
        Product 간장치킨 = Product.of("간장치킨", 12000);

        ReflectionTestUtils.setField(후라이드치킨, "id", 1L);
        ReflectionTestUtils.setField(양념치킨, "id", 2L);
        ReflectionTestUtils.setField(간장치킨, "id", 3L);

        menuProducts.add(MenuProduct.of(후라이드치킨, 1L));
        menuProducts.add(MenuProduct.of(양념치킨, 1L));
        menuProducts.add(MenuProduct.of(간장치킨, 2L));

        assertTrue(menuProducts.getTotalPrice().equals(Price.fromInteger(45000)));

    }
}