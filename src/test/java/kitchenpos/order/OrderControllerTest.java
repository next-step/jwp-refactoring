package kitchenpos.order;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.ui.OrderController;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.utils.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;

import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerTest extends ControllerTest {

    @PostConstruct
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new OrderController(orderService)).build();
    }

    private OrderRequest createOrder() {
        OrderRequest orderRequest = new OrderRequest();
        ReflectionTestUtils.setField(orderRequest, "orderTableId", 1L);

        OrderLineItemRequest orderLineItemRequestA = new OrderLineItemRequest();
        ReflectionTestUtils.setField(orderLineItemRequestA, "menuId", 1L);
        ReflectionTestUtils.setField(orderLineItemRequestA, "quantity", 3L);

        OrderLineItemRequest orderLineItemRequestB = new OrderLineItemRequest();
        ReflectionTestUtils.setField(orderLineItemRequestA, "menuId", 2L);
        ReflectionTestUtils.setField(orderLineItemRequestA, "quantity", 2L);

        ReflectionTestUtils.setField(orderRequest, "orderLineItems", Arrays.asList(orderLineItemRequestA, orderLineItemRequestB));
        return orderRequest;
    }

    @DisplayName("주문하다.")
    @Test
    void order() throws Exception {

        //given
        OrderRequest orderRequest = createOrder();
        OrderResponse orderResponse = new OrderResponse();
        ReflectionTestUtils.setField(orderResponse, "id", 1L);
        when(orderService.create(any())).thenReturn(orderResponse);

        //when
        ResultActions resultActions = post("/api/orders", orderRequest);

        //then
        resultActions.andExpect(status().isCreated());
    }

    @DisplayName("주문할 때 주문 Item 내역이 없을경우")
    @Test
    void orderEmptyItems() throws Exception {

        //given
        OrderRequest orderRequest = createOrder();
        ReflectionTestUtils.setField(orderRequest, "orderLineItems", Collections.emptyList());
        OrderResponse orderResponse = new OrderResponse();
        ReflectionTestUtils.setField(orderResponse, "id", 1L);
        when(orderService.create(any())).thenReturn(orderResponse);

        //when
        ResultActions resultActions = post("/api/orders", orderRequest);

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    @DisplayName("주문 리스트를 조회한다.")
    @Test
    void getOrders() throws Exception {

        //given
        final List<Order> orders = new ArrayList<>();
        final int numberOfGuests = 10;
        final OrderTable orderTable = OrderTable.create(numberOfGuests);
        ReflectionTestUtils.setField(orderTable, "id", 1L);
        final Order order = Order.create(orderTable);
        ReflectionTestUtils.setField(order, "id", 1L);
        orders.add(order);

        when(orderService.list()).thenReturn(OrderResponse.ofList(orders));

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
        OrderRequest orderRequest = createOrder();
        OrderResponse orderResponse = new OrderResponse();
        ReflectionTestUtils.setField(orderResponse, "orderStatus", OrderStatus.COMPLETION.name());

        when(orderService.changeOrderStatus(anyLong())).thenReturn(orderResponse);

        //when
        ResultActions resultActions = put("/api/orders/1/order-status", new LinkedMultiValueMap<>(), orderRequest);

        //then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.orderStatus").value(OrderStatus.COMPLETION.name()));
    }
}
