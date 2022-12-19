package kitchenpos.product.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static kitchenpos.product.domain.NameTest.EMPTY_NAME;
import static kitchenpos.product.domain.PriceTest.MINUS_PRICE;

@DisplayName("상품 테스트")
public class ProductTest {

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        //given:
        String name = "상품";
        BigDecimal price = BigDecimal.ONE;
        //when, then:
        assertThat(상품(name, price)).isEqualTo(상품(name, price));
    }

    @DisplayName("생성 예외 - 이름이 빈 값인 경우")
    @Test
    void 생성_예외_이름이_빈_값인_경우() {
        //given:
        String name = EMPTY_NAME;
        BigDecimal price = BigDecimal.ONE;
        //when, then:
        assertThatIllegalArgumentException().isThrownBy(() -> 상품(name, price));
    }

    @DisplayName("생성 예외 - 가격이 0보다 작은 경우")
    @Test
    void 생성_예외_가격이_0보다_작은_경우() {
        //given:
        String name = "상품";
        BigDecimal price = MINUS_PRICE;
        //when, then:
        assertThatIllegalArgumentException().isThrownBy(() -> 상품(name, price));
    }

    public static Product 상품(String name) {
        return 상품(name, BigDecimal.ONE);
    }

    public static Product 상품(String name, BigDecimal price) {
        return Product.of(ProductName.from(name), Price.from(price));
    }

    public static Product 상품_통다리() {
        return 상품("통다리", BigDecimal.ONE);
    }

    public static Product 상품_콜라() {
        return 상품("콜라", BigDecimal.ONE);
    }
}
