package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @DisplayName("주문을 등록한다.")
    @Test
    void create() throws Exception {
        Order order = new Order(1L, 1L, OrderStatus.COOKING.name());

        when(orderService.create(any())).thenReturn(order);

        mockMvc.perform(post("/api/orders")
            .content(objectMapper.writeValueAsString(order)).contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() throws Exception {
        Order order1 = new Order(1L, 1L, OrderStatus.COOKING.name());
        Order order2 = new Order(2L, 1L, OrderStatus.COOKING.name());

        when(orderService.list()).thenReturn(Arrays.asList(order1, order2));
        mockMvc.perform(get("/api/orders"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)));
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() throws Exception {
        Order order1 = new Order(1L, 1L, OrderStatus.COOKING.name());

        when(orderService.changeOrderStatus(anyLong(), any(Order.class))).thenReturn(order1);
        Order order2 = new Order(order1.getId(), 1L, OrderStatus.MEAL.name());

        mockMvc.perform(put("/api/orders/{orderId}/order-status", order1.getId())
            .content(objectMapper.writeValueAsString(order2))
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());
    }

}
