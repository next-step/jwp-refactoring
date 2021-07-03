package kitchenpos.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("주문 관련 기능 테스트")
@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {
    private static final String ORDER_URI = "/api/orders";
    private static final String ORDER_STATUS_CHANGE_URI = "/{orderId}/order-status";

    private Order order;

    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRestController orderRestController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();

        order = new Order();
        order.setId(1L);
        order.setOrderStatus(OrderStatus.COOKING.name());
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void create() throws Exception {
        given(orderService.create(any())).willReturn(order);

        final ResultActions actions = mockMvc.perform(post(ORDER_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toString(order)));

        actions
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "/api/orders/1"))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() throws Exception {
        given(orderService.list()).willReturn(Collections.singletonList(order));

        final ResultActions actions = mockMvc.perform(get(ORDER_URI));

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].orderStatus").value(OrderStatus.COOKING.name()));
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() throws Exception {
        given(orderService.changeOrderStatus(order.getId(), order)).willReturn(order);

        final ResultActions actions = mockMvc.perform(put(ORDER_URI + ORDER_STATUS_CHANGE_URI , order.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toString(order)));

        actions.andExpect(status().isOk());
    }

    public String toString(Order order) throws JsonProcessingException {
        return objectMapper.writeValueAsString(order);
    }
}
