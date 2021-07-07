package kitchenpos.order.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.UTF8MockMvcTest;
import kitchenpos.order.application.OrderService2;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@UTF8MockMvcTest(controllers = {OrderRestController2.class})
class OrderRestController2Test {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private OrderService2 orderService;

  @DisplayName("새로운 주문을 저장한다.")
  @Test
  void createTest() throws Exception {
    //given
    OrderResponse createdOrder = new OrderResponse(1L,
        1L,
        OrderStatus.COOKING.name(),
        LocalDateTime.of(2021,6,29,3,45),
        Collections.singletonList(new OrderResponse.OrderLineItemResponse(1L, 1L, 1L, 2)));
    when(orderService.create(any())).thenReturn(createdOrder);
    OrderRequest createRequestBody = new OrderRequest(1L, Collections.singletonList(new OrderRequest.OrderLineItemRequest(1L, 2L)));
    String requestBody = objectMapper.writeValueAsString(createRequestBody);

    //when & then
    mockMvc
        .perform(post("/api/v2/orders")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(requestBody))
        .andExpect(status().isCreated());
  }

  @DisplayName("주문 목록을 조회한다.")
  @Test
  void listTest() throws Exception {
    //given
    OrderResponse createdOrder1 = new OrderResponse(1L,
        1L,
        OrderStatus.COOKING.name(),
        LocalDateTime.of(2021,6,29,3,45),
        Collections.singletonList(new OrderResponse.OrderLineItemResponse(1L, 1L, 1L, 2)));
    OrderResponse createdOrder2 = new OrderResponse(2L,
        2L,
        OrderStatus.COOKING.name(),
        LocalDateTime.of(2021,6,29,3,45),
        Collections.singletonList(new OrderResponse.OrderLineItemResponse(2L, 2L, 2L, 1)));
    when(orderService.findAllOrders()).thenReturn(Arrays.asList(createdOrder1, createdOrder2));

    //when & then
    mockMvc
        .perform(get("/api/v2/orders"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("\"id\":1")))
        .andExpect(content().string(containsString("\"id\":2")));
  }

  @DisplayName("주문의 현재 상태를 변경할 수 있다.")
  @Test
  void changeOrderStatusTest() throws Exception {
    //given
    OrderResponse createdOrder = new OrderResponse(1L,
        1L,
        OrderStatus.MEAL.name(),
        LocalDateTime.of(2021,6,29,3,45),
        Collections.singletonList(new OrderResponse.OrderLineItemResponse(1L, 1L, 1L, 2)));
    when(orderService.changeOrderStatus(any(), any())).thenReturn(createdOrder);
    OrderStatusRequest createRequestBody = new OrderStatusRequest(OrderStatus.MEAL.name());
    String requestBody = objectMapper.writeValueAsString(createRequestBody);

    //when & then
    mockMvc
        .perform(put("/api/v2/orders/{orderId}/order-status", createdOrder.getId())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("MEAL")));
  }

}
