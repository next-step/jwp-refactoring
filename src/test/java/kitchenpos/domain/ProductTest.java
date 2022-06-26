package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProductTest {

    @DisplayName("상품을 생성한다.")
    @Test
    void 생성() {
        // when
        ProductEntity 샐러드 = new ProductEntity("샐러드", BigDecimal.valueOf(100));

        // then
        assertThat(샐러드).isNotNull();
        assertThat(샐러드.getName()).isEqualTo("샐러드");
        assertThat(샐러드.getUnitPrice()).isEqualTo(BigDecimal.valueOf(100));
    }

    @DisplayName("상품 가격이 0보다 작아서 생성에 실패한다.")
    @Test
    void 생성_예외() {
        // when, then
        assertThatThrownBy(() -> new ProductEntity("샐러드", BigDecimal.ZERO.subtract(BigDecimal.ONE)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
