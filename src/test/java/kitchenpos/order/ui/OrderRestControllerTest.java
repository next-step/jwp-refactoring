package kitchenpos.order.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.config.MockMvcTestConfig;
import kitchenpos.domain.OrderTable;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.ui.OrderRestController;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrderRestController.class)
@MockMvcTestConfig
class OrderRestControllerTest {
    private static final String ORDER_API_URI = "/api/orders";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    private Order order;

    @BeforeEach
    void setUp() {
        OrderTable orderTable = new OrderTable(1L, 1L, 0, false);
        order = new Order(1L, orderTable, new ArrayList<>());
    }

    @DisplayName("주문을 등록할 수 있다.")
    @Test
    void createTest() throws Exception {
        // given
        given(orderService.create(any())).willReturn(order);

        // when
        ResultActions actions = mockMvc.perform(post(ORDER_API_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)));

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().string("location", ORDER_API_URI + "/1"))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @DisplayName("주문의 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatusTest() throws Exception {
        // given
        given(orderService.changeOrderStatus(order.getId(), order)).willReturn(order);

        // when
        ResultActions actions = mockMvc.perform(put(ORDER_API_URI + "/{orderId}/order-status", order.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)));

        // then
        actions.andExpect(status().isOk());
    }

    @DisplayName("주문의 목록을 조회할 수 있다.")
    @Test
    void listTest() throws Exception {
        // given
        given(orderService.list()).willReturn(Arrays.asList(order));

        // when
        ResultActions actions = mockMvc.perform(get(ORDER_API_URI));

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].orderStatus").value(OrderStatus.COOKING.name()));
    }
}
