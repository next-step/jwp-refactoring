package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceTest {

    @Test
    @DisplayName("정상 생성")
    void create() {
        // when
        BigDecimal one = BigDecimal.ONE;
        Price price = Price.from(one);

        // then
        assertThat(price).isNotNull();
        assertThat(price.value()).isEqualTo(one);
    }

    @Test
    @DisplayName("null을 파라미터로 생성하면 예외 발생")
    void create_null() {
        // expect
        assertThatThrownBy(() -> Price.from(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("최소값 미만으로 생성하면 예외 발생")
    void create_under_price() {
        // expect
        assertThatThrownBy(() -> Price.from(-1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Price는 Price와 더할 수 있다")
    void add() {
        // given
        Price one = Price.from(BigDecimal.ONE);
        Price ten = Price.from(BigDecimal.TEN);

        // when
        Price eleven = one.add(ten);

        // then
        assertThat(eleven.value()).isEqualTo(BigDecimal.valueOf(11));
    }

    @Test
    @DisplayName("Price는 Quantity의 값 만큼 곱할 수 있다")
    void multiply(){
        // given
        Price two = Price.from(BigDecimal.valueOf(2));
        Quantity three = Quantity.from(3);

        // when
        Price six = two.multiply(three);

        // then
        assertThat(six.value()).isEqualTo(BigDecimal.valueOf(6));
    }

}