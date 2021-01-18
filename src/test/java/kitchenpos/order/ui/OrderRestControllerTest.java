package kitchenpos.order.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("주문 컨트롤러 테스트")
@SpringBootTest
@AutoConfigureMockMvc
@Sql("/db/test_data.sql")
class OrderRestControllerTest {
    public static final String DEFAULT_ORDERS_URI = "/api/orders/";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderService orderService;

    private OrderRequest orderRequest;
    private List<OrderLineItemRequest> orderLineItemRequestList;
    private OrderLineItemRequest orderLineItemRequest;

    @BeforeEach
    void setUp() {
        orderLineItemRequestList = new ArrayList<>();

        orderLineItemRequest = new OrderLineItemRequest();
        orderLineItemRequest.setMenuId(1L);
        orderLineItemRequest.setQuantity(2L);

        orderLineItemRequestList.add(orderLineItemRequest);

        orderRequest = new OrderRequest();
        orderRequest.setOrderStatus(OrderStatus.COOKING.name());
        orderRequest.setOrderTableId(1L);
        orderRequest.setOrderLineItems(orderLineItemRequestList);
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void 주문_생성() throws Exception {
        final String jsonTypeOrder = objectMapper.writeValueAsString(orderRequest);

        mockMvc.perform(post(DEFAULT_ORDERS_URI)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonTypeOrder))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("id").exists())
            .andExpect(jsonPath("orderTableId").value(orderRequest.getOrderTableId()))
            .andExpect(jsonPath("orderStatus").value(orderRequest.getOrderStatus()))
            .andExpect(jsonPath("orderLineItems[0].menuId").value(orderRequest.getOrderLineItems().get(0).getMenuId()))
            .andExpect(jsonPath("orderLineItems[0].quantity").value(orderRequest.getOrderLineItems().get(0).getQuantity()));
    }

    @DisplayName("주문을 조회한다.")
    @Test
    void 주문_조회() throws Exception {
        orderService.create(orderRequest);

        mockMvc.perform(get(DEFAULT_ORDERS_URI)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].orderTableId").value(orderRequest.getOrderTableId()))
            .andExpect(jsonPath("$[0].orderStatus").value(orderRequest.getOrderStatus()))
            .andExpect(jsonPath("$[0].orderLineItems[0].menuId").value(orderRequest.getOrderLineItems().get(0).getMenuId()))
            .andExpect(jsonPath("$[0].orderLineItems[0].orderId").exists())
            .andExpect(jsonPath("$[0].orderLineItems[0].quantity").value(orderRequest.getOrderLineItems().get(0).getQuantity()));
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void 주문_상태_변경() throws Exception {
        final OrderResponse orderResponse = orderService.create(orderRequest);
        orderRequest.setOrderStatus(OrderStatus.MEAL.name());

        final String jsonTypeOrder = objectMapper.writeValueAsString(orderRequest);

        final String putRequestUri = DEFAULT_ORDERS_URI + orderResponse.getId() + "/order-status";

        mockMvc.perform(put(putRequestUri)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonTypeOrder))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(orderResponse.getId()))
            .andExpect(jsonPath("orderTableId").value(orderRequest.getOrderTableId()))
            .andExpect(jsonPath("orderStatus").value(orderRequest.getOrderStatus()))
            .andExpect(jsonPath("orderLineItems[0].menuId").value(orderRequest.getOrderLineItems().get(0).getMenuId()))
            .andExpect(jsonPath("orderLineItems[0].orderId").exists())
            .andExpect(jsonPath("orderLineItems[0].quantity").value(orderRequest.getOrderLineItems().get(0).getQuantity()));
    }
}
