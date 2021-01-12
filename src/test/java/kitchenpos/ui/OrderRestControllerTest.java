package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;


    @DisplayName("주문을 등록할 수 있다.")
    @Test
    void createOrder() throws Exception {
        List<OrderLineItem> orderLineItems = Arrays.asList(
                new OrderLineItem(1L, 1),
                new OrderLineItem(2L, 1)
        );
        Order order = new Order(1L, orderLineItems);

        when(orderService.create(any())).thenReturn(order);

        mockMvc.perform(post("/api/orders")
                .content(objectMapper.writeValueAsString(order)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("주문 목록을 가져올 수 있다.")
    @Test
    void findAllOrder() throws Exception {
        when(orderService.list()).thenReturn(Arrays.asList(new Order(1L), new Order(2L)));
        mockMvc.perform(get("/api/orders"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @DisplayName("주문의 상태값을 나타낼 수 있다.")
    @Test
    void changeOrderStatus() throws Exception {
        when(orderService.changeOrderStatus(anyLong(), any(Order.class))).thenReturn(new Order(1L));
        Order order = new Order(OrderStatus.MEAL.name());

        mockMvc.perform(put("/api/orders/{orderId}/order-status", 1L)
                .content(objectMapper.writeValueAsString(order))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }
}