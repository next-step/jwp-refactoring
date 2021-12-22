package kitchenpos.product.testfixtures.ui;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.common.CommonTestFixtures;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.ui.OrderRestController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {

    private static final String BASE_PATH = "/api/orders";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @DisplayName("주문 등록")
    @Test
    void create() throws Exception {
        //given
        List<OrderLineItem> orderLineItems = Arrays.asList(
            new OrderLineItem(1L, 1),
            new OrderLineItem(2L, 3));
        Order requestOrder = new Order(1L, orderLineItems);
        Order expectedOrder = new Order(1L, 1L, OrderStatus.COOKING.name(),
            LocalDateTime.now(), orderLineItems);
        given(orderService.create(any()))
            .willReturn(expectedOrder);

        //when, then
        mockMvc.perform(post(BASE_PATH)
                .content(CommonTestFixtures.asJsonString(requestOrder))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(expectedOrder.getId()))
            .andExpect(jsonPath("$.orderTableId").value(expectedOrder.getOrderTableId()));
    }

    @DisplayName("주문 목록 조회")
    @Test
    void list() throws Exception {
        //given
        List<OrderLineItem> orderLineItems1 = Arrays.asList(
            new OrderLineItem(1L, 1),
            new OrderLineItem(2L, 3));

        List<OrderLineItem> orderLineItems2 = Arrays.asList(
            new OrderLineItem(1L, 2),
            new OrderLineItem(3L, 2));

        List<Order> expectedOrders = Arrays.asList(
            new Order(1L, 1L, OrderStatus.MEAL.name(), LocalDateTime.now(), orderLineItems1),
            new Order(2L, 2L, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems2)
        );
        given(orderService.list())
            .willReturn(expectedOrders);

        //when, then
        mockMvc.perform(get(BASE_PATH))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[*]['id']",
                containsInAnyOrder(
                    expectedOrders.stream()
                        .mapToInt(order -> order.getId().intValue())
                        .boxed()
                        .toArray(Integer[]::new))));
    }

    @DisplayName("주문_상태_변경")
    @Test
    void changeOrderStatus() throws Exception {
        //given
        List<OrderLineItem> orderLineItems = Arrays.asList(
            new OrderLineItem(1L, 1),
            new OrderLineItem(2L, 3));
        String changeOrderStatus = OrderStatus.MEAL.name();
        Order requestOrder = new Order(OrderStatus.MEAL.name());
        Order expectedOrder = new Order(1L, 1L, changeOrderStatus, LocalDateTime.now(),
            orderLineItems);
        given(orderService.changeOrderStatus(any(), any()))
            .willReturn(expectedOrder);

        //when, then
        mockMvc.perform(
                put(BASE_PATH + "/{orderId}/order-status", expectedOrder.getId())
                    .content(CommonTestFixtures.asJsonString(requestOrder))
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(expectedOrder.getId()))
            .andExpect(jsonPath("$.orderStatus").value(expectedOrder.getOrderStatus()));
    }
}
