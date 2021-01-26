package kitchenpos.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class MoneyTest {

    @DisplayName("Price 생성")
    @Test
    void testCreatePriceNormal() {
        // given
        long amount = 1000;

        // when
        Money money = Money.of(amount);

        // then
        assertThat(money.getAmount()).isEqualTo(BigDecimal.valueOf(amount));
    }

    @DisplayName("가격 더하기")
    @Test
    void testAddPrice() {
        // given
        Money money = Money.of(1000);

        // when
        money.add(Money.of(1000));

        // then
        assertThat(money.getAmount()).isEqualTo(BigDecimal.valueOf(2000));
    }

    @DisplayName("가격 곱셈")
    @Test
    void testMultiply() {
        // given
        Money money = Money.of(1000);

        // when
        Money newMoney = money.multiply(3);

        // then
        assertThat(newMoney.getAmount()).isEqualTo(BigDecimal.valueOf(3000));
    }

    @DisplayName("null 로 생성 시 예외")
    @Test
    void testCreatePriceWithNullObject() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Money(null));
    }

    @DisplayName("0보다 작은 값으로 생성 시 예외")
    @Test
    void testCreatePriceWithNegativeValue() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Money(new BigDecimal(-1000)));
    }
}
