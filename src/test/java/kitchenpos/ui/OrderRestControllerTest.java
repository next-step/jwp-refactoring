package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.UTF8MockMvcTest;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@UTF8MockMvcTest(controllers = {OrderRestController.class})
class OrderRestControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private OrderService orderService;

  @DisplayName("새로운 주문을 저장한다.")
  @Test
  void createTest() throws Exception {
    //given
    Order createdOrder = new Order(1L,
                          1L,
                                        OrderStatus.COOKING.name(),
                                        LocalDateTime.of(2021,6,29,3,45),
                                        Arrays.asList(new OrderLineItem(1L, 1L, 1L, 2)));
    when(orderService.create(any())).thenReturn(createdOrder);
    Order createRequestBody = new Order(1L, Arrays.asList(new OrderLineItem(1L, 2)));
    String requestBody = objectMapper.writeValueAsString(createRequestBody);

    //when & then
    mockMvc
        .perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(requestBody))
        .andExpect(status().isCreated());
  }

  @DisplayName("주문 목록을 조회한다.")
  @Test
  void listTest() throws Exception {
    Order createdOrder1 = new Order(1L,
        1L,
        OrderStatus.COOKING.name(),
        LocalDateTime.of(2021,6,29,3,45),
        Arrays.asList(new OrderLineItem(1L, 1L, 1L, 2)));
    Order createdOrder2 = new Order(2L,
        2L,
        OrderStatus.COOKING.name(),
        LocalDateTime.of(2021,6,29,3,45),
        Arrays.asList(new OrderLineItem(2L, 2L, 2L, 1)));
    when(orderService.list()).thenReturn(Arrays.asList(createdOrder1, createdOrder2));
    mockMvc
        .perform(get("/api/orders"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("\"id\":1")))
        .andExpect(content().string(containsString("\"id\":2")));
  }

  @DisplayName("주문의 현재 상태를 변경할 수 있다.")
  @Test
  void changeOrderStatusTest() throws Exception {
    //given
    Order createdOrder = new Order(1L,
        1L,
        OrderStatus.MEAL.name(),
        LocalDateTime.of(2021,6,29,3,45),
        Arrays.asList(new OrderLineItem(1L, 1L, 1L, 2)));
    when(orderService.changeOrderStatus(any(), any())).thenReturn(createdOrder);
    Order createRequestBody = new Order();
    createRequestBody.setOrderStatus(OrderStatus.MEAL.name());
    String requestBody = objectMapper.writeValueAsString(createRequestBody);

    //when & then
    mockMvc
        .perform(put("/api/orders/{orderId}/order-status", createdOrder.getId())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("MEAL")));
  }


}
