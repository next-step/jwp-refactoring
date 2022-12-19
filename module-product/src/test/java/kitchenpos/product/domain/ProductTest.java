package kitchenpos.product.domain;

import kitchenpos.core.exception.InvalidPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Product 클래스 테스트")
class ProductTest {

    @DisplayName("Product를 생성한다.")
    @Test
    void successfulCreate() {
        Product product = new Product("강정치킨", BigDecimal.TEN);
        assertThat(product).isNotNull();
    }

    @DisplayName("이름없이 Product를 생성한다.")
    @Test
    void failureCreateWithEmptyName() {
        assertThatThrownBy(() -> {
            new Product(null, BigDecimal.TEN);
        }).isInstanceOf(NullPointerException.class);
    }

    @DisplayName("가격이 -1인 Product를 생성한다.")
    @Test
    void failureCreateWithNegativePrice() {
        assertThatThrownBy(() -> {
            new Product("강정치킨", BigDecimal.valueOf(-1));
        }).isInstanceOf(InvalidPriceException.class);
    }
}
