package kitchenpos.product.domain;

import kitchenpos.common.exception.InputDataErrorCode;
import kitchenpos.common.exception.InputDataException;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("상품 단위 테스트")
class ProductTest {

    @Test
    @DisplayName("상품을 등록한다.")
    void createProductTest() {
        Product product = new Product("대파치킨", new BigDecimal("10000"));
        checkValidProduct(product, "대파치킨", new BigDecimal("10000"));
    }

    @Test
    @DisplayName("잘못된 상품 가격을 음수로 입력하면 예외처리")
    void createWrongMinusPriceProductTest() {
        assertThatThrownBy(() -> {
            new Product("대파치킨", new BigDecimal(-10000));
        }).isInstanceOf(InputDataException.class)
                .hasMessageContaining(InputDataErrorCode.THE_PRICE_CAN_NOT_INPUT_LESS_THAN_ZERO.errorMessage());
    }

    @Test
    @DisplayName("상품 가격을 입력하지 않으면 예외처리")
    void createNotInputPriceProductTest() {
        assertThatThrownBy(() -> {
            new Product("대파치킨", null);
        }).isInstanceOf(InputDataException.class)
                .hasMessageContaining(InputDataErrorCode.THE_PRICE_MUST_INPUT.errorMessage());
    }

    private void checkValidProduct(Product product, String name, BigDecimal price) {
        assertThat(product.getName()).isEqualTo(name);
        assertThat(product.getAmount()).isEqualTo(price);
    }

}
