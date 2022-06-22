package kitchenpos.ui;

import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.OrderFixture.주문_요청_데이터_생성;
import static kitchenpos.fixture.OrderLineItemFixture.주문항목_데이터_생성;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderRestControllerTest extends BaseRestControllerTest {

    @Mock
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new OrderRestController(orderService)).build();
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void create() throws Exception {
        //given
        List<OrderLineItem> orderLineItems = Arrays.asList(주문항목_데이터_생성());
        Order request = 주문_요청_데이터_생성(orderLineItems);
        String requestBody = objectMapper.writeValueAsString(request);

        Order order = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
        given(orderService.create(any())).willReturn(order);

        //when //then
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.orderTableId").value(1L))
                .andExpect(jsonPath("$.orderStatus").value(OrderStatus.COOKING.name()))
                .andExpect(jsonPath("$.orderedTime").isNotEmpty())
                .andExpect(jsonPath("$.orderLineItems").isNotEmpty());
    }

    @DisplayName("주문과 주문항목을 전체 조회한다.")
    @Test
    void list() throws Exception {
        //given
        List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(1L, 1L, 1L, 1));
        Order order = new Order(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), orderLineItems);
        given(orderService.list()).willReturn(Arrays.asList(order));

        //when //then
        mockMvc.perform(get("/api/orders"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @DisplayName("주문상태를 변경한다.")
    @Test
    void changeOrderStatus() throws Exception {
        //given
        Long orderId = 1L;
        Order request = new Order(null, null, OrderStatus.MEAL.name(), null, null);
        String requestBody = objectMapper.writeValueAsString(request);

        List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(1L, 1L, 1L, 1));
        Order order = new Order(orderId, 1L, OrderStatus.MEAL.name(), LocalDateTime.now(), orderLineItems);
        given(orderService.changeOrderStatus(any(), any())).willReturn(order);

        //when //then
        mockMvc.perform(put("/api/orders/{orderId}/order-status", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId))
                .andExpect(jsonPath("$.orderStatus").value(OrderStatus.MEAL.name()));
    }
}