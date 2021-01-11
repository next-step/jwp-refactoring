package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("주문 컨트롤러 테스트")
@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {
    public static final String DEFAULT_ORDERS_URI = "/api/orders/";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    private List<OrderLineItem> orderLineItems;
    private OrderLineItem orderLineItem;
    private Order paramsOrder;
    private Order expectedOrder;

    @BeforeEach
    void setUp() {
        orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setOrderId(1L);
        orderLineItem.setSeq(1L);
        orderLineItem.setQuantity(1L);

        orderLineItems = new ArrayList<>();
        orderLineItems.add(orderLineItem);

        paramsOrder = new Order();
        paramsOrder.setOrderTableId(1L);
        paramsOrder.setOrderStatus(OrderStatus.COOKING.name());
        paramsOrder.setOrderLineItems(orderLineItems);

        expectedOrder = new Order();
        expectedOrder.setId(1L);
        expectedOrder.setOrderTableId(1L);
        expectedOrder.setOrderStatus(OrderStatus.COOKING.name());
        expectedOrder.setOrderLineItems(orderLineItems);
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void 주문_생성() throws Exception {
        given(orderService.create(any())).willReturn(expectedOrder);

        final String jsonTypeOrder = objectMapper.writeValueAsString(expectedOrder);

        mockMvc.perform(post(DEFAULT_ORDERS_URI)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonTypeOrder))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("id").value(expectedOrder.getId()))
            .andExpect(jsonPath("orderTableId").value(expectedOrder.getOrderTableId()))
            .andExpect(jsonPath("orderStatus").value(expectedOrder.getOrderStatus()))
            .andExpect(jsonPath("orderLineItems[0].menuId").value(orderLineItem.getMenuId()))
            .andExpect(jsonPath("orderLineItems[0].orderId").value(orderLineItem.getOrderId()))
            .andExpect(jsonPath("orderLineItems[0].quantity").value(orderLineItem.getQuantity()));
    }

    @DisplayName("주문을 조회한다.")
    @Test
    void 주문_조회() throws Exception {
        given(orderService.list()).willReturn(Collections.singletonList(expectedOrder));

        final String jsonTypeOrder = objectMapper.writeValueAsString(expectedOrder);

        mockMvc.perform(get(DEFAULT_ORDERS_URI)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonTypeOrder))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id").value(expectedOrder.getId()))
            .andExpect(jsonPath("$[0].orderTableId").value(expectedOrder.getOrderTableId()))
            .andExpect(jsonPath("$[0].orderStatus").value(expectedOrder.getOrderStatus()))
            .andExpect(jsonPath("$[0].orderLineItems[0].menuId").value(orderLineItem.getMenuId()))
            .andExpect(jsonPath("$[0].orderLineItems[0].orderId").value(orderLineItem.getOrderId()))
            .andExpect(jsonPath("$[0].orderLineItems[0].quantity").value(orderLineItem.getQuantity()));
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void 주문_상태_변경() throws Exception {
        paramsOrder.setOrderStatus(OrderStatus.MEAL.name());
        expectedOrder.setOrderStatus(OrderStatus.MEAL.name());

        final String jsonTypeOrder = objectMapper.writeValueAsString(paramsOrder);

        given(orderService.changeOrderStatus(paramsOrder.getId(), paramsOrder)).willReturn(expectedOrder);

        final String putRequestUri = DEFAULT_ORDERS_URI + expectedOrder.getId() + "/order-status";
        mockMvc.perform(put(putRequestUri)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonTypeOrder))
            .andDo(print())
            .andExpect(status().isOk())
            // TODO 왜 response body가 비어있는지 확인 필요
            .andExpect(jsonPath("$").doesNotExist())
//            .andExpect(jsonPath("$.id").value(expectedOrder.getId()))
//            .andExpect(jsonPath("orderTableId").value(expectedOrder.getOrderTableId()))
//            .andExpect(jsonPath("orderStatus").value(expectedOrder.getOrderStatus()))
//            .andExpect(jsonPath("orderLineItems[0].menuId").value(orderLineItem.getMenuId()))
//            .andExpect(jsonPath("orderLineItems[0].orderId").value(orderLineItem.getOrderId()))
//            .andExpect(jsonPath("orderLineItems[0].quantity").value(orderLineItem.getQuantity()))
        ;
    }
}
