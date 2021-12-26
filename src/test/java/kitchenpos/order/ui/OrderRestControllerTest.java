package kitchenpos.order.ui;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.common.CommonTestFixtures;
import kitchenpos.common.vo.Price;
import kitchenpos.common.vo.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.testfixtures.OrderTestFixtures;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.vo.NumberOfGuests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {

    private static final String BASE_PATH = "/api/orders";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    private Menu 혼술세트;
    private Menu 이달의메뉴;
    private OrderTable 테이블1번;

    @BeforeEach
    void setUp() {
        MenuGroup 신상메뉴그룹 = new MenuGroup("신상메뉴그룹");
        테이블1번 = new OrderTable(1L, new NumberOfGuests(5), false);
        혼술세트 = new Menu(1L, "혼술세트", Price.valueOf(BigDecimal.ZERO), 신상메뉴그룹);
        이달의메뉴 = new Menu(2L, "이달의메뉴", Price.valueOf(BigDecimal.ZERO), 신상메뉴그룹);
    }

    @DisplayName("주문 등록")
    @Test
    void create() throws Exception {
        //given
        List<OrderLineItem> orderLineItems = Arrays.asList(
            new OrderLineItem(혼술세트.getId(), new Quantity(1L)),
            new OrderLineItem(이달의메뉴.getId(), new Quantity(3L)));
        OrderRequest requestOrder = OrderTestFixtures.convertToOrderRequest(
            new Order(테이블1번.getId(), orderLineItems));
        OrderResponse expectedOrder = OrderResponse.from(
            new Order(1L, 테이블1번.getId(), OrderStatus.COOKING,
                orderLineItems));
        given(orderService.create(any()))
            .willReturn(expectedOrder);

        //when, then
        mockMvc.perform(post(BASE_PATH)
                .content(CommonTestFixtures.asJsonString(requestOrder))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(expectedOrder.getId()))
            .andExpect(jsonPath("$.orderTableId").value(expectedOrder.getOrderTableId()));
    }

    @DisplayName("주문 목록 조회")
    @Test
    void list() throws Exception {
        //given
        List<OrderLineItem> orderLineItems1 = Arrays.asList(
            new OrderLineItem(혼술세트.getId(), new Quantity(1L)),
            new OrderLineItem(이달의메뉴.getId(), new Quantity(3L)));

        List<OrderLineItem> orderLineItems2 = Arrays.asList(
            new OrderLineItem(혼술세트.getId(), new Quantity(2L)),
            new OrderLineItem(이달의메뉴.getId(), new Quantity(2L)));

        List<OrderResponse> expectedOrders = OrderResponse.fromList(Arrays.asList(
            new Order(1L, 테이블1번.getId(), OrderStatus.MEAL, orderLineItems1),
            new Order(2L, 테이블1번.getId(), OrderStatus.COOKING, orderLineItems2)));

        given(orderService.list())
            .willReturn(expectedOrders);

        //when, then
        mockMvc.perform(get(BASE_PATH))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[*]['id']",
                containsInAnyOrder(
                    expectedOrders.stream()
                        .mapToInt(order -> order.getId().intValue())
                        .boxed()
                        .toArray(Integer[]::new))));
    }

    @DisplayName("주문_상태_변경")
    @Test
    void changeOrderStatus() throws Exception {
        //given
        List<OrderLineItem> orderLineItems = Arrays.asList(
            new OrderLineItem(혼술세트.getId(), new Quantity(1L)),
            new OrderLineItem(이달의메뉴.getId(), new Quantity(3L)));
        OrderStatus changeOrderStatus = OrderStatus.MEAL;
        OrderRequest requestOrder = OrderTestFixtures.convertToChangeOrderStatusRequest(
            changeOrderStatus);

        OrderResponse expectedOrder = OrderResponse.from(
            new Order(1L, 테이블1번.getId(), changeOrderStatus, orderLineItems));
        given(orderService.changeOrderStatus(any(), any()))
            .willReturn(expectedOrder);

        //when, then
        mockMvc.perform(
                put(BASE_PATH + "/{orderId}/order-status", expectedOrder.getId())
                    .content(CommonTestFixtures.asJsonString(requestOrder))
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(expectedOrder.getId()))
            .andExpect(jsonPath("$.orderStatus").value(expectedOrder.getOrderStatus().name()));
    }
}
