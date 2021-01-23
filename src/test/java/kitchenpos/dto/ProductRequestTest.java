package kitchenpos.dto;

import kitchenpos.product.dto.ProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("제품 요청")
public class ProductRequestTest extends ValidateBase {
    @DisplayName("가격이 음수인 경우")
    @Test
    public void validatePrice() {
        ProductRequest request = new ProductRequest("", -1);

        validate(request);
    }
}
