package kitchenpos.order.ui;

import kitchenpos.common.fixtrue.MenuFixture;
import kitchenpos.common.fixtrue.MenuGroupFixture;
import kitchenpos.common.fixtrue.MenuProductFixture;
import kitchenpos.common.fixtrue.OrderLineItemFixture;
import kitchenpos.common.fixtrue.OrderTableFixture;
import kitchenpos.common.fixtrue.ProductFixture;
import kitchenpos.common.ui.RestControllerTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest extends RestControllerTest {

    private static final String API_ORDER_ROOT = "/api/orders";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderService orderService;

    OrderTable 주문_테이블;
    OrderRequest 주문_요청;
    Order 주문;
    OrderLineItem 주문_항목;
    OrderResponse 주문_응답;

    @BeforeEach
    void setUp() {
        Product 후라이드치킨 = ProductFixture.of("후라이드치킨", BigDecimal.valueOf(16000));
        MenuGroup 두마리치킨 = MenuGroupFixture.from("두마리치킨");

        MenuProduct 후라이드_메뉴_상품 = MenuProductFixture.of(후라이드치킨, 2);
        Menu 후라이드_후라이드 = MenuFixture.of(
                "후라이드+후라이드",
                BigDecimal.valueOf(16000),
                두마리치킨,
                MenuProducts.from(Collections.singletonList(후라이드_메뉴_상품)));

        주문_테이블 = OrderTableFixture.of(4, false);
        OrderLineItemRequest 주문_항목_요청 = OrderLineItemRequest.of(후라이드_후라이드.getId(), 1L);
        주문_요청 = OrderRequest.of(주문_테이블.getId(), Arrays.asList(주문_항목_요청));

        주문_항목 = OrderLineItemFixture.of(후라이드_후라이드, 1L);
        주문 = Order.of(주문_테이블, Collections.singletonList(주문_항목));

        주문_응답 = OrderResponse.from(주문);
    }

    @Test
    void 주문_발생() throws Exception {
        // given
        given(orderService.create(any())).willReturn(주문_응답);

        // when
        ResultActions actions = mockMvc.perform(post(API_ORDER_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(주문_요청)))
                .andDo(print());

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderStatus").value("COOKING"))
                .andExpect(jsonPath("$.orderLineItems.length()").value(1));
    }

    @Test
    void 주문_조회() throws Exception {
        // given
        List<OrderResponse> orders = new ArrayList<>();
        orders.add(OrderResponse.from(주문));

        given(orderService.list()).willReturn(orders);

        // when
        ResultActions actions = mockMvc.perform(get(API_ORDER_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(orders)))
                .andDo(print());

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderStatus").value("COOKING"))
                .andExpect(jsonPath("$[0].orderLineItems.length()").value(1));
    }

    @Test
    void 주문_상태_변경() throws Exception {
        // given
        Order 변경된_주문 = Order.of(주문_테이블, Collections.singletonList(주문_항목));
        변경된_주문.changeOrderStatus(OrderStatus.MEAL);
        OrderResponse 변경된_주문_응답 = OrderResponse.from(변경된_주문);

        given(orderService.changeOrderStatus(any(), any())).willReturn(변경된_주문_응답);

        // when
        ResultActions actions = mockMvc.perform(put(API_ORDER_ROOT + "/" + 1L + "/order-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(주문_요청)))
                .andDo(print());

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderStatus").value("MEAL"))
                .andExpect(jsonPath("$.orderLineItems.length()").value(1));
    }

}
