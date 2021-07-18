package kitchenpos.order.ui;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.dto.*;
import kitchenpos.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static kitchenpos.order.OrderTableTestFixture.*;
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
}
