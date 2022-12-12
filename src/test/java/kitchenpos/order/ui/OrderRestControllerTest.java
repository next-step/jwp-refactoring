package kitchenpos.order.ui;

import kitchenpos.ControllerTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(OrderRestController.class)
public class OrderRestControllerTest extends ControllerTest {
    @MockBean
    private OrderService orderService;

    @DisplayName("주문생성을 요청하면 생성된 주문 응답")
    @Test
    @Disabled
    public void returnOrder() throws Exception {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest();
        orderLineItemRequest.setMenuId(16l);
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderTableId(13l);
        orderRequest.setOrderStatus(OrderStatus.COOKING);
        orderRequest.setOrderLineItems(Arrays.asList(orderLineItemRequest));

        doReturn(OrderResponse.of(Order.builder().id(14l)
                .orderLineItems(Arrays.asList(OrderLineItem.builder().menu(Menu.builder().id(35l).build()).build()))
                .orderTable(OrderTable.builder().build())
                .build())).when(orderService).create(any(OrderRequest.class));

        webMvc.perform(post("/api/orders")
                        .content(mapper.writeValueAsString(orderRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @DisplayName("주문생성을 요청하면 주문생성 실패응답")
    @Test
    public void throwsExceptionWhenOrderCreate() throws Exception {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderTableId(13l);
        orderRequest.setOrderStatus(OrderStatus.COOKING);
        orderRequest.setOrderLineItems(Arrays.asList(new OrderLineItemRequest()));
        doThrow(new IllegalArgumentException()).when(orderService).create(any(OrderRequest.class));
        webMvc.perform(post("/api/orders")
                        .content(mapper.writeValueAsString(orderRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("주문목록을 요청하면 메뉴목록을 응답")
    @Test
    public void returnOrders() throws Exception {
        List<OrderResponse> orders = Arrays.asList(new OrderResponse(), new OrderResponse());
        doReturn(orders).when(orderService).list();

        webMvc.perform(get("/api/orders"))
                .andExpect(jsonPath("$", hasSize(orders.size())))
                .andExpect(status().isOk());
    }

    private List<Order> getOrders(Order order, int size) {
        return IntStream.rangeClosed(1, size)
                .mapToObj(value -> Order.builder()
                        .id(order.getId())
                        .orderTable(order.getOrderTable())
                        .orderStatus(order.getOrderStatus())
                        .orderLineItems(order.getOrderLineItems())
                        .build())
                .collect(Collectors.toList());
    }
}
