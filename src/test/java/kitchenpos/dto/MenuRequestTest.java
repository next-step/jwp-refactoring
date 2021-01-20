package kitchenpos.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

@DisplayName("메뉴 요청")
public class MenuRequestTest extends ValidateBase {
    @DisplayName("가격이 음수인 경우")
    @Test
    public void negativePrice() {
        MenuRequest request = new MenuRequest("샘플", -1, 1L,
                Arrays.asList(new MenuProductRequest(1, 1), new MenuProductRequest(2, 1)));

        validate(request);
    }

    @DisplayName("제품 리스트가 비어있는 경우")
    @Test
    public void emptyProductList() {
        MenuRequest request = new MenuRequest("샘플", -1, 1L,
                Collections.emptyList());

        validate(request);
    }

    @DisplayName("제품 리스트가 null인 경우")
    @Test
    public void nullProductList() {
        MenuRequest request = new MenuRequest("샘플", -1, 1L, null);

        validate(request);
    }
}
