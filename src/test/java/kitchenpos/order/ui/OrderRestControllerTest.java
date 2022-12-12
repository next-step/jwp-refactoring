package kitchenpos.order.ui;

import kitchenpos.ControllerTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
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
    public void returnOrder() throws Exception {
        Order order = getOrder();
        doReturn(order).when(orderService).create(any(Order.class));

        webMvc.perform(post("/api/orders")
                .content(mapper.writeValueAsString(Menu.builder().build()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(order.getId().intValue())))
                .andExpect(status().isCreated());
    }

    @DisplayName("주문생성을 요청하면 주문생성 실패응답")
    @Test
    public void throwsExceptionWhenOrderCreate() throws Exception {
        doThrow(new IllegalArgumentException()).when(orderService).create(any(Order.class));

        webMvc.perform(post("/api/orders")
                .content(mapper.writeValueAsString(Order.builder().build()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("주문목록을 요청하면 메뉴목록을 응답")
    @Test
    public void returnOrders() throws Exception {
        List<Order> orders = getOrders(Order.builder()
                .id(Arbitraries.longs().between(1, 100).sample())
                .build(), 5);
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

    private Order getOrder() {
        return Order.builder()
                .id(Arbitraries.longs().between(1, 100).sample())
                .orderLineItems(getOrderLineItems())
                .orderStatus(OrderStatus.COOKING.name())
                .orderTable(OrderTable.builder().id(13l).build())
                .build();
    }

    private List<OrderLineItem> getOrderLineItems() {
        return IntStream.rangeClosed(1, 20)
                .mapToObj(value -> OrderLineItem.builder()
                        .seq(Arbitraries.longs().between(1, 20).sample())
                        .order(Order.builder().id(14l).orderTable(OrderTable.builder().build()).build())
                        .build())
                .collect(Collectors.toList());
    }
}
