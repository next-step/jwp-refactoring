package kitchenpos.order.ui;

import kitchenpos.ControllerTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.tablegroup.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("OrderRestController 테스트")
@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest extends ControllerTest {
    @MockBean
    private OrderService orderService;

    private MenuGroup 양식;
    private Menu 양식_세트1;
    private Menu 양식_세트2;
    private OrderTable 주문테이블;
    private Order 주문;
    private List<OrderLineItem> 주문_메뉴_목록;

    @BeforeEach
    public void setUp() {
        super.setUp();

        주문테이블 = new OrderTable(2, false);

        ReflectionTestUtils.setField(주문테이블, "id", 1L);

        주문 = new Order(주문테이블.getId(), OrderStatus.COOKING);

        양식 = new MenuGroup("양식");
        양식_세트1 = new Menu("양식 세트1", new BigDecimal(43000), 양식);
        양식_세트2 = new Menu("양식 세트2", new BigDecimal(50000), 양식);

        ReflectionTestUtils.setField(주문, "id", 1L);
        ReflectionTestUtils.setField(양식, "id", 1L);
        ReflectionTestUtils.setField(양식_세트1, "id", 1L);
        ReflectionTestUtils.setField(양식_세트2, "id", 2L);

        주문_메뉴_목록 = Arrays.asList(new OrderLineItem(주문, 양식_세트1.getId(), 1L),
                new OrderLineItem(주문, 양식_세트2.getId(), 1L));
        주문.order(주문_메뉴_목록);
    }

    @Test
    void 주문_등록() throws Exception {
        OrderRequest request = new OrderRequest(주문테이블.getId(), OrderStatus.COOKING,
                OrderLineItemRequest.list(주문_메뉴_목록));
        given(orderService.create(any(OrderRequest.class))).willReturn(OrderResponse.of(주문));

        webMvc.perform(post("/api/orders")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(주문.getId().intValue())))
                .andExpect(jsonPath("$.orderTableId", is(주문테이블.getId().intValue())))
                .andExpect(jsonPath("$.orderLineItems", hasSize(2)));
    }

    @Test
    void 주문_등록_실패() throws Exception {
        OrderRequest request = new OrderRequest(주문테이블.getId(), OrderStatus.COOKING,
                OrderLineItemRequest.list(주문_메뉴_목록));
        given(orderService.create(any(OrderRequest.class))).willThrow(IllegalArgumentException.class);

        webMvc.perform(post("/api/orders")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 주문_목록_조회() throws Exception {
        given(orderService.list()).willReturn(Arrays.asList(OrderResponse.of(주문)));

        webMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void 주문_상태_변경() throws Exception {
        주문.changeOrderStatus(OrderStatus.MEAL);
        given(orderService.changeOrderStatus(anyLong(), any(OrderStatus.class))).willReturn(OrderResponse.of(주문));

        webMvc.perform(put("/api/orders/" + 주문.getId() + "/order-status?request=" + OrderStatus.COOKING))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(주문.getId().intValue())))
                .andExpect(jsonPath("$.orderStatus", is(주문.getOrderStatus().name())));
    }

    @Test
    void 주문_상태_변경_실패() throws Exception {
        주문.changeOrderStatus(OrderStatus.MEAL);

        given(orderService.changeOrderStatus(anyLong(), any(OrderStatus.class))).willThrow(IllegalArgumentException.class);

        webMvc.perform(put("/api/orders/" + 주문.getId() + "/order-status")
                        .content(mapper.writeValueAsString(OrderStatus.COOKING))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }
}