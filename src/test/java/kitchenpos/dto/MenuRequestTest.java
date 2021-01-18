package kitchenpos.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 요청")
public class MenuRequestTest extends ValidateBase {
    @DisplayName("가격이 음수인 경우")
    @Test
    public void negativePrice() {
        MenuRequest request = MenuRequest.builder()
                .price(-1)
                .build();

        validate(request);
    }
}
