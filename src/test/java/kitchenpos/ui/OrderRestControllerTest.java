package kitchenpos.ui;

import kitchenpos.ControllerTest;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

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
class OrderRestControllerTest extends ControllerTest {

    private static final Long MAIN_MENU_ID = 1L;
    private static final Long SUB_MENU_ID = 2L;

    @MockBean
    private OrderService orderService;

    private OrderTable 주문테이블;
    private Order 주문;
    private OrderLineItem 주문_메뉴1;
    private OrderLineItem 주문_메뉴2;
    private List<OrderLineItem> 주문_메뉴_목록;

    @BeforeEach
    public void setUp() {
        super.setUp();

        주문테이블 = new OrderTable(1L, null, 2, false);
        주문 = new Order(1L, 주문테이블.getId(), null);
        주문_메뉴1 = new OrderLineItem(1L, 주문.getId(), MAIN_MENU_ID, 1);
        주문_메뉴2 = new OrderLineItem(2L, 주문.getId(), SUB_MENU_ID, 1);

        주문_메뉴_목록 = Arrays.asList(주문_메뉴1, 주문_메뉴2);
        주문.setOrderLineItems(주문_메뉴_목록);
    }

    @Test
    void 주문_등록() throws Exception {
        given(orderService.create(any(Order.class))).willReturn(주문);

        webMvc.perform(post("/api/orders")
                    .content(mapper.writeValueAsString(주문))
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(주문.getId().intValue())))
                .andExpect(jsonPath("$.orderTableId", is(주문테이블.getId().intValue())))
                .andExpect(jsonPath("$.orderLineItems", hasSize(2)));
    }

    @Test
    void 주문_등록_실패() throws Exception {
        given(orderService.create(any(Order.class))).willThrow(IllegalArgumentException.class);

        webMvc.perform(post("/api/orders")
                        .content(mapper.writeValueAsString(주문))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 주문_목록_조회() throws Exception {
        given(orderService.list()).willReturn(Arrays.asList(주문));

        webMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void 주문_상태_변경() throws Exception {
        Order 상태_변경_주문 = new Order();
        상태_변경_주문.setId(주문.getId());
        상태_변경_주문.setOrderStatus(OrderStatus.MEAL.name());

        given(orderService.changeOrderStatus(anyLong(), any(Order.class))).willReturn(상태_변경_주문);

        webMvc.perform(put("/api/orders/" + 주문.getId() + "/order-status")
                        .content(mapper.writeValueAsString(상태_변경_주문))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(상태_변경_주문.getId().intValue())))
                .andExpect(jsonPath("$.orderStatus", is(상태_변경_주문.getOrderStatus())));
    }

    @Test
    void 주문_상태_변경_실패() throws Exception {
        Order 상태_변경_주문 = new Order();
        상태_변경_주문.setId(주문.getId());
        상태_변경_주문.setOrderStatus(OrderStatus.MEAL.name());

        given(orderService.changeOrderStatus(anyLong(), any(Order.class))).willThrow(IllegalArgumentException.class);

        webMvc.perform(put("/api/orders/" + 주문.getId() + "/order-status")
                        .content(mapper.writeValueAsString(상태_변경_주문))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }
}
