package kitchenpos.application.product;

import kitchenpos.application.valueobject.Price;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("상품 도메인 테스트")
class ProductTest {
    @Test
    @DisplayName("상품을 생성할 수 있다.")
    public void create() throws Exception {
        // given
        String name = "후라이드";
        Price price = Price.of(16000);

        // when
        Product product = new Product(name, price);

        // then
        Assertions.assertThat(product.getName()).isEqualTo(name);
        Assertions.assertThat(product.getPrice()).isEqualTo(price);
    }
}