package kitchenpos.unit;

import kitchenpos.domain.Price;
import kitchenpos.domain.menu.Quantity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("값 타입 실패 테스트")
public class VoTest {
    @DisplayName("가격 값 타입 생성 실패")
    @Test
    void priceFail() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Price(-1));
    }

    @DisplayName("수량 값 타입 생성 실패")
    @Test
    void quantityFail() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Quantity(-1));
    }
}
