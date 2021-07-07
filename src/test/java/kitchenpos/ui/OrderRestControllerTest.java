package kitchenpos.ui;

import kitchenpos.ApiTest;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Collections;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest extends ApiTest {

    @MockBean
    private OrderService orderService;

    private Order order;

    @BeforeEach
    public void setUp() {
        super.setUp();

        OrderLineItem orderLineItem1 = new OrderLineItem();
        orderLineItem1.setSeq(1L);

        OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem2.setSeq(2L);

        order = new Order();
        order.setId(1L);
        order.setOrderStatus(COOKING.name());
        order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));
    }

    @Test
    @DisplayName("주문을 생성한다")
    void createTest() throws Exception {

        // given
        when(orderService.create(any())).thenReturn(order);

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(order)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("주문 목록을 조회한다")
    void listTest() throws Exception {

        // given
        when(orderService.list()).thenReturn(Collections.singletonList(order));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("COOKING")))
        ;
    }

    @Test
    @DisplayName("주문 상태를 변경한다")
    void changeOrderStatusTest() throws Exception {

        // given
        order.setOrderStatus(COMPLETION.name());
        when(orderService.changeOrderStatus(any(), any())).thenReturn(order);

        mockMvc.perform(put("/api/orders/{orderId}/order-status", order.getId(), order)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("COMPLETION")))
        ;
    }
}