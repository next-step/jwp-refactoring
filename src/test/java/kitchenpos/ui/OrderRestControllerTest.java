package kitchenpos.ui;

import static kitchenpos.OrderBuilder.anOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;
import kitchenpos.OrderTableBuilder;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest extends ControllerTest {
    @MockBean
    private OrderService orderService;

    private Order orderWithCookingStatus;
    private Order orderWithMealStatus;

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();

        orderWithCookingStatus = anOrder()
            .withId(1L)
            .withStatus(OrderStatus.COOKING)
            .withOrderTable(OrderTableBuilder.nonEmptyOrderTableWithIdAndGuestNo(1L, 2))
            .withOrderLineItems(Collections.emptyList())
            .build();
        orderWithMealStatus = anOrder()
            .withId(2L)
            .withStatus(OrderStatus.MEAL)
            .withOrderTable(OrderTableBuilder.nonEmptyOrderTableWithIdAndGuestNo(2L, 4))
            .withOrderLineItems(Collections.emptyList())
            .build();
    }

    @DisplayName("[POST] 주문 생성")
    @Test
    void create() throws Exception {
        given(orderService.create(any(Order.class))).willReturn(orderWithCookingStatus);

        perform(postAsJson("/api/orders", orderWithCookingStatus))
            .andExpect(status().isCreated())
            .andExpect(header().string("location", "/api/orders/1"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isNotEmpty())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.orderTableId").value(1L))
            .andExpect(jsonPath("$.orderStatus").value(OrderStatus.COOKING.name()))
            .andExpect(jsonPath("$.orderLineItems").isEmpty());
    }

    @DisplayName("[GET] 주문 목록 조회")
    @Test
    void list() throws Exception {
        given(orderService.list()).willReturn(Arrays.asList(orderWithCookingStatus, orderWithMealStatus));

        perform(get("/api/orders"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isNotEmpty())
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].orderTableId").value(1L))
            .andExpect(jsonPath("$[0].orderStatus").value(OrderStatus.COOKING.name()))
            .andExpect(jsonPath("$[0].orderLineItems").isEmpty())
            .andExpect(jsonPath("$[1].id").value(2L))
            .andExpect(jsonPath("$[1].orderTableId").value(2L))
            .andExpect(jsonPath("$[1].orderStatus").value(OrderStatus.MEAL.name()))
            .andExpect(jsonPath("$[1].orderLineItems").isEmpty());
    }

    @DisplayName("[PUT] 주문 상태 변경")
    @Test
    void changeOrderStatus() throws Exception {
        given(orderService.changeOrderStatus(anyLong(), any(Order.class))).willReturn(orderWithMealStatus);

        perform(putAsJson("/api/orders/1/order-status", orderWithCookingStatus))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.orderStatus").value(OrderStatus.MEAL.name()));
    }
}
