package kitchenpos.ui.order;

import com.fasterxml.jackson.core.type.TypeReference;
import kitchenpos.MockMvcTestHelper;
import kitchenpos.application.order.OrderService;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.order.OrderStatusRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest extends MockMvcTestHelper {

    @MockBean
    private OrderService orderService;

    @Autowired
    private OrderRestController orderRestController;


    @Override
    protected Object controller() {
        return orderRestController;
    }

    @DisplayName("주문 생성 요청")
    @Test
    void createTest() throws Exception {
        // given
        OrderRequest orderRequest = new OrderRequest(1L, Arrays.asList(new OrderLineItemRequest()));

        OrderResponse orderResponse = new OrderResponse();
        Mockito.when(orderService.create(any())).thenReturn(orderResponse);

        // when
        ResultActions resultActions = 주문_생성_요청(orderRequest);

        // then
        주문_생성_성공(resultActions);
    }

    @DisplayName("전체 주문 조회 요청")
    @Test
    void listTest() throws Exception {
        // given
        Mockito.when(orderService.list()).thenReturn(Arrays.asList(new OrderResponse(),
                                                                   new OrderResponse()));

        // when
        ResultActions resultActions = 전체_주문_조회_요청();

        // then
        MvcResult mvcResult = 주문_조회_성공(resultActions);
        List<OrderResponse> orderResponses = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                                                                    new TypeReference<List<OrderResponse>>(){});
        assertThat(orderResponses).isNotEmpty().hasSize(2);
    }

    @DisplayName("주문 상태 변경 요청")
    @Test
    void changeOrderStatusTest() throws Exception {
        // given
        Mockito.when(orderService.changeOrderStatus(any(), any())).thenReturn(new OrderResponse());

        // when
        ResultActions resultActions = 주문_상태_변경_요청(1L, new OrderStatusRequest(OrderStatus.COOKING));

        // then
        주문_상태_변경_성공(resultActions);
    }

    private ResultActions 주문_생성_요청(final OrderRequest orderRequest) throws Exception {
        return postRequest("/api/orders", orderRequest);
    }

    private MvcResult 주문_생성_성공(final ResultActions resultActions) throws Exception {
        return resultActions.andExpect(status().isCreated()).andReturn();
    }

    private ResultActions 전체_주문_조회_요청() throws Exception {
        return getRequest("/api/orders");
    }

    private MvcResult 주문_조회_성공(final ResultActions resultActions) throws Exception {
        return resultActions.andExpect(status().isOk()).andReturn();
    }

    private ResultActions 주문_상태_변경_요청(final Long orderId, final OrderStatusRequest orderStatusRequest) throws Exception {
        String uri = String.format("/api/orders/%s/order-status", orderId);
        return putRequest(uri, orderStatusRequest);
    }

    private MvcResult 주문_상태_변경_성공(final ResultActions resultActions) throws Exception {
        return resultActions.andExpect(status().isOk()).andReturn();
    }
}
