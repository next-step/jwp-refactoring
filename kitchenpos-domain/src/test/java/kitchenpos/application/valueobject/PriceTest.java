package kitchenpos.application.valueobject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("가격 테스트")
class PriceTest {
    private Price price;
    private Price otherPrice;

    @BeforeEach
    void setUp() {
        price = Price.of(1);
        otherPrice = Price.of(10);
    }

    @Test
    @DisplayName("가격이 올바르지 않으면 생성할 수 없다. : 0원 이상이어야 한다.")
    public void createFail() {
        // when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Price.of(-1))
                .withMessageMatching("가격은 0 원 이상이어야 합니다.");
    }

    @Test
    @DisplayName("덧셈 처리 한다.")
    public void add() {
        // given
        Price expected = Price.of(11);

        // when
        Price actual = price.add(otherPrice);

        // then
        assertThat(expected).isEqualTo(actual);
    }

    @Test
    @DisplayName("뺄셈 처리 한다.")
    public void subtract() {
        // given
        Price expected = Price.of(9);

        // when
        Price actual = otherPrice.subtract(price);

        // then
        assertThat(expected).isEqualTo(actual);
    }

    @Test
    @DisplayName("곱셈 처리 한다.")
    public void multiply() {
        // given
        Price expected = Price.of(10);

        // when
        Price actual = price.multiply(10);

        // then
        assertThat(expected).isEqualTo(actual);
    }
}