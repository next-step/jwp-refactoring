package kitchenpos.acceptance;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import kitchenpos.application.OrderService;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderLineItemResponse;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderAcceptanceTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private OrderService orderService;

    @DisplayName("주문 등록")
    @Test
    public void createOrder() throws Exception {
        OrderLineItemResponse orderLineItem = new OrderLineItemResponse(1L, 1L, 1L, 2);
        OrderResponse order = new OrderResponse(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), Collections.singletonList(orderLineItem));
        given(orderService.create(any())).willReturn(order);
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialize(order)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("주문 리스트")
    @Test
    public void listOrder() throws Exception {
        OrderLineItemResponse orderLineItem = new OrderLineItemResponse(1L, 1L, 1L, 1);
        OrderResponse order1 = new OrderResponse(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), Collections.singletonList(orderLineItem));
        OrderResponse order2 = new OrderResponse(2L, 2L, OrderStatus.COOKING.name(), LocalDateTime.now(), Collections.singletonList(orderLineItem));
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
    public void updateOrder() throws Exception {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 1L);
        OrderLineItemResponse orderLineItemResponse = new OrderLineItemResponse(1L, 1L, 1L, 1L);
        OrderRequest changedOrderRequest = new OrderRequest(1L, Collections.singletonList(orderLineItemRequest));
        OrderResponse changedOrderResponse = new OrderResponse(1L,1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), Collections.singletonList(orderLineItemResponse));

        given(orderService.changeOrderStatus(1L, changedOrderRequest)).willReturn(changedOrderResponse);
        mockMvc.perform(put("/api/orders/{orderId}/order-status", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialize(changedOrderResponse)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private String serialize(Object request) throws JsonProcessingException {
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(request);
    }
}
