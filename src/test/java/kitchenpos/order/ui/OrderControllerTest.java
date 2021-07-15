package kitchenpos.order.ui;

import kitchenpos.menu.domain.*;
import kitchenpos.order.application.OrderService;
import kitchenpos.common.ControllerTest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.ui.OrderRestController;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

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

    private final MenuGroup 첫번째_메뉴그룹 = new MenuGroup("메뉴그룹");
    private final Product 첫번째_상품 = new Product("첫번째 상품", Price.valueOf(13000));
    private final MenuProduct 첫번째_메뉴상품 = new MenuProduct(첫번째_상품, Quantity.of(1L));
    private final Menu 첫번째_메뉴 = new Menu("첫번째 메뉴", Price.valueOf(13000), 첫번째_메뉴그룹.getId(), Arrays.asList(첫번째_메뉴상품));

    private final OrderTable 첫번째_테이블 = new OrderTable(3, false);
    private final OrderLineItem 주문_항목_첫번째 = new OrderLineItem(첫번째_메뉴.getId(), Quantity.of(3L));
    private Order 첫번째_주문 = new Order(첫번째_테이블.getId(), COOKING, Arrays.asList(주문_항목_첫번째));


    @DisplayName("주문 생성요청")
    @Test
    void 주문_생성요청() throws Exception {
        //Given
        when(orderService.create(any())).thenReturn(OrderResponse.of(첫번째_주문));

        //When
        ResultActions 결과 = postRequest(BASE_URI, OrderRequest.of(첫번째_주문));

        //Then
        생성성공(결과);
    }

    @DisplayName("주문 목록 조회요청")
    @Test
    void 주문_목록_조회요청() throws Exception {
        //Given
        List<Order> 주문_목록 = new ArrayList<>(Arrays.asList(첫번째_주문));
        when(orderService.list()).thenReturn(OrderResponse.ofList(주문_목록));

        //When
        ResultActions 결과 = getRequest(BASE_URI);

        //Then
        조회성공(결과);
    }

    @DisplayName("주문 상태 수정요청")
    @Test
    void 주문_상태_수정요청() throws Exception {
        //Given
        OrderResponse 주문상태_변경_응답 = new OrderResponse(첫번째_주문.getId(),
                첫번째_테이블.getId(),
                "COOKING",
                LocalDateTime.now(),
                Arrays.asList(OrderLineItemResponse.of(주문_항목_첫번째)));
        Long 첫번째_주문_ID = 1L;
        when(orderService.changeOrderStatus(첫번째_주문_ID, OrderStatusRequest.of(MEAL))).thenReturn(주문상태_변경_응답);

        String 수정요청_URI = BASE_URI + "/" + 첫번째_주문_ID + "/order-status";

        //When
        ResultActions 결과 = putStatusRequest(수정요청_URI, OrderStatusRequest.of(MEAL));

        //Then
        수정성공(결과);
    }
}
