package kitchenpos.menu.domain;

import kitchenpos.price.domain.Amount;
import kitchenpos.price.domain.Quantity;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("MenuProduct 클래스 테스트")
class MenuProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product("강정치킨", BigDecimal.valueOf(15_000L));
    }

    @DisplayName("MenuProduct 생성한다.")
    @Test
    void successfulCreate() {
        MenuProduct menuProduct = new MenuProduct(product, new Quantity(1));
        assertThat(menuProduct).isNotNull();
    }

    @DisplayName("Product없이 MenuProduct 생성한다.")
    @Test
    void failureCreateWithEmptyProduct() {
        assertThatThrownBy(() -> {
            new MenuProduct(null, new Quantity(1));
        }).isInstanceOf(NullPointerException.class);
    }

    @DisplayName("Product없이 MenuProduct 생성한다.")
    @Test
    void failureCreateWithNegativeQuantity() {
        assertThatThrownBy(() -> {
            new MenuProduct(product, new Quantity(-1));
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격이 15_000인 Product를 2개로 MenuProduct를 생성하면 금액은 30_000이다.")
    @Test
    void calculateAmount() {
        MenuProduct menuProduct = new MenuProduct(product, new Quantity(2));

        assertThat(menuProduct.calculateAmount()).isEqualTo(new Amount(BigDecimal.valueOf(30_000L)));
    }
}
