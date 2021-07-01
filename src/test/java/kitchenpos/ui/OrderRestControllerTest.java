package kitchenpos.ui;

import kitchenpos.application.OrderService;
import kitchenpos.domain.*;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemCreateRequest;
import kitchenpos.dto.request.OrderStatusChangeRequest;
import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.OrderTableFixture.미사용중인_테이블;
import static kitchenpos.ui.JsonUtil.toJson;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderRestController.class)
@ExtendWith(MockitoExtension.class)
class OrderRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    private Product product;

    private MenuProduct menuProduct1;
    private MenuProduct menuProduct2;

    @BeforeEach
    void setUp() {
        OrderTableFixture.cleanUp();

        this.product = new Product("SIMPLE", new Price(100));
        this.menuProduct1 = new MenuProduct(null, product, 1);
        this.menuProduct2 = new MenuProduct(null, product, 1);
    }


    @Test
    void create() throws Exception {
        // given
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(1L, OrderStatus.MEAL, Arrays.asList(new OrderLineItemCreateRequest(1L, 1L)));
        Order fakeOrder = new Order(1L, 미사용중인_테이블, OrderStatus.MEAL, LocalDateTime.now(), null);
        Menu fakeMenu = new Menu(1L, "Hello", new Price(1), null, Arrays.asList(menuProduct1));
        List<OrderLineItem> orderLineItems = Arrays.asList(
                new OrderLineItem(1L, fakeOrder, fakeMenu, 1),
                new OrderLineItem(2L, fakeOrder, fakeMenu, 2),
                new OrderLineItem(3L, fakeOrder, fakeMenu, 3)
        );
        Order order = new Order(1L, 미사용중인_테이블, OrderStatus.MEAL, LocalDateTime.now(), orderLineItems);

        given(orderService.create(any(OrderCreate.class)))
                .willReturn(order);

        // when
        mockMvc.perform(
                post("/api/orders")
                        .content(toJson(orderCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(validateOrder("$", order))
                .andExpect(validateOrderLineItem("$.orderLineItems[0]", orderLineItems.get(0)))
                .andExpect(validateOrderLineItem("$.orderLineItems[1]", orderLineItems.get(1)))
                .andExpect(validateOrderLineItem("$.orderLineItems[2]", orderLineItems.get(2)));
    }

    @Test
    void list() throws Exception {
        // given
        Order fakeOrder = new Order(1L, null, OrderStatus.COMPLETION, null, null);
        Order fakeOrder2 = new Order(2L, null, OrderStatus.MEAL, null, null);

        Menu fakeMenu = new Menu(1L, null, new Price(1), null, Arrays.asList(menuProduct1));
        Menu fakeMenu2 = new Menu(2L, null, new Price(2), null, Arrays.asList(menuProduct2));

        List<OrderLineItem> orderLineItems = Arrays.asList(
                new OrderLineItem(1L, fakeOrder, fakeMenu, 1),
                new OrderLineItem(2L, fakeOrder, fakeMenu, 2)
        );
        List<OrderLineItem> orderLineItems2 = Arrays.asList(
                new OrderLineItem(3L, fakeOrder2, fakeMenu2, 3),
                new OrderLineItem(4L, fakeOrder2, fakeMenu2, 4),
                new OrderLineItem(5L, fakeOrder2, fakeMenu2, 5)
        );
        Order order = new Order(null, 미사용중인_테이블, OrderStatus.COMPLETION, LocalDateTime.now(), orderLineItems);
        Order order2 = new Order(null, 미사용중인_테이블, OrderStatus.MEAL, LocalDateTime.now(), orderLineItems2);

        given(orderService.list()).willReturn(Arrays.asList(order, order2));

        // when & then
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(validateOrder("$.[0]", order))
                .andExpect(validateOrderLineItem("$.[0].orderLineItems[0]", orderLineItems.get(0)))
                .andExpect(validateOrderLineItem("$.[0].orderLineItems[1]", orderLineItems.get(1)))
                .andExpect(validateOrder("$.[1]", order2))
                .andExpect(validateOrderLineItem("$.[1].orderLineItems[0]", orderLineItems2.get(0)))
                .andExpect(validateOrderLineItem("$.[1].orderLineItems[1]", orderLineItems2.get(1)))
                .andExpect(validateOrderLineItem("$.[1].orderLineItems[2]", orderLineItems2.get(2)));
    }

    @Test
    void changeOrderStatus() throws Exception {
        // given
        Order fakeOrder = new Order(1L, null, OrderStatus.COMPLETION, null, null);
        Menu fakeMenu = new Menu(1L, null, new Price(1), null, Arrays.asList(menuProduct1));

        OrderStatusChangeRequest orderStatusChangeRequest = new OrderStatusChangeRequest(OrderStatus.COOKING);

        List<OrderLineItem> orderLineItems = Arrays.asList(
                new OrderLineItem(1L, fakeOrder, fakeMenu, 1),
                new OrderLineItem(2L, fakeOrder, fakeMenu, 2),
                new OrderLineItem(3L, fakeOrder, fakeMenu, 3)
        );
        Order order = new Order(null, 미사용중인_테이블, OrderStatus.COMPLETION, LocalDateTime.now(), orderLineItems);

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
                    jsonPath(prefix + ".quantity").value(orderLineItem.getQuantity().toLong())
            ).match(result);
        };
    }

    private ResultMatcher validateOrder(String prefix, Order order) {
        return result -> {
            ResultMatcher.matchAll(
                    jsonPath(prefix + ".id").value(order.getId()),
                    jsonPath(prefix + ".orderTableId").value(order.getOrderTable().getId()),
                    jsonPath(prefix + ".orderStatus").value(order.getOrderStatus().toString()),
                    jsonPath(prefix + ".orderedTime").isNotEmpty()
            ).match(result);
        };
    }
}