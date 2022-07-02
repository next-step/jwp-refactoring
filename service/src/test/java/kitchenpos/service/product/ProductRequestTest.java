package kitchenpos.service.product;

import kitchenpos.domain.menu.InvalidNameException;
import kitchenpos.domain.menu.InvalidPriceException;
import kitchenpos.service.product.dto.ProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductRequestTest {
    @Test
    @DisplayName("상품 가격이 음수면 생성할 수 없다.")
    void create_fail_price() {
        assertThatThrownBy(() -> new ProductRequest("product", -1)).isExactlyInstanceOf(InvalidPriceException.class);
    }
    @Test
    @DisplayName("상품 이름이 비어있으면 생성할 수 없다.")
    void create_fail_name() {
        assertThatThrownBy(() -> new ProductRequest(null, 1000)).isExactlyInstanceOf(InvalidNameException.class);
    }

}
