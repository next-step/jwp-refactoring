package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.domian.Price;
import kitchenpos.common.domian.Quantity;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.product.domain.Product;

@DisplayName("메뉴 상품 테스트케이스")
class MenuProductsTest {

    private ProductsQuantities productsQuantities;
    private Menu menu;

    @BeforeEach
    void setup() {
        Long productId = 1L;

        int requestAmount = 1000;
        long quantity = 8L;

        List<Product> products = Arrays.asList(new Product(productId, "순대", new Price(requestAmount)));
        Map<Long, Quantity> quantities = new HashMap<Long, Quantity>() {{ put(productId, new Quantity(quantity)); }};

        Price requestPrice = new Price(BigDecimal.valueOf(requestAmount * quantity));

        MenuGroup menuGroup = new MenuGroup("국밥");
        productsQuantities = new ProductsQuantities(new Products(products, products.size()), new Quantities(quantities, quantities.size()), requestPrice);
        menu = Menu.of(menuGroup, "순대국", productsQuantities);
    }

    @DisplayName("생성")
    @Test
    void create() {
        // given
        // when
        MenuProducts menuProducts = new MenuProducts(menu, productsQuantities);
        // then
        assertThat(menuProducts).isNotNull();
    }

    @DisplayName("응답 생성")
    @Test
    void toResponse() {
        // given
        MenuProducts menuProducts = new MenuProducts(menu, productsQuantities);
        // when
        List<MenuProductResponse> menuProductResponses = menuProducts.toResponse();
        // then
        assertThat(menuProductResponses.size()).isEqualTo(1);
        assertThat(menuProductResponses.get(0).getProductResponse().getName()).isEqualTo("순대");
    }
}