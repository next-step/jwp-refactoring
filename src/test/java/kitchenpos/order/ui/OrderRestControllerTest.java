package kitchenpos.order.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.order.applicatioin.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderRestController.class)
public class OrderRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    private OrderLineItem orderLineItem;
    private Order order;
    private OrderResponse orderResponse;
    @BeforeEach
    void setUp() {
        orderLineItem = new OrderLineItem(1L,1L);
        order = new Order(Arrays.asList(orderLineItem));
    }

    @Test
    @DisplayName("주문 생성 확인")
    public void whenPostOrder_thenReturnStatus() throws Exception {
        when(orderService.create(any())).thenReturn(orderResponse.of(order));

        mockMvc.perform(post("/api/orders")
                .content(asJsonString(order))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("주문 생성 조회")
    public void givenOrder_whenGetOrders_thenReturnStatus() throws Exception{
        List<OrderResponse> allOrders = Arrays.asList(OrderResponse.of(order));

        given(orderService.list()).willReturn(allOrders);

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("생성된 주문 수정")
    public void givenOrder_whenPutOrder_thenReturnSatus() throws Exception {
        List<OrderResponse> allOrders = Arrays.asList(OrderResponse.of(order));

        given(orderService.list()).willReturn(allOrders);

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andDo(print());

        order.changeOrderStatus(OrderStatus.MEAL.name());
        mockMvc.perform(put("/api/orders/{orderId}/order-status", order.getId())
                .content(asJsonString(order))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        order.changeOrderStatus(OrderStatus.COMPLETION.name());
        mockMvc.perform(put("/api/orders/{orderId}/order-status", order.getId())
                .content(asJsonString(order))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
