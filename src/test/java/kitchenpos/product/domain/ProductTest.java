package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductTest {


    @Test
    @DisplayName("상품 생성 시 상품의 가격이 0원 미만이면 예외가 발생한다.")
    void validatePrice() {
        // given
        String name = "순대국밥";
        BigDecimal price = BigDecimal.valueOf(-1);

        // expect
        assertThatThrownBy(() -> 상품_생성(null, name, price))
                .isInstanceOf(IllegalArgumentException.class);
    }

    public static Product 상품_생성(Long id, String name, BigDecimal price) {
        return new Product.Builder()
                .id(id)
                .name(name)
                .price(price)
                .build();
    }
}