package kitchenpos.product.domain;

import kitchenpos.common.valueobject.exception.NegativePriceException;
import kitchenpos.product.domain.exception.InvalidProductNameException;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @Test
    void create() {
        //when
        Product actual = Product.of("후라이드치킨", BigDecimal.valueOf(100L));

        //then
        assertThat(actual.getName()).isEqualTo("후라이드치킨");
        assertThat(actual.getPrice().getValue().longValue()).isEqualTo(100L);
    }

    @DisplayName("상품의 이름을 지정해야한다.")
    @Test
    void createProductExceptionIfNameIsNull() {
        //when
        assertThatThrownBy(() -> Product.of("", BigDecimal.valueOf(100L)))
                .isInstanceOf(InvalidProductNameException.class); //then
        //when
        assertThatThrownBy(() -> Product.of(null, BigDecimal.valueOf(100L)))
                .isInstanceOf(InvalidProductNameException.class); //then
    }

    @DisplayName("상품의 가격은 0원 이상이어야 한다.")
    @Test
    void createProductExceptionIfPriceIsNegative() {
        //when
        assertThatThrownBy(() -> Product.of("후라이드치킨", BigDecimal.valueOf(-1000)))
                .isInstanceOf(NegativePriceException.class); //then
    }

    @DisplayName("상품의 가격을 지정해야한다.")
    @Test
    void createProductExceptionIfPriceIsNull() {
        //when
        AssertionsForClassTypes.assertThatThrownBy(() -> Product.of("후라이드치킨", null))
                .isInstanceOf(NegativePriceException.class); //then
    }
}
