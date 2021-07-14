package kitchenpos.order.ui;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.RestControllerTest;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("주문 API")
@WebMvcTest(OrderRestController.class)
public class OrderRestControllerTest extends RestControllerTest<OrderRequest> {

    public static final String BASE_URL = "/api/orders";
    private static final OrderLineItem 첫번째_주문항목 = new OrderLineItem(1L, 1L, 1L);
    private static final List<OrderLineItem> 주문_항목_목록 = new ArrayList<>(Arrays.asList(첫번째_주문항목));
    private static final OrderTable 주문테이블 = new OrderTable(1L);
    private static final Order 주문 = new Order(1L, 주문테이블.getId(), OrderStatus.COOKING, LocalDateTime.now(), 주문_항목_목록);
    private static final List<OrderResponse> 주문_목록 = new ArrayList<>(Arrays.asList(OrderResponse.of(주문)));

    @MockBean
    private OrderService orderService;

    @DisplayName("주문을 등록한다.")
    @Test
    void create() throws Exception {
        // Given
        given(orderService.create(any())).willReturn(OrderResponse.of(주문));

        // When & Then
        post(BASE_URL, OrderRequest.of(주문))
            .andExpect(jsonPath("$.orderTableId").value(주문.getId()));
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() throws Exception {
        // Given
        given(orderService.list()).willReturn(주문_목록);

        // When & Then
        get(BASE_URL)
            .andExpect(jsonPath("$.*", hasSize(주문_목록.size())));
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() throws Exception {
        // Given
        given(orderService.changeOrderStatus(any(), any())).willReturn(OrderResponse.of(주문));

        // When & Then
        String url = BASE_URL + String.format("/%d/order-status", 주문.getId());
        OrderStatusRequest orderStatusRequest = new OrderStatusRequest(주문.getOrderStatus());
        put(url, objectMapper.writeValueAsString(orderStatusRequest))
            .andExpect(jsonPath("$.orderTableId").value(주문.getId()));
    }

}