package kitchenpos.product.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.product.dto.ProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ProductTest {

    @Test
    @DisplayName("Product 인스턴스를 생성한다")
    void of() {
        // given
        Name name = new Name("진순이");
        Price price = new Price(1_000);
        ProductRequest 진순이 = new ProductRequest(name.value(), price.value());

        // when
        Product product = Product.of(진순이);

        // then
        assertAll(
                () -> assertThat(product.getName()).isEqualTo(name),
                () -> assertThat(product.getPrice()).isEqualTo(price)
        );
    }
}
