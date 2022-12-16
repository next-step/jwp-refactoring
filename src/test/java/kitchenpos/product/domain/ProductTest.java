package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import kitchenpos.common.exception.InvalidParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ProductTest {
    @Test
    @DisplayName("상품 생성")
    void createProduct() {
        Product actual = Product.of("짬뽕", BigDecimal.valueOf(9000L));
        
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isInstanceOf(Product.class)        
        );
    }
    
    @Test
    @DisplayName("상품명 필수")
    void createProductByNameNull() {
        assertThatThrownBy(() -> Product.of(null, BigDecimal.valueOf(9000L)))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("이름은 필수입니다.");
    }

    @Test
    @DisplayName("가격 필수")
    void createProductByPriceNull() {
        assertThatThrownBy(() -> Product.of("짬뽕", null))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("가격은 필수입니다.");
    }

    @ParameterizedTest(name = "[{index}] 가격 음수 불가능")
    @ValueSource(longs = {-1, Long.MIN_VALUE})
    void createProductByPriceNull(long price) {
        assertThatThrownBy(() -> Product.of("짬뽕", BigDecimal.valueOf(price)))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("가격은 0원 이상이어야 합니다.");
    }
}
