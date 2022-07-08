package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("가격")
class PriceTest {
    private static final BigDecimal 가격_값 = new BigDecimal(1500);
    private static final Price 가격 = Price.from(가격_값);

    @DisplayName("가격의 BigDecimal 값을 반환한다.")
    @Test
    void 값_확인() {
        assertThat(가격.value()).isEqualTo(가격_값);
    }

    @DisplayName("다른 가격 객체와 비교하여 더 큰값을 가지고 있는지 알 수 있다.")
    @Test
    void 더_큰지_비교() {
        assertAll(() -> assertThat(가격.isGreaterThan(Price.from(500))).isTrue(),
                () -> assertThat(가격.isGreaterThan(Price.from(1500))).isFalse());
    }

    @DisplayName("long 타입의 숫자와 곱하여 그 결가 값을 가지는 가격 객체를 생성할 수 있다.")
    @Test
    void 곱하기() {
        assertThat(가격.multiply(2L)).isEqualTo(Price.from(3000));
    }

    @DisplayName("다른 가격 객체와 더하여 그 결과 값을 가지는 가격 객체를 생성할 수 있다.")
    @Test
    void 더하기() {
        assertThat(가격.add(Price.from(500))).isEqualTo(Price.from(2000));
    }

    @DisplayName("생성")
    @Nested
    class 생성 {
        @DisplayName("가격의 값을 지정하면 생성할 수 있다.")
        @Test
        void 생성_성공() {
            assertThat(가격).isNotNull();
        }

        @DisplayName("가격 값이 NULL이면 생성할 수 없습니다.")
        @Test
        void 가격이_값이_NULL() {
            assertThatThrownBy(() -> Price.from(null)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("값이 0이하 이면 생성할 수 없습니다.")
        @Test
        void 값이_0이하() {
            assertThatThrownBy(() -> Price.from(-1)).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
