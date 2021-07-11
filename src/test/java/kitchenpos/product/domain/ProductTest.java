package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.common.domain.Price;
import kitchenpos.common.exception.PriceEmptyException;
import kitchenpos.common.exception.PriceNegativeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("상품 테스트")
public class ProductTest {

    @DisplayName("상품의 가격은 필수 입력항목이다.")
    @Test
    void price_null() {
        assertThatThrownBy(() -> new Product("상품명", null))
            .isInstanceOf(PriceEmptyException.class);
    }

    @DisplayName("상품의 가격은 음수가 될수 없다.")
    @Test
    void price_negative() {
        assertThatThrownBy(() -> new Product("상품명", Price.wonOf(-1)))
            .isInstanceOf(PriceNegativeException.class);
    }

}
