package kitchenpos.ui.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.ui.OrderRestController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private OrderService orderService;

    @DisplayName("주문 등록")
    @Test
    public void create() throws Exception {
        OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1);
        Order order = new Order(1L, OrderStatus.COOKING.name(), LocalDateTime.now(), Arrays.asList(orderLineItem));
        given(orderService.create(any())).willReturn(order);
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJsonString(order)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("주문 리스트")
    @Test
    public void list() throws Exception {
        OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1);
        Order order1 = new Order(1L, OrderStatus.COOKING.name(), LocalDateTime.now(), Arrays.asList(orderLineItem));
        Order order2 = new Order(2L, OrderStatus.COOKING.name(), LocalDateTime.now(), Arrays.asList(orderLineItem));
        given(orderService.list()).willReturn(Arrays.asList(order1, order2));
        mockMvc.perform(get("/api/orders"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].orderTableId").value(1L))
                .andExpect(jsonPath("$.[1].orderTableId").value(2L));
    }

    @DisplayName("주문 상태 수정")
    @Test
    public void updateStatus() throws Exception {
        OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1);
        Order changedOrder = new Order(1L,1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), Arrays.asList(orderLineItem));

        given(orderService.changeOrderStatus(1L, changedOrder)).willReturn(changedOrder);
        mockMvc.perform(put("/api/orders/{orderId}/order-status", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJsonString(changedOrder)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private String makeJsonString(Object request) throws JsonProcessingException {
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(request);
    }
}