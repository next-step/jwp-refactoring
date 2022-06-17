package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Product 클래스 테스트")
class ProductEntityTest {

    @DisplayName("Product를 생성한다.")
    @Test
    void successfulCreate() {
        ProductEntity product = new ProductEntity("강정치킨", BigDecimal.TEN);
        assertThat(product).isNotNull();
    }

    @DisplayName("이름없이 Product를 생성한다.")
    @Test
    void failureCreateWithEmptyName() {
        assertThatThrownBy(() -> {
            new ProductEntity(null, BigDecimal.TEN);
        }).isInstanceOf(NullPointerException.class);
    }

    @DisplayName("가격이 -1인 Product를 생성한다.")
    @Test
    void failureCreateWithNegativePrice() {
        assertThatThrownBy(() -> {
            new ProductEntity("강정치킨", BigDecimal.valueOf(-1));
        }).isInstanceOf(InvalidPriceException.class);
    }
}