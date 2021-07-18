package kitchenpos.order.ui;

import kitchenpos.order.Order;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import static kitchenpos.order.OrderStatus.MEAL;
import static kitchenpos.order.OrderTableTestFixture.비어있지_않은_테이블;
import static kitchenpos.order.OrderTableTestFixture.첫번째_주문;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = OrderRestController.class)
public class OrderControllerStatusTest  extends ControllerTest<OrderStatusRequest> {
    private static final String BASE_URI = "/api/orders";

    @MockBean
    private OrderService orderService;

    @Autowired
    private OrderRestController orderRestController;

    @Override
    protected Object controller() {
        return orderRestController;
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
        ResultActions 결과 = putRequest(수정요청_URI, new OrderStatusRequest(MEAL));

        //Then
        수정성공(결과);
    }
}
