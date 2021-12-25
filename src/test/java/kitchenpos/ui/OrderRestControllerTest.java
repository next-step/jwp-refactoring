package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
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
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext ctx;

    @MockBean
    private OrderService orderService;

    @BeforeEach
    void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void create() throws Exception {
        // given
        final LocalDateTime localDateTime = LocalDateTime.now();
        final OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, null, 5);
        final OrderLineItem orderLineItem2 = new OrderLineItem(1L, 1L, null, 5);
        final List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem1, orderLineItem2);
        final Order order = new Order(1L, 1L, OrderStatus.COOKING.name(), null, orderLineItems);
        final OrderTable orderTable = new OrderTable(1L, null, 0, false);
        given(orderService.create(any())).willReturn(order);

        // when
        final ResultActions actions = mvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(order)))
                .andDo(print());
        //then
        actions.andExpect(status().isCreated())
                .andExpect(header().stringValues("Location", "/api/orders/1"))
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("orderTableId").value(1L))
                .andExpect(jsonPath("orderStatus").value(OrderStatus.COOKING.name()))
                .andExpect(jsonPath("orderLineItems").isArray());
    }

    @DisplayName("주문을 조회한다.")
    @Test
    void list() throws Exception {
        // given
        final OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, null, 5);
        final OrderLineItem orderLineItem2 = new OrderLineItem(1L, 1L, null, 5);
        final List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem1, orderLineItem2);
        final Order order = new Order(1L, 1L, OrderStatus.COOKING.name(), null, orderLineItems);
        final OrderTable orderTable = new OrderTable(1L, null, 0, false);
        given(orderService.list()).willReturn(Collections.singletonList(order));

        // when
        final ResultActions actions = mvc.perform(get("/api/orders")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());

        //then
        actions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/json;charset=UTF-8")))
                .andExpect(content().string(containsString("COOKING")));
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() throws Exception {
        // given
        final OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, null, 5);
        final OrderLineItem orderLineItem2 = new OrderLineItem(1L, 1L, null, 5);
        final List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem1, orderLineItem2);
        final Order order = new Order(1L, 1L, OrderStatus.COMPLETION.name(), null, orderLineItems);
        final OrderTable orderTable = new OrderTable(1L, null, 0, false);

        given(orderService.changeOrderStatus(anyLong(), any())).willReturn(order);

        // when
        final ResultActions actions = mvc.perform(put("/api/orders/{orderId}/order-status", order.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(order)))
                .andDo(print());
        //then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("orderTableId").value(1L))
                .andExpect(jsonPath("orderStatus").value(OrderStatus.COMPLETION.name()))
                .andExpect(jsonPath("orderLineItems").isArray());
    }
}
