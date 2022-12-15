package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static kitchenpos.domain.NameTest.EMPTY_NAME;
import static kitchenpos.domain.PriceTest.MINUS_PRICE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("상품 테스트")
public class ProductTest {

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        String name = "상품";
        BigDecimal price = BigDecimal.ONE;
        assertThat(Product.of(name, price)).isEqualTo(Product.of(name, price));
    }

    @DisplayName("생성 예외 - 이름이 빈 값인 경우")
    @Test
    void 생성_예외_이름이_빈_값인_경우() {
        String name = EMPTY_NAME;
        BigDecimal price = BigDecimal.ONE;
        assertThatIllegalArgumentException().isThrownBy(() -> Product.of(name, price));
    }

    @DisplayName("생성 예외 - 가격이 0보다 작은 경우")
    @Test
    void 생성_예외_가격이_0보다_작은_경우() {
        String name = "상품";
        BigDecimal price = MINUS_PRICE;
        assertThatIllegalArgumentException().isThrownBy(() -> Product.of(name, price));
    }

    public static Product 상품(String name) {
        return 상품(name, BigDecimal.ONE);
    }
    public static Product 상품(String name, BigDecimal price) {
        return Product.of(name, price);
    }
}
