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

import static kitchenpos.order.domain.OrderStatus.MEAL;
import static kitchenpos.ordertable.OrderTableTestFixture.*;
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


    @DisplayName("주문 생성요청")
    @Test
    void 주문_생성요청() throws Exception {
        //Given
        when(orderService.create(any())).thenReturn(첫번째_주문_응답);

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
        Order 변경된_주문 = new Order(비어있지_않은_테이블.getId(), MEAL);
        OrderResponse 주문상태_변경_응답 = new OrderResponse(변경된_주문.getId(),
                변경된_주문.getOrderTableId(),
                변경된_주문.getOrderStatus().name(),
                변경된_주문.getOrderedTime());

        when(orderService.changeOrderStatus(첫번째_주문.getId(), new OrderStatusRequest(MEAL))).thenReturn(주문상태_변경_응답);

        String 수정요청_URI = BASE_URI + "/" + 첫번째_주문.getId() + "/order-status";

        //When
        ResultActions 결과 = putStatusRequest(수정요청_URI, new OrderStatusRequest(MEAL));

        //Then
        수정성공(결과);
    }
}
