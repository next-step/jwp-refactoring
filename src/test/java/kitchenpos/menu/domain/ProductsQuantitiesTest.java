package kitchenpos.menu.domain;

import kitchenpos.common.domian.Price;
import kitchenpos.common.domian.Quantity;
import kitchenpos.common.error.CustomException;
import kitchenpos.common.error.ErrorInfo;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("상품 수랑 컬렉션 테스트")
class ProductsQuantitiesTest {

    private ProductsQuantities productsQuantities;
    private Menu menu;
    private Products products;
    private Quantities quantities;

    @BeforeEach
    void setup() {
        Long productId = 1L;

        int requestAmount = 1000;
        long quantity = 8L;

        products = new Products(Arrays.asList(new Product(productId, "순대", new Price(requestAmount))), 1);
        quantities = new Quantities(new HashMap<Long, Quantity>() {{ put(productId, new Quantity(quantity)); }}, 1);

        Price requestPrice = new Price(BigDecimal.valueOf(requestAmount * quantity));

        MenuGroup menuGroup = new MenuGroup("국밥");
        productsQuantities = new ProductsQuantities(products, quantities, requestPrice);
        menu = Menu.of(menuGroup, "순대국", productsQuantities);
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
        assertThat(totalPrice.equals(new Price(8000))).isTrue();
    }

    @DisplayName("메뉴 상품 생성")
    @Test
    void toResponse() {
        // given

        // when
        List<MenuProduct> menuProducts = productsQuantities.toMenuProduct(menu);

        // then
        assertThat(menuProducts.size()).isEqualTo(1);
    }

    @DisplayName("생성 실패 - 요청 금액과 다름")
    @Test
    void createFailedByRequestPrice() {
        // given
        // when
        // then
        assertThatThrownBy(() -> new ProductsQuantities(products, quantities, new Price(9999)))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorInfo.TOTAL_PRICE_NOT_EQUAL_REQUEST.message());
    }
}