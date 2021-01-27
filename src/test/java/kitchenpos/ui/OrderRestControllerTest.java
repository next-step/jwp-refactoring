package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderRestController.class)
public class OrderRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    private OrderLineItem orderLineItem = new OrderLineItem();
    private Order order = new Order();

    @BeforeEach
    void setUp() {
        orderLineItem.setSeq(1L);
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1L);

        order.setId(1L);
        order.setOrderLineItems(Arrays.asList(orderLineItem));
    }

    @Test
    @DisplayName("주문 생성 확인")
    public void whenPostOrder_thenReturnStatus() throws Exception {
        when(orderService.create(any())).thenReturn(order);

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
        List<Order> allOrders = Arrays.asList(order);

        given(orderService.list()).willReturn(allOrders);

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("생성된 주문 수정")
    public void givenOrder_whenPutOrder_thenReturnSatus() throws Exception {
        List<Order> allOrders = Arrays.asList(order);

        given(orderService.list()).willReturn(allOrders);

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andDo(print());

        order.setOrderStatus(OrderStatus.MEAL.name());
        mockMvc.perform(put("/api/orders/{orderId}/order-status", order.getId())
                .content(asJsonString(order))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        order.setOrderStatus(OrderStatus.COMPLETION.name());
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
