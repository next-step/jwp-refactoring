package kitchenpos.order.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
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
import static org.mockito.Mockito.when;
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
        OrderResponse orderResponse = new OrderResponse(1L,"COOKING",LocalDateTime.now());
        given(orderService.create(any())).willReturn(orderResponse);
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJsonString(new OrderRequest(1L))))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("주문 리스트")
    @Test
    public void list() throws Exception {
        when(orderService.list()).thenReturn(Arrays.asList(
                new OrderResponse(1L,"COOKING", getNow()),
                new OrderResponse(2L,"COOKING", getNow()))
        );

        mockMvc.perform(get("/api/orders"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[1].id").value(2L));
    }

    private LocalDateTime getNow() {
        return LocalDateTime.now();
    }

    @DisplayName("주문 상태 수정")
    @Test
    public void updateStatus() throws Exception {
        OrderResponse response = new OrderResponse(1L, "COMPLETION", getNow());
        OrderStatusRequest request = new OrderStatusRequest(OrderStatus.COOKING);
        given(orderService.changeOrderStatus(any(), any())).willReturn(response);
        mockMvc.perform(put("/api/orders/{orderId}/order-status", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJsonString(request)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private String makeJsonString(Object request) throws JsonProcessingException {
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(request);
    }
}