package kitchenpos.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

@DisplayName("주문 요청")
public class OrderRequestTest extends ValidateBase {
    @DisplayName("메뉴 리스트가 비었을 경우")
    @Test
    public void emptyMenuList() {
        OrderRequest request = OrderRequest.builder()
                .orderTableId(1L)
                .orderMenuRequests(Collections.emptyList())
                .build();

        validate(request);
    }

    @DisplayName("메뉴 리스트가 null인 경우")
    @Test
    public void nullMenuList() {
        OrderRequest request = OrderRequest.builder()
                .orderTableId(1L)
                .orderMenuRequests(null)
                .build();

        validate(request);
    }
}
