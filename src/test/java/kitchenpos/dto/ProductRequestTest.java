package kitchenpos.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductRequestTest extends ValidateBase {
    @DisplayName("가격이 음수인 경우")
    @Test
    public void validatePrice() {
        ProductRequest request = ProductRequest.builder()
                .price(-1)
                .build();

       assertThat(validator.validate(request)).isNotEmpty();
    }
}
