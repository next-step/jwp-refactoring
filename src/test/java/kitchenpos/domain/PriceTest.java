package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;

class PriceTest {
    @Test
    @DisplayName("가격이 비어 있거나, 0원보다 적을경우 IllegalArgumentException이 발생한다")
    void 가격이_비어_있거나_0원보다_적을경우_IllegalArgumentException이_발생한다() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Price(null));
        assertThatIllegalArgumentException().isThrownBy(() -> new Price(new BigDecimal(-1)));
    }
}