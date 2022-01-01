package kitchenpos.ui;

import kitchenpos.application.OrderService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

        MenuProduct 후라이드_메뉴_상품 = MenuProductFixture.of(후라이드치킨.getId(), 2);
        Menu 후라이드_후라이드 = MenuFixture.of(
                "후라이드+후라이드",
                BigDecimal.valueOf(16000),
                두마리치킨,
                MenuProducts.from(Collections.singletonList(후라이드_메뉴_상품)));

        주문_테이블 = OrderTableFixture.of(4, false);
        OrderLineItemRequest 주문_항목_요청 = OrderLineItemRequest.of(후라이드_후라이드.getId(), 1L);
        주문_요청 = OrderRequest.of(주문_테이블.getId(), Arrays.asList(주문_항목_요청));

        주문_항목 = OrderLineItemFixture.of(후라이드_후라이드, 1L);
        주문 = Order.of(1L, OrderLineItems.from(Collections.singletonList(주문_항목)));

        주문_응답 = OrderResponse.from(주문);
    }

    @Test
    void 주문_발생() throws Exception {
        // given
        BDDMockito.given(orderService.create(ArgumentMatchers.any())).willReturn(주문_응답);

        // when
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post(API_ORDER_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(주문_요청)))
                .andDo(MockMvcResultHandlers.print());

        // then
        actions
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderStatus").value("COOKING"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderLineItems.length()").value(1));
    }

    @Test
    void 주문_조회() throws Exception {
        // given
        List<OrderResponse> orders = new ArrayList<>();
        orders.add(OrderResponse.from(주문));

        BDDMockito.given(orderService.list()).willReturn(orders);

        // when
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get(API_ORDER_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(orders)))
                .andDo(MockMvcResultHandlers.print());

        // then
        actions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].orderStatus").value("COOKING"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].orderLineItems.length()").value(1));
    }

    @Test
    void 주문_상태_변경() throws Exception {
        // given
        Order 변경된_주문 = Order.of(1L, OrderLineItems.from(Collections.singletonList(주문_항목)));
        변경된_주문.changeOrderStatus(OrderStatus.MEAL);
        OrderResponse 변경된_주문_응답 = OrderResponse.from(변경된_주문);

        BDDMockito.given(orderService.changeOrderStatus(ArgumentMatchers.any(), ArgumentMatchers.any())).willReturn(변경된_주문_응답);

        // when
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.put(API_ORDER_ROOT + "/" + 1L + "/order-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(주문_요청)))
                .andDo(MockMvcResultHandlers.print());

        // then
        actions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderStatus").value("MEAL"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderLineItems.length()").value(1));
    }

}
