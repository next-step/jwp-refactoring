package kitchenpos.product.domain;

import kitchenpos.product.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("가격 테스트")
public class PriceTest {

    public static final BigDecimal MINUS_PRICE = BigDecimal.valueOf(-1);
    public static final BigDecimal EXPENSIVE_PRICE = BigDecimal.valueOf(9999999);

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        //given:
        final BigDecimal price = BigDecimal.ONE;
        //when, then:
        assertThat(Price.from(price)).isEqualTo(Price.from(price));
    }

    @DisplayName("생성 예외 - 가격이 0보다 작은 경우")
    @Test
    void 생성_예외_가격이_0보다_작은_경우() {
        //given:
        final BigDecimal price = MINUS_PRICE;
        //when, then:
        assertThatIllegalArgumentException().isThrownBy(() -> Price.from(price));
    }

    @DisplayName("moreThan 메서드 테스트 성공")
    @Test
    void moreThan_메서드_성공() {
        //given:
        final Price source = Price.from(EXPENSIVE_PRICE);
        final Price target = Price.from(BigDecimal.ZERO);
        //when, then:
        assertThat(source.moreThan(target)).isTrue();
    }

    @DisplayName("multiply 메서드 테스트 성공")
    @Test
    void multiply_메서드_성공() {
        //given:
        final Price source = Price.from(BigDecimal.valueOf(2));
        final BigDecimal target = BigDecimal.valueOf(3);
        //when, then:
        assertThat(source.multiply(target)).isEqualTo(BigDecimal.valueOf(6));
    }
}
