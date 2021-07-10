package kitchenpos.Product.domain;

import kitchenpos.common.Name;
import kitchenpos.common.Price;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Product 도메인 테스트")
public class ProductTest {
    private Product product;

    @BeforeEach
    void setUp() {
        product = 상품_생성("뿌링클", new BigDecimal(18000));
    }

    @Test
    @DisplayName("상품 등록 테스트")
    void create() {
        상품_생성됨(product);
    }

    @Test
    @DisplayName("상품 가격 예외 테스트")
    void priceException() {
        BigDecimal price = new BigDecimal(-1);

        assertThatThrownBy(() -> {
            상품_생성("뿌링클", price);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    public static Product 상품_생성(String 뿌링클, BigDecimal price) {
        return new Product(new Name("뿌링클"), new Price(price));
    }

    private void 상품_생성됨(Product product) {
        assertThat(product.getName()).isEqualTo(product.getName());
        assertThat(product.getPrice()).isEqualTo(product.getPrice());
    }
}
