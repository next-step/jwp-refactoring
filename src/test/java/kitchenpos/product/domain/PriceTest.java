package kitchenpos.product.domain;

import kitchenpos.exception.IllegalPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("가격 원시값 포장 객체 테스트")
public class PriceTest {

    @Test
    void 가격_객체_생성() {
        Price price = new Price(new BigDecimal(7000));
        assertThat(price).isEqualTo(new Price(7000));
    }

    @ParameterizedTest
    @NullSource
    void 가격_객체에_null_입력_시_에러_발생(BigDecimal price) {
        assertThatThrownBy(() -> new Price(price)).isInstanceOf(IllegalPriceException.class);
    }

    @Test
    void 가격_객체에_음수값_입력_시_에러_발생() {
        assertThatThrownBy(() -> new Price(new BigDecimal(-1000))).isInstanceOf(IllegalPriceException.class);
        assertThatThrownBy(() -> new Price(-1000)).isInstanceOf(IllegalPriceException.class);
    }
}
