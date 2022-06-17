package kitchenpos.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("MenuProduct 클래스 테스트")
class MenuProductEntityTest {

    private ProductEntity product;

    @BeforeEach
    void setUp() {
        product = new ProductEntity("강정치킨", BigDecimal.valueOf(15_000L));
    }

    @DisplayName("MenuProduct 생성한다.")
    @Test
    void successfulCreate() {
        MenuProductEntity menuProduct = new MenuProductEntity(product, 1);
        assertThat(menuProduct).isNotNull();
    }

    @DisplayName("Product없이 MenuProduct 생성한다.")
    @Test
    void failureCreateWithEmptyProduct() {
        assertThatThrownBy(() -> {
            new MenuProductEntity(null, 1);
        }).isInstanceOf(NullPointerException.class);
    }

    @DisplayName("Product없이 MenuProduct 생성한다.")
    @Test
    void failureCreateWithNegativeQuantity() {
        assertThatThrownBy(() -> {
            new MenuProductEntity(product, -1);
        }).isInstanceOf(InvalidQuantityException.class);
    }

    @DisplayName("가격이 15_000인 Product를 2개로 MenuProduct를 생성하면 금액은 30_000이다.")
    @Test
    void calculateAmount() {
        MenuProductEntity menuProduct = new MenuProductEntity(product, 2);

        assertThat(menuProduct.calculateAmount()).isEqualTo(new Amount(BigDecimal.valueOf(30_000L)));
    }
}