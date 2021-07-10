package kitchenpos.ui;

import kitchenpos.domain.order.OrderStatus;
import kitchenpos.ui.dto.order.OrderLineItemRequest;
import kitchenpos.ui.dto.order.OrderRequest;
import kitchenpos.ui.dto.order.OrderResponse;
import kitchenpos.ui.dto.order.OrderTableRequest;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("주문 통합테스트")
class OrderRestControllerTest extends IntegrationSupport {
    private static final String URI = "/api/orders";

    @DisplayName("주문을 추가한다.")
    @Test
    void create() throws Exception {
        //given
        mockMvc.perform(putAsJson("/api/tables/1/empty", OrderTableRequest.of(false)));

        //when
        ResultActions actions = mockMvc.perform(postAsJson(URI, OrderRequest.of(1L, OrderStatus.COOKING.name(), LocalDateTime.now(), Lists.list(OrderLineItemRequest.of(1L, 1)))));

        //then
        actions.andExpect(status().isCreated());
        //and then
        OrderResponse response = toObject(actions.andReturn(), OrderResponse.class);
        assertThat(response.getId()).isNotNull();
        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(response.getOrderLineItems()).isNotEmpty();
    }

    @DisplayName("주문을 모두 조회한다.")
    @Test
    void list() throws Exception {
        //given
        mockMvc.perform(putAsJson("/api/tables/2/empty", OrderTableRequest.of(false)));
        mockMvc.perform(postAsJson(URI, OrderRequest.of(2L, OrderStatus.COOKING.name(), LocalDateTime.now(), Lists.list(OrderLineItemRequest.of(2L, 2)))));

        //when
        ResultActions actions = mockMvc.perform(get(URI));

        //then
        actions.andExpect(status().isOk());
        //and then
        List<OrderResponse> response = toList(actions.andReturn(), OrderResponse.class);
        assertThat(response).isNotEmpty();
    }

    @DisplayName("특정 주문의 상태를 변경한다.")
    @Test
    void changeOrderStatus() throws Exception {
        //given
        mockMvc.perform(putAsJson("/api/tables/3/empty", OrderTableRequest.of(false)));
        MvcResult mvcResult = mockMvc.perform(postAsJson(URI, OrderRequest.of(3L, OrderStatus.COOKING.name(), LocalDateTime.now(), Lists.list(OrderLineItemRequest.of(3L, 3))))).andReturn();
        OrderResponse orderResponse = toObject(mvcResult, OrderResponse.class);

        //when
        ResultActions actions = mockMvc.perform(putAsJson(URI + "/" + orderResponse.getId() + "/order-status", OrderRequest.of(
                3L,
                OrderStatus.COMPLETION.name(),
                LocalDateTime.now(),
                Lists.list(OrderLineItemRequest.of(3L, 4))
        )));

        //then
        actions.andExpect(status().isOk());
        //and then
        OrderResponse response = toObject(actions.andReturn(), OrderResponse.class);
        assertThat(response.getId()).isEqualTo(orderResponse.getId());
        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }
}
