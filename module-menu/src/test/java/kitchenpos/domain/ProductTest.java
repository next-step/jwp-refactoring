package kitchenpos.domain;

import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProductTest {
    @DisplayName("입력받은 메뉴의 가격이 음수인지 확인")
    @Test
    void 입력받은_메뉴의_가격이_음수인지_확인() {
        Product inputProduct = new Product("후라이드치킨", BigDecimal.valueOf(-18000L));

        assertThatThrownBy(inputProduct::validatePrice).isInstanceOf(IllegalArgumentException.class);
    }
}
