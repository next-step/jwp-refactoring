package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class ProductTest {
    @Test
    void 상품_생성_가격_없는_경우_예외() {
        assertThatThrownBy(
                () -> new Product(ProductName.from("토마토"), null)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
