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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
@MockBean(JpaMetamodelMappingContext.class)
public class OrderRestControllerTest extends ControllerTest {
    @MockBean
    private OrderService orderService;

    @DisplayName("주문생성을 요청하면 생성된 주문 응답")
    @Test
    public void returnOrder() throws Exception {
        long menuId = 16;
        long quantity = 1;
        long orderTableId = 13;
        long orderId = 7;
        OrderRequest orderRequest = new OrderRequest(orderTableId, OrderStatus.COOKING, Arrays.asList(new OrderLineItemRequest(menuId, quantity)));
        Menu menu = Menu.builder().price(BigDecimal.valueOf(1000)).id(menuId).build();
        OrderLineItem orderLineItem = OrderLineItem.builder().menu(menu).build();
        OrderTable orderTable = OrderTable.builder().build();
        doReturn(OrderResponse.of(Order.builder().id(orderId)
                .orderLineItems(Arrays.asList(orderLineItem))
                .orderTable(orderTable)
                .build())).when(orderService).create(any(OrderRequest.class));

        webMvc.perform(post("/api/orders")
                .content(mapper.writeValueAsString(orderRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is((int) orderId)))
                .andExpect(jsonPath("$.orderLineItems", hasSize(1)))
                .andExpect(status().isCreated());
    }

    @DisplayName("주문생성을 요청하면 주문생성 실패응답")
    @Test
    public void throwsExceptionWhenOrderCreate() throws Exception {
        doThrow(new IllegalArgumentException()).when(orderService).create(any(OrderRequest.class));

        webMvc.perform(post("/api/orders")
                .content(mapper.writeValueAsString(new OrderRequest()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("주문목록을 요청하면 메뉴목록을 응답")
    @Test
    public void returnOrders() throws Exception {
        Order order1 = Order.builder().id(1l)
                .orderLineItems(Collections.EMPTY_LIST)
                .orderTable(OrderTable.builder().build())
                .build();
        Order order2 = Order.builder().id(2l)
                .orderLineItems(Collections.EMPTY_LIST)
                .orderTable(OrderTable.builder().build())
                .build();
        List<OrderResponse> orders = Arrays.asList(new OrderResponse(order1), new OrderResponse(order2));
        doReturn(orders).when(orderService).list();

        webMvc.perform(get("/api/orders"))
                .andExpect(jsonPath("$", hasSize(orders.size())))
                .andExpect(status().isOk());
    }
}

