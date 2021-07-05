package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.ui.OrderRestController;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    OrderService orderService;

    OrderLineItem orderLineItem;

    @BeforeEach
    void setUp() {
        orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setOrderId(1L);
        orderLineItem.setQuantity(10);
        orderLineItem.setSeq(5L);

        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @DisplayName("주문등록 api 테스트")
    @Test
    public void create() throws Exception {
        OrderTable orderTable = new OrderTable(4, false);

        Order order = new Order();
        order.setOrderLineItems(Arrays.asList(orderLineItem));
        order.setOrderTableId(orderTable.getId());
        order.setOrderStatus(OrderStatus.COOKING);
        order.setOrderedTime(LocalDateTime.now());


        String requestBody = objectMapper.writeValueAsString(order);

        OrderResponse responseOrder = new OrderResponse();
        responseOrder.setOrderLineItems(Arrays.asList(OrderLineItemResponse.of(orderLineItem)));
        responseOrder.setOrderTableId(orderTable.getId());
        responseOrder.setOrderStatus(OrderStatus.COOKING);
        responseOrder.setOrderedTime(LocalDateTime.now());
        String responseBody = objectMapper.writeValueAsString(responseOrder);

        when(orderService.create(any())).thenReturn(responseOrder);
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(responseBody))
        ;

    }

    @DisplayName("주문 목록 Api 테스트")
    @Test
    void list() throws Exception {
        OrderResponse order = new OrderResponse();
        order.setOrderLineItems(Arrays.asList(OrderLineItemResponse.of(orderLineItem)));
        order.setOrderTableId(1L);
        order.setOrderStatus(OrderStatus.COOKING);
        order.setOrderedTime(LocalDateTime.now());

        List<OrderResponse> orders = Arrays.asList(order);

        String responseBody = objectMapper.writeValueAsString(orders);

        when(orderService.list()).thenReturn(orders);
        mockMvc.perform(get("/api/orders")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(responseBody))
        ;
    }

    @DisplayName("주문 상태 변경 Api 테스트")
    @Test
    void changeOrderStatus() throws Exception {
        LocalDateTime orderedTime = LocalDateTime.now();

        OrderTable orderTable = new OrderTable(4, false);

        Order order = new Order();
        order.setOrderLineItems(Arrays.asList(orderLineItem));
        order.setOrderTableId(orderTable.getId());
        order.setOrderStatus(OrderStatus.COOKING);
        order.setOrderedTime(orderedTime);

        String requestBody = objectMapper.writeValueAsString(order);

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderLineItems(Arrays.asList(OrderLineItemResponse.of(orderLineItem)));
        orderResponse.setOrderTableId(1L);
        orderResponse.setOrderStatus(OrderStatus.COOKING);
        orderResponse.setOrderedTime(orderedTime);
        String responseBody = objectMapper.writeValueAsString(orderResponse);

        when(orderService.changeOrderStatus(any(), any())).thenReturn(orderResponse);
        mockMvc.perform(put("/api/orders/1/order-status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(responseBody))
        ;
    }
}
