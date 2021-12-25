package kitchenpos.order;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.ui.OrderRestController;
import kitchenpos.utils.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;

import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerTest extends ControllerTest {

    @PostConstruct
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new OrderRestController(orderService)).build();
    }

    private Order createOrder() {
        Order order = new Order();
        order.setId(1L);
        order.setOrderTableId(1L);
        order.setOrderStatus(OrderStatus.COOKING.name());

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1);
        orderLineItem.setOrderId(order.getId());
        order.setOrderLineItems(Arrays.asList(orderLineItem));
        return order;
    }

    @DisplayName("주문하다.")
    @Test
    void order() throws Exception {

        //given
        Order order = createOrder();
        when(orderService.create(any())).thenReturn(order);

        //when
        ResultActions resultActions = post("/api/orders", order);

        //then
        resultActions.andExpect(status().isCreated());
    }

    @DisplayName("주문 리스트를 조회한다.")
    @Test
    void getOrders() throws Exception {

        //given
        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        order.setId(1L);
        order.setOrderTableId(1L);
        order.setOrderStatus(OrderStatus.COOKING.name());
        orders.add(order);
        when(orderService.list()).thenReturn(orders);

        //when
        ResultActions resultActions = get("/api/orders", new LinkedMultiValueMap<>());

        //then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$").isArray());
        resultActions.andExpect(jsonPath("$[0]['id']").isNumber());
        resultActions.andExpect(jsonPath("$[0]['orderTableId']").isNumber());
        resultActions.andExpect(jsonPath("$[0]['orderStatus']").value(OrderStatus.COOKING.name()));
    }

    @DisplayName("주문 상태를 수정하다.")
    @Test
    void changeOrderStatus() throws Exception {

        //given
        Order order = createOrder();
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        when(orderService.changeOrderStatus(anyLong(), any())).thenReturn(order);

        //when
        ResultActions resultActions = put("/api/orders/1/order-status", new LinkedMultiValueMap<>(), order);

        //then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.orderStatus").value(OrderStatus.COMPLETION.name()));
    }
}
