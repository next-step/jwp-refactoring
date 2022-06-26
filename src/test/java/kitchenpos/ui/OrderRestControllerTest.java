package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

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
    private Order 주문;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(orderRestController).build();
        주문 = new Order();
        주문.setOrderStatus(OrderStatus.COOKING.toString());
    }

    @Test
    void post() throws Exception {
        // given
        given(orderService.create(any())).willReturn(주문);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(주문)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderStatus").value("COOKING"))
                .andDo(print());
    }

    @Test
    void get() throws Exception {
        // given
        given(orderService.list()).willReturn(Collections.singletonList(주문));

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
        주문.setOrderStatus(OrderStatus.COMPLETION.toString());
        given(orderService.changeOrderStatus(anyLong(), any())).willReturn(주문);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{orderId}/order-status", 1)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(주문)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderStatus").value("COMPLETION"))
                .andDo(print());
    }
}
