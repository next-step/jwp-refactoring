package kitchenpos.ui;

import kitchenpos.application.OrderService;
import kitchenpos.domain.*;
import kitchenpos.dto.request.OrderStatusChangeRequest;
import kitchenpos.dto.response.OrderLineItemViewResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.ui.JsonUtil.toJson;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderRestController.class)
@ExtendWith(MockitoExtension.class)
class OrderRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

//    private Long seq;
//    private Long orderId;
//    private Long menuId;
//    private long quantity;
    @Test
    void changeOrderStatus() throws Exception {
        // given
        Order fakeOrder = new Order(1L, null, OrderStatus.COMPLETION, null, null);
        Menu fakeMenu = new Menu(1L, null, BigDecimal.valueOf(1), null, null);

        OrderStatusChangeRequest orderStatusChangeRequest = new OrderStatusChangeRequest(OrderStatus.COOKING);
        OrderTable orderTable = new OrderTable(1L, null, null, null, false);
        List<OrderLineItem> orderLineItems = Arrays.asList(
                new OrderLineItem(1L, fakeOrder, fakeMenu, 1),
                new OrderLineItem(2L, fakeOrder, fakeMenu, 2),
                new OrderLineItem(3L, fakeOrder, fakeMenu, 3)
        );
        Order order = new Order(null, orderTable, OrderStatus.COMPLETION, LocalDateTime.now(), orderLineItems);

        given(orderService.changeOrderStatus(any(), any(OrderStatus.class)))
                .willReturn(order);

        // when & then
        mockMvc.perform(
                put("/api/orders/1/order-status")
                        .content(toJson(orderStatusChangeRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(validateOrder("$", order))
                .andExpect(validateOrderLineItem("$.orderLineItems[0]", orderLineItems.get(0)))
                .andExpect(validateOrderLineItem("$.orderLineItems[1]", orderLineItems.get(1)))
                .andExpect(validateOrderLineItem("$.orderLineItems[2]", orderLineItems.get(2)));


    }

    private ResultMatcher validateOrderLineItem(String prefix, OrderLineItem orderLineItem) {
        return result -> {
            ResultMatcher.matchAll(
                    jsonPath(prefix + ".seq").value(orderLineItem.getSeq()),
                    jsonPath(prefix + ".orderId").value(orderLineItem.getOrder().getId()),
                    jsonPath(prefix + ".menuId").value(orderLineItem.getMenu().getId()),
                    jsonPath(prefix + ".quantity").value(orderLineItem.getQuantity())
            ).match(result);
        };
    }

    private ResultMatcher validateOrder(String prefix, Order order) {
        return result -> {
            ResultMatcher.matchAll(
                    jsonPath(prefix + ".id").value(order.getId()),
                    jsonPath(prefix + ".orderTableId").value(order.getOrderTable().getId()),
                    jsonPath(prefix + ".orderStatus").value(order.getOrderStatus().toString()),
                    jsonPath(prefix + ".orderedTime").value(order.getOrderedTime().toString())
            ).match(result);
        };
    }
}