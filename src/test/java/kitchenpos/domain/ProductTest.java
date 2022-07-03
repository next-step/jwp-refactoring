package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductTest {
    Product 스낵랩;

    @BeforeEach
    void setUp() {
        스낵랩 = new Product(1L, "스낵랩", BigDecimal.valueOf(3000));
    }

    @Test
    @DisplayName("상품 등록 시 가격 세팅 안할 시 0 에러 확인")
    public void saveProductPriceNull() {
        assertThatThrownBy(() -> new Product("상품명", null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 등록 시 가격 0미만 예외처리")
    public void saveProductPriceZero() {
        assertThatThrownBy(() -> 스낵랩.setPrice(BigDecimal.valueOf(-1))).isInstanceOf(IllegalArgumentException.class);
    }
}
