package kitchenpos.product.domain;

import static kitchenpos.fixture.ProductFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.Exception.ProductPriceEmptyException;
import kitchenpos.common.Price;
import kitchenpos.common.Quantity;
import org.junit.jupiter.api.Test;

class ProductTest {
    @Test
    void 상품_생성_가격_없는_경우_예외() {
        assertThatThrownBy(
                () -> new Product(ProductName.from("토마토"), null)
        ).isInstanceOf(ProductPriceEmptyException.class);
    }

    @Test
    void 수량_만큼_가격() {
        Product 토마토 = createProduct(1L, "토마토", 1000);
        assertThat(토마토.priceByQuantity(Quantity.from(10))).isEqualTo(Price.from(10000));
    }
}
