package kitchenpos.menu.domain;

import kitchenpos.common.domian.Price;
import kitchenpos.common.domian.Quantity;
import kitchenpos.menuproduct.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("상품 수랑 컬렉션 테스트")
class ProductsQuantitiesTest {

    private ProductsQuantities productsQuantities;

    @BeforeEach
    void setup() {
        List<Product> products = Arrays.asList(new Product(1L, "순대", new Price(1000)));
        Map<Long, Quantity> quantities = new HashMap<Long, Quantity>() {{
            put(1L, new Quantity(1L));
        }};
        productsQuantities = new ProductsQuantities(new Products(products, 1), new Quantities(quantities, 1));
    }

    @DisplayName("생성")
    @Test
    void create() {
        // given
        // when
        // then
        assertThat(productsQuantities).isNotNull();
    }

    @DisplayName("가격 비교")
    @Test
    void priceEqual() {
        // given

        // when
        Price totalPrice = productsQuantities.totalPrice();

        // then
        assertThat(totalPrice).isEqualTo(new Price(1000));
    }

    @DisplayName("메뉴 상품 생성")
    @Test
    void toResponse() {
        // given

        // when
        List<MenuProduct> menuProducts = productsQuantities.toMenuProduct();

        // then
        assertThat(menuProducts.size()).isEqualTo(1);
    }
}