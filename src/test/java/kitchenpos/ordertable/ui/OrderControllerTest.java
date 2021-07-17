package kitchenpos.ordertable.ui;

import kitchenpos.order.application.OrderService;
import kitchenpos.common.ui.ControllerTest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.*;
import kitchenpos.order.ui.OrderRestController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = OrderRestController.class)
public class OrderControllerTest extends ControllerTest<OrderRequest> {

    private static final String BASE_URI = "/api/orders";

    @MockBean
    private OrderService orderService;

    @Autowired
    private OrderRestController orderRestController;

    @Override
    protected Object controller() {
        return orderRestController;
    }

    private final Long 상품_ID = 1L;
    private final Long 메뉴_ID = 1L;
    private final Long 메뉴_두번째_ID = 2L;
    private final Long 테이블_ID = 1L;
    private final Long 주문_ID = 1L;

    private final OrderLineItemResponse 주문_항목_첫번째_응답
            = new OrderLineItemResponse(1L, 주문_ID, 메뉴_ID,1L);
    private final OrderLineItemResponse 주문_항목_두번째_응답
            = new OrderLineItemResponse(2L, 주문_ID, 메뉴_두번째_ID,1L);
    private final Order 첫번째_주문 = new Order(테이블_ID, COOKING);
    private final Order 두번째_주문 = new Order(테이블_ID, COOKING);
    private final OrderResponse 첫번째_주문_응답 = OrderResponse.of(첫번째_주문, Arrays.asList(주문_항목_첫번째_응답));
    private final OrderResponse 두번째_주문_응답 = OrderResponse.of(두번째_주문, Arrays.asList(주문_항목_두번째_응답));

    private final OrderLineItemRequest 주문_항목_첫번째_요청 = new OrderLineItemRequest(메뉴_ID, 1L);
    private final OrderRequest 첫번째_주문_요청 = new OrderRequest(테이블_ID, Arrays.asList(주문_항목_첫번째_요청));
    private final OrderRequest 두번째_주문_요청 = new OrderRequest(테이블_ID, Arrays.asList(주문_항목_첫번째_요청));


    @DisplayName("주문 생성요청")
    @Test
    void 주문_생성요청() throws Exception {
        //Given
        when(orderService.create(any())).thenReturn(OrderResponse.of(첫번째_주문, Arrays.asList(주문_항목_첫번째_응답)));

        //When
        ResultActions 결과 = postRequest(BASE_URI, 첫번째_주문_요청);

        //Then
        생성성공(결과);
    }

    @DisplayName("주문 목록 조회요청")
    @Test
    void 주문_목록_조회요청() throws Exception {
        //Given
        when(orderService.list()).thenReturn(Arrays.asList(첫번째_주문_응답, 두번째_주문_응답));

        //When
        ResultActions 결과 = getRequest(BASE_URI);

        //Then
        조회성공(결과);
    }

    @DisplayName("주문 상태 수정요청")
    @Test
    void 주문_상태_수정요청() throws Exception {
        //Given
        Order 변경된_주문 = new Order(테이블_ID, MEAL);
        OrderResponse 주문상태_변경_응답 = new OrderResponse(변경된_주문);

        when(orderService.changeOrderStatus(주문_ID, OrderStatusRequest.of(MEAL))).thenReturn(주문상태_변경_응답);

        String 수정요청_URI = BASE_URI + "/" + 주문_ID + "/order-status";

        //When
        ResultActions 결과 = putStatusRequest(수정요청_URI, OrderStatusRequest.of(MEAL));

        //Then
        수정성공(결과);
    }
}
