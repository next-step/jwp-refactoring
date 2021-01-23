package kitchenpos.order.dto;

import kitchenpos.ValidateBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 메뉴 요청")
public class OrderMenuRequestTest extends ValidateBase {
    @DisplayName("수량이 1보다 작은 경우")
    @Test
    public void minimumQuantity() {
        OrderMenuRequest request = new OrderMenuRequest(1L, 0);

        validate(request);
    }
}
