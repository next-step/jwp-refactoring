package kitchenpos.product;

import kitchenpos.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProductTest {

    @DisplayName("상품 생성 불가능 케이스 1 - 금액이 0원 이하인 경우")
    @Test
    public void invalidCreate() {
        Product product = new Product("이름", new BigDecimal(-1000));
        assertThatThrownBy(product::checkValidation).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 생성 불가능 케이스 2 - 금액이 올바르지 않은 경우")
    @Test
    public void invalidCreate2() {
        Product product = new Product();
        assertThatThrownBy(product::checkValidation).isInstanceOf(IllegalArgumentException.class);
    }
}
