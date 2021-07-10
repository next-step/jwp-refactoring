package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProductTest {
    @DisplayName("상품 등록 예외 - 가격이 입력되지 않은 경우")
    @Test
    public void 가격입력되지않은경우_상품등록_예외() throws Exception {
        //when
        //then
        assertThatThrownBy(() -> new Product(1L, "상품이름", null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 등록 예외 - 가격이 음수인 경우")
    @Test
    public void 가격이음수인경우_상품등록_예외() throws Exception {
        //when
        //then
        assertThatThrownBy(() -> new Product(1L, "상품이름", BigDecimal.valueOf(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
