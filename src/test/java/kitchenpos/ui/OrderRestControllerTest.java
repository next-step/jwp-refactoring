package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(new CharacterEncodingFilter("UTF-8", true)) //한글 깨짐 처리
                .build();
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void create() throws Exception {
        OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1L);
        Order order = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), Arrays.asList(orderLineItem));
        String orderJsonString = objectMapper.writeValueAsString(order);
        given(orderService.create(any())).willReturn(order);

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderJsonString))
                .andExpect(status().isCreated());
    }

    @DisplayName("주문을 조회한다.")
    @Test
    void list() throws Exception {
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 1L, 1L);
        Order order1 = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), Arrays.asList(orderLineItem1));
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, 2L, 3L, 1L);
        Order order2 = new Order(2L, 2L, OrderStatus.MEAL.name(), LocalDateTime.now(), Arrays.asList(orderLineItem2));

        given(orderService.list()).willReturn(Arrays.asList(order1, order2));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk());
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() throws Exception {
        Long orderId = 1L;
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 1L, 1L);
        Order changedOrder = new Order(orderId, 1L, OrderStatus.MEAL.name(), LocalDateTime.now(), Arrays.asList(orderLineItem1));
        String orderJsonString = objectMapper.writeValueAsString(changedOrder);

        given(orderService.changeOrderStatus(anyLong(), any())).willReturn(changedOrder);

        mockMvc.perform(put("/api/orders/" + orderId + "/order-status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderJsonString))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("MEAL")));
    }
}
