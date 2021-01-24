package kitchenpos.order.dto;

import kitchenpos.ValidateBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

@DisplayName("주문 요청")
public class OrderRequestTest extends ValidateBase {
    @DisplayName("메뉴 리스트가 비었을 경우")
    @Test
    public void emptyMenuList() {
        OrderRequest request = new OrderRequest(1L, Collections.emptyList());

        validate(request);
    }

    @DisplayName("메뉴 리스트가 null인 경우")
    @Test
    public void nullMenuList() {
        OrderRequest request = new OrderRequest(1L, null);

        validate(request);
    }
}
