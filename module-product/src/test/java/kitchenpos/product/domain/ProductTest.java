package kitchenpos.product.domain;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ExceptionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("상품에 대한 단위 테스트")
class ProductTest {

    @DisplayName("상품에 이름과 가격을 전달하면 정상적으로 생성된다")
    @Test
    void create_test() {
        // given
        String name = "test_product";
        BigDecimal price = BigDecimal.valueOf(5000L);

        // when
        Product result = Product.of(name, price);

        // then
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getPrice()).isEqualTo(price);
    }

    @DisplayName("상품에 이름을 null 로 전달하면 예외가 발생한다")
    @Test
    void create_exception_test() {
        // given
        String name = null;
        BigDecimal price = BigDecimal.valueOf(5000L);

        // then
        assertThatThrownBy(() -> {
            Product.of(name, price);
        }).isInstanceOf(BadRequestException.class)
                .hasMessageContaining(ExceptionType.INVALID_NAME.getMessage());
    }

    @DisplayName("상품에 가격을 null 로 전달하면 예외가 발생한다")
    @Test
    void create_exception_test2() {
        // given
        String name = "test_product";
        BigDecimal price = null;

        // then
        assertThatThrownBy(() -> {
            Product.of(name, price);
        }).isInstanceOf(BadRequestException.class)
                .hasMessageContaining(ExceptionType.INVALID_PRICE.getMessage());
    }

    @DisplayName("상품에 가격을 음수로 전달하면 예외가 발생한다")
    @Test
    void create_exception_test3() {
        // given
        String name = "test_product";
        BigDecimal price = BigDecimal.valueOf(-3000L);

        // then
        assertThatThrownBy(() -> {
            Product.of(name, price);
        }).isInstanceOf(BadRequestException.class)
                .hasMessageContaining(ExceptionType.INVALID_PRICE.getMessage());
    }
}
