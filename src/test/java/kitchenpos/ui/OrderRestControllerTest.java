package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.*;
import kitchenpos.order.ui.OrderRestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderRestControllerTest {
    private static final String URI = "/api/orders";

    @InjectMocks
    private ObjectMapper objectMapper;

    @InjectMocks
    private OrderRestController orderRestController;

    @Mock
    private OrderService orderService;

    private MockMvc mockMvc;
    private OrderRequest 주문_요청;
    private OrderResponse 주문_응답;
    private List<OrderLineItemRequest> 주문_항목;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(orderRestController).build();
        주문_항목 = Arrays.asList(new OrderLineItemRequest(1L, 1));
        주문_요청 = new OrderRequest(1L, 주문_항목);
        주문_응답 = new OrderResponse(1L, 1L, OrderStatus.COOKING.name(), null,
                new ArrayList<>(Arrays.asList(new OrderLineItemResponse(1L, 1L, 1L, 1))));
    }

    @Test
    void post() throws Exception {
        // given
        given(orderService.create(any())).willReturn(주문_응답);
        System.out.println(objectMapper.writeValueAsString(주문_요청));
        String a = objectMapper.writeValueAsString(주문_요청);
        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(주문_요청)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderStatus").value("COOKING"))
                .andDo(print());
    }

    @Test
    void get() throws Exception {
        // given
        given(orderService.list()).willReturn(Collections.singletonList(주문_응답));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get(URI)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderStatus").value("COOKING"))
                .andDo(print());
    }

    @Test
    void put() throws Exception {
        // given
        주문_응답 = new OrderResponse(1L, 1L, OrderStatus.COMPLETION.name(), null,
                new ArrayList<>(Arrays.asList(new OrderLineItemResponse(1L, 1L, 1L, 1))));
        given(orderService.changeOrderStatus(anyLong(), any())).willReturn(주문_응답);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{orderId}/order-status", 1)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new OrderStatusRequest(OrderStatus.COMPLETION))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderStatus").value("COMPLETION"))
                .andDo(print());
    }
}
