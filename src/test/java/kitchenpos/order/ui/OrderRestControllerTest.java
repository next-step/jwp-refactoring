package kitchenpos.order.ui;

import kitchenpos.ControllerTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.Product;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("OrderRestController 테스트")
@WebMvcTest(OrderRestController.class)
public class OrderRestControllerTest extends ControllerTest {

    @MockBean
    private OrderService orderService;

    private OrderTable 후라이드치킨_주문_테이블;
    private OrderTable 양념치킨_주문_테이블;
    private Order 후라이드치킨_주문;
    private Order 양념치킨_주문;
    private Order 콜라_주문;
    private OrderLineItem 후라이드치킨_주문_항목;
    private OrderLineItem 콜라_주문_항목;
    private List<OrderLineItem> 주문_메뉴_목록;

    @BeforeEach
    public void setUp() {
        MenuGroup 한마리메뉴_메뉴그룹 = new MenuGroup("한마리메뉴");
        Product 후라이드치킨_상품 = new Product("후라이드치킨", 16000);
        Product 콜라_상품 = new Product("콜라", 2000);
        Menu 후라이드치킨_메뉴 = new Menu("후라이드치킨", 후라이드치킨_상품.getPrice().value().intValue(), 한마리메뉴_메뉴그룹);
        Menu 콜라_메뉴 = new Menu("콜라", 콜라_상품.getPrice().value().intValue(), 한마리메뉴_메뉴그룹);

        후라이드치킨_주문_테이블 = new OrderTable(4, false);
        양념치킨_주문_테이블 = new OrderTable(2, false);

        후라이드치킨_주문 = new Order(후라이드치킨_주문_테이블, OrderStatus.COOKING);
        양념치킨_주문 = new Order(양념치킨_주문_테이블, OrderStatus.COOKING);
        콜라_주문 = new Order(후라이드치킨_주문_테이블, OrderStatus.COOKING);

        후라이드치킨_주문_항목 = new OrderLineItem(후라이드치킨_주문, 후라이드치킨_메뉴, 1);
        콜라_주문_항목 = new OrderLineItem(콜라_주문, 콜라_메뉴, 1);

        ReflectionTestUtils.setField(후라이드치킨_주문_테이블, "id", 1L);
        ReflectionTestUtils.setField(양념치킨_주문_테이블, "id", 2L);

        ReflectionTestUtils.setField(후라이드치킨_주문, "id", 1L);
        ReflectionTestUtils.setField(양념치킨_주문, "id", 2L);

        주문_메뉴_목록 = Arrays.asList(후라이드치킨_주문_항목, 콜라_주문_항목);
        후라이드치킨_주문.order(주문_메뉴_목록);
    }

    @DisplayName("주문 등록에 실패한다.")
    @Test
    void 주문_등록에_실패한다() throws Exception {
        given(orderService.create(any(OrderRequest.class))).willThrow(IllegalArgumentException.class);

        webMvc.perform(post("/api/orders")
                        .content(objectMapper.writeValueAsString(new OrderRequest(후라이드치킨_주문_테이블.getId(), OrderStatus.COOKING, OrderLineItemRequest.from(주문_메뉴_목록))))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("주문 등록에 성공한다.")
    @Test
    void 주문_등록에_성공한다() throws Exception {
        given(orderService.create(any(OrderRequest.class))).willReturn(new OrderResponse(후라이드치킨_주문));

        webMvc.perform(post("/api/orders")
                        .content(objectMapper.writeValueAsString(new OrderRequest(후라이드치킨_주문_테이블.getId(), OrderStatus.COOKING, OrderLineItemRequest.from(주문_메뉴_목록))))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(후라이드치킨_주문.getId().intValue())))
                .andExpect(jsonPath("$.orderTableId", is(후라이드치킨_주문_테이블.getId().intValue())))
                .andExpect(jsonPath("$.orderLineItems", hasSize(2)));
    }

    @DisplayName("주문 목록 조회에 성공한다.")
    @Test
    void 주문_목록_조회에_성공한다() throws Exception {
        given(orderService.list()).willReturn(Arrays.asList(new OrderResponse(후라이드치킨_주문), new OrderResponse(양념치킨_주문)));

        webMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @DisplayName("주문 상태 변경에 실패한다.")
    @Test
    void 주문_상태_변경에_실패한다() throws Exception {
        given(orderService.changeOrderStatus(anyLong(), any(OrderStatus.class))).willThrow(IllegalArgumentException.class);

        webMvc.perform(put("/api/orders/" + 후라이드치킨_주문.getId() + "/order-status?request=" + OrderStatus.COOKING)
                        .content(objectMapper.writeValueAsString(OrderStatus.COOKING))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("주문 상태 변경에 성공한다.")
    @Test
    void 주문_상태_변경에_성공한다() throws Exception {
        후라이드치킨_주문.updateOrderStatus(OrderStatus.MEAL);
        given(orderService.changeOrderStatus(anyLong(), any(OrderStatus.class))).willReturn(new OrderResponse(후라이드치킨_주문));

        webMvc.perform(put("/api/orders/" + 후라이드치킨_주문.getId() + "/order-status?request=" + OrderStatus.COOKING))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(후라이드치킨_주문.getId().intValue())))
                .andExpect(jsonPath("$.orderStatus", is(후라이드치킨_주문.getOrderStatus().name())));
    }
}
