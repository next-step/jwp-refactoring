package kitchenpos.domain.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.api.OrderRestController;
import kitchenpos.service.order.application.OrderService;
import kitchenpos.service.order.dto.OrderResponse;
import kitchenpos.service.order.dto.OrdersRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrdersRestControllerTest {
    private MockMvc mockMvc;
    @Mock
    private OrderService orderService;
    @InjectMocks
    private OrderRestController orderRestController;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.objectMapper = new ObjectMapper();
        this.mockMvc = MockMvcBuilders.standaloneSetup(orderRestController).build();
    }

    @Test
    void test_get() throws Exception {
        //given
        given(orderService.list()).willReturn(Collections.emptyList());

        //then
        mockMvc.perform(get("/api/orders")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    void test_post() throws Exception {
        //given
        given(orderService.create(any())).willReturn(new OrderResponse());

        //then
        mockMvc.perform(post("/api/orders")
                .content(objectMapper.writeValueAsString(new OrdersRequest(0, Collections.emptyList())))
                .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isCreated());
    }

    @Test
    void test_put_changeEmpty() throws Exception {
        //given
        given(orderService.changeOrderStatus(any(), any())).willReturn(new OrderResponse());

        //then
        mockMvc.perform(put("/api/orders/{orderId}/order-status", 0)
                .content("{\"orderStatus\": \"COMPLETION\"}")
                .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
    }
}
