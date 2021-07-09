package kitchenpos.ui;

import kitchenpos.application.command.OrderService;
import kitchenpos.application.query.OrderQueryService;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderCreate;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemCreateRequest;
import kitchenpos.dto.request.OrderStatusChangeRequest;
import kitchenpos.dto.response.OrderViewResponse;
import kitchenpos.fixture.CleanUp;
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

import java.util.Arrays;

import static kitchenpos.dto.response.OrderViewResponse.of;
import static kitchenpos.fixture.OrderFixture.결제완료_음식_1;
import static kitchenpos.fixture.OrderFixture.결제완료_음식_2;
import static kitchenpos.ui.JsonUtil.toJson;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderRestController.class)
@ExtendWith(MockitoExtension.class)
class OrderRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderQueryService orderQueryService;

    @BeforeEach
    void setUp() {
        CleanUp.cleanUp();
    }

    @Test
    void create() throws Exception {
        // given
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(1L, OrderStatus.MEAL, Arrays.asList(new OrderLineItemCreateRequest(1L, 1L)));

        given(orderService.create(any(OrderCreate.class)))
                .willReturn(결제완료_음식_2.getId());
        given(orderQueryService.findById(결제완료_음식_2.getId()))
                .willReturn(of(결제완료_음식_2));

        // when
        mockMvc.perform(
                post("/api/orders")
                        .content(toJson(orderCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(validateOrder("$", 결제완료_음식_2))
                .andExpect(validateOrderLineItem("$.orderLineItems[0]", 결제완료_음식_2.getOrderLineItems().get(0)))
                .andExpect(validateOrderLineItem("$.orderLineItems[1]", 결제완료_음식_2.getOrderLineItems().get(1)));
    }

    @Test
    void list() throws Exception {
        // given
        given(orderQueryService.list()).willReturn(Arrays.asList(of(결제완료_음식_1), of(결제완료_음식_2)));

        // when & then
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(validateOrder("$.[0]", 결제완료_음식_1))
                .andExpect(validateOrderLineItem("$.[0].orderLineItems[0]", 결제완료_음식_1.getOrderLineItems().get(0)))
                .andExpect(validateOrder("$.[1]", 결제완료_음식_2))
                .andExpect(validateOrderLineItem("$.[1].orderLineItems[0]", 결제완료_음식_2.getOrderLineItems().get(0)))
                .andExpect(validateOrderLineItem("$.[1].orderLineItems[1]", 결제완료_음식_2.getOrderLineItems().get(1)));
    }

    @Test
    void changeOrderStatus() throws Exception {
        // given
        OrderStatusChangeRequest orderStatusChangeRequest = new OrderStatusChangeRequest(OrderStatus.COOKING);

        given(orderQueryService.findById(결제완료_음식_2.getId()))
                .willReturn(OrderViewResponse.of(결제완료_음식_2));

        // when & then
        mockMvc.perform(
                put("/api/orders/"+ 결제완료_음식_2.getId() +"/order-status")
                        .content(toJson(orderStatusChangeRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(validateOrder("$", 결제완료_음식_2))
                .andExpect(validateOrderLineItem("$.orderLineItems[0]", 결제완료_음식_2.getOrderLineItems().get(0)))
                .andExpect(validateOrderLineItem("$.orderLineItems[1]", 결제완료_음식_2.getOrderLineItems().get(1)));


    }

    private ResultMatcher validateOrderLineItem(String prefix, OrderLineItem orderLineItem) {
        return result -> {
            ResultMatcher.matchAll(
                    jsonPath(prefix + ".seq").value(orderLineItem.getId()),
                    jsonPath(prefix + ".orderId").value(orderLineItem.getOrderId()),
                    jsonPath(prefix + ".menuId").value(orderLineItem.getMenuId()),
                    jsonPath(prefix + ".quantity").value(orderLineItem.getQuantity().toLong())
            ).match(result);
        };
    }

    private ResultMatcher validateOrder(String prefix, Order order) {
        return result -> {
            ResultMatcher.matchAll(
                    jsonPath(prefix + ".id").value(order.getId()),
                    jsonPath(prefix + ".orderTableId").value(order.getOrderTableId()),
                    jsonPath(prefix + ".orderStatus").value(order.getOrderStatus().toString()),
                    jsonPath(prefix + ".orderedTime").isNotEmpty()
            ).match(result);
        };
    }
}