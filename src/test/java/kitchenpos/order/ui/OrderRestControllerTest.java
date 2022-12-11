package kitchenpos.order.ui;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.generator.BuilderArbitraryGenerator;
import kitchenpos.ControllerTest;
import kitchenpos.order.application.OrderService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
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
                .andExpect(jsonPath("$.orderTableId", is(order.getOrderTableId().intValue())))
                .andExpect(jsonPath("$.orderStatus", is(order.getOrderStatus())))
                .andExpect(jsonPath("$.orderLineItems", hasSize(order.getOrderLineItems().size())))
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
        List<Order> orders = FixtureMonkey.create()
                .giveMeBuilder(Order.class)
                .sampleList(Arbitraries.integers().between(1, 50).sample());
        doReturn(orders).when(orderService).list();

        webMvc.perform(get("/api/orders"))
                .andExpect(jsonPath("$", hasSize(orders.size())))
                .andExpect(status().isOk());
    }

    private Order getOrder() {
        return   FixtureMonkey.builder()
                .defaultGenerator(BuilderArbitraryGenerator.INSTANCE)
                .build()
                .giveMeBuilder(Order.class)
                .set("id", Arbitraries.longs().between(1, 100))
                .set("orderTableId", Arbitraries.longs().between(1, 100))
                .set("orderStatus", OrderStatus.COOKING.name())
                .set("orderedTime", LocalDateTime.now())
                .set("orderLineItems", getOrderLineItems())
                .sample();
    }

    private List<OrderLineItem> getOrderLineItems() {
        return  FixtureMonkey.builder()
                .defaultGenerator(BuilderArbitraryGenerator.INSTANCE)
                .build()
                .giveMeBuilder(OrderLineItem.class)
                .set("id", Arbitraries.longs().between(1, 20))
                .sampleList(10);
    }
}
