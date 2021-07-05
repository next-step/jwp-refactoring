package kitchenpos.order.controller;

import kitchenpos.common.ControllerTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.util.NestedServletException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerTest extends ControllerTest {

    private Order order;

    @BeforeEach
    public void setup() {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L);
        orderLineItems.add(orderLineItem);
        order = new Order(3L, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
    }

    @Test
    @DisplayName("주문을 생성 한다")
    public void createOrder() throws Exception {
        // when
        // then
        주문_생성_요청(order)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("orderTableId").value(3))
                .andExpect(jsonPath("orderStatus").value("COOKING"))
                .andExpect(jsonPath("$.orderLineItems.[0].orderId").isNumber())
                .andExpect(jsonPath("$.orderLineItems.[0].quantity").value(1))
        ;
    }

    @Test
    @DisplayName("주문을 생성 실패 - orderLineItems 가 없을 경우")
    public void createOrderFailByOrderLineItemsIsNull() {
        // given
        Order order = new Order(1L, OrderStatus.COOKING.name(), LocalDateTime.now(), null);

        // when
        // then
        assertThrows(NestedServletException.class, () -> 주문_생성_요청(order));
    }

    @Test
    @DisplayName("주문 리스트를 가져온다")
    public void selectOrderList() throws Exception {
        // given
        주문_생성_요청(order);

        // when
        // then
        주문_리스트_요청()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].orderTableId").isNumber())
                .andExpect(jsonPath("$.[0].orderStatus").exists())
                .andExpect(jsonPath("$.[0].orderLineItems.[0].orderId").isNumber())
                .andExpect(jsonPath("$.[0].orderLineItems.[0].quantity").value(1))
        ;
    }

    @Test
    @DisplayName("주문 상태를 변경한다")
    public void modifyOrder() throws Exception {
        // given
        주문_생성_요청(order);
        order.setOrderStatus(OrderStatus.MEAL.name());

        // when
        // then
        주문_상태_변경_요청(order, 3L)
                .andExpect(status().isOk())
                .andExpect(jsonPath("orderTableId").isNumber())
                .andExpect(jsonPath("orderStatus").value("MEAL"))
                .andExpect(jsonPath("$.orderLineItems.[0].orderId").isNumber())
                .andExpect(jsonPath("$.orderLineItems.[0].quantity").value(1))
        ;
    }

    @Test
    @DisplayName("주문 상태를 변경 실패 - 이미 계산 완료 된 주문")
    public void modifyOrderFailByCompletionOrder() {
        // given
        order.setOrderStatus(OrderStatus.MEAL.name());

        // when
        // then
        assertThrows(NestedServletException.class, () -> 주문_상태_변경_요청(order, 1L));
    }


    private ResultActions 주문_생성_요청(Order order) throws Exception {
        return mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andDo(print());
    }

    private ResultActions 주문_리스트_요청() throws Exception {
        return mockMvc.perform(get("/api/orders")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions 주문_상태_변경_요청(Order order, Long orderId) throws Exception {
        return mockMvc.perform(put("/api/orders/{orderId}/order-status", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andDo(print());
    }

}
