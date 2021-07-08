package kitchenpos.order;

import kitchenpos.application.OrderService;
import kitchenpos.common.ControllerTest;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.OrderRestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = OrderRestController.class)
public class OrderControllerTest  extends ControllerTest<Order> {

    private static final String BASE_URI = "/api/orders";

    @MockBean
    private OrderService orderService;

    @Autowired
    private OrderRestController orderRestController;

    private Order 첫번째_주문;

    @Override
    protected Object controller() {
        return orderRestController;
    }

    @BeforeEach
    void 사전준비() {
        첫번째_주문 = new Order();
        첫번째_주문.setId(1L);
    }

    @DisplayName("주문 생성요청")
    @Test
    void 주문_생성요청() throws Exception {
        //Given
        when(orderService.create(any())).thenReturn(첫번째_주문);

        //When
        ResultActions 결과 = postRequest(BASE_URI, 첫번째_주문);

        //Then
        생성성공(결과, 첫번째_주문);
    }

    @DisplayName("주문 목록 조회요청")
    @Test
    void 주문_목록_조회요청() throws Exception {
        //Given
        List<Order> 주문_목록 = new ArrayList<>(Arrays.asList(첫번째_주문));
        when(orderService.list()).thenReturn(주문_목록);

        //When
        ResultActions 결과 = getRequest(BASE_URI);

        //Then
        목록_조회성공(결과, 주문_목록);
    }

    @DisplayName("주문 상태 수정요청")
    @Test
    void 주문_상태_수정요청() throws Exception {
        //Given
        Order 주문_리퀘스트 = new Order();
        주문_리퀘스트.setId(첫번째_주문.getId());
        주문_리퀘스트.setOrderStatus(OrderStatus.COOKING.name());
        when(orderService.changeOrderStatus(첫번째_주문.getId(), 주문_리퀘스트)).thenReturn(주문_리퀘스트);

        String 수정요청_URI = BASE_URI + "/" + 첫번째_주문.getId() + "/order-status";

        //When
        ResultActions 결과 = putRequest(수정요청_URI, 주문_리퀘스트);

        //Then
        수정성공(결과, 주문_리퀘스트);
    }
}
