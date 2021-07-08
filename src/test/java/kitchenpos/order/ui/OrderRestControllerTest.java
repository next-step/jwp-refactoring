package kitchenpos.order.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.ui.OrderRestController;
import kitchenpos.utils.MockMvcControllerTest;
import kitchenpos.utils.domain.MenuObjects;
import kitchenpos.utils.domain.OrderLineItemObjects;
import kitchenpos.utils.domain.OrderObjects;

@DisplayName("주문 관리 기능")
@WebMvcTest(controllers = OrderRestController.class)
class OrderRestControllerTest extends MockMvcControllerTest {

    public static final String DEFAULT_REQUEST_URL = "/api/orders";
    @MockBean
    private OrderService orderService;

    @Autowired
    private OrderRestController orderRestController;

    private OrderLineItemObjects orderLineItemObjects;
    private OrderObjects orderObjects;
    private MenuObjects menuObjects;

    @Override
    protected Object controller() {
        return orderRestController;
    }

    @BeforeEach
    void setUp() {
        orderLineItemObjects = new OrderLineItemObjects();
        orderObjects = new OrderObjects();
        menuObjects = new MenuObjects();
    }

    @Test
    @DisplayName("주문을 등록할 수 있다.")
    void create_order() throws Exception {
        // given
        orderLineItemObjects.getOrderLineItem1().setOrderId(orderObjects.getOrder1().getId());
        orderLineItemObjects.getOrderLineItem2().setOrderId(orderObjects.getOrder1().getId());
        orderObjects.getOrder1().setOrderLineItems(new ArrayList<>(Arrays.asList(orderLineItemObjects.getOrderLineItem1(), orderLineItemObjects.getOrderLineItem2())));
        when(orderService.create(any(Order.class))).thenReturn(orderObjects.getOrder1());

        // then
        mockMvc.perform(post(DEFAULT_REQUEST_URL)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(new ObjectMapper().writeValueAsString(orderObjects.getOrder2())))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(orderObjects.getOrder1().getId()))
                .andExpect(jsonPath("orderTableId").value(orderObjects.getOrder1().getOrderTableId()))
                .andExpect(jsonPath("orderLineItems.[0].orderId").value(orderObjects.getOrder1().getId()))
        ;
    }

    @Test
    @DisplayName("주문 목록을 조회할 수 있다.")
    void retrieve_orderList() throws Exception {
        // given
        when(orderService.list()).thenReturn(orderObjects.getOrders());

        // then
        mockMvc.perform(get(DEFAULT_REQUEST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").value(orderObjects.getOrder1().getId()))
                .andExpect(jsonPath("[0].orderTableId").value(orderObjects.getOrder1().getOrderTableId()))
                .andExpect(jsonPath("[4].id").value(orderObjects.getOrder5().getId()))
                .andExpect(jsonPath("[4].orderTableId").value(orderObjects.getOrder5().getOrderTableId()))
        ;
    }

    @Test
    @DisplayName("주문을 수정할 수 있다.")
    void change_orderStatus() throws Exception {
        // given
        orderObjects.getOrder2().setOrderStatus(OrderStatus.COMPLETION.name());
        when(orderService.changeOrderStatus(anyLong(), any(Order.class))).thenReturn(orderObjects.getOrder2());

        // then
        mockMvc.perform(put(DEFAULT_REQUEST_URL + "/1/order-status")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(new ObjectMapper().writeValueAsString(orderObjects.getOrder4()))
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(orderObjects.getOrder2().getId()))
                .andExpect(jsonPath("orderStatus").value(orderObjects.getOrder2().getOrderStatus()))
        ;
    }
}
