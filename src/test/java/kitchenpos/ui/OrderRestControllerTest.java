package kitchenpos.ui;

import kitchenpos.application.OrderService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.product.domain.Product;
import kitchenpos.common.fixtrue.MenuFixture;
import kitchenpos.common.fixtrue.MenuGroupFixture;
import kitchenpos.common.fixtrue.MenuProductFixture;
import kitchenpos.common.fixtrue.OrderFixture;
import kitchenpos.common.fixtrue.OrderLineItemFixture;
import kitchenpos.common.fixtrue.OrderTableFixture;
import kitchenpos.common.fixtrue.ProductFixture;
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

    Order 주문;

    @BeforeEach
    void setUp() {
        Product 후라이드치킨 = ProductFixture.of(1L, "후라이드치킨", BigDecimal.valueOf(16000));
        MenuGroup 두마리치킨 = MenuGroupFixture.of(1L, "두마리치킨");
        Menu 후라이드_후라이드 = MenuFixture.of(
                1L,
                "후라이드+후라이드",
                BigDecimal.valueOf(16000),
                두마리치킨.getId(),
                MenuProductFixture.of(1L, 1L, 후라이드치킨.getId(), 2));

        OrderTable 주문_테이블 = OrderTableFixture.of(1L, null, 4, false);
        주문 = OrderFixture.of(
                1L,
                주문_테이블.getId(),
                OrderStatus.COOKING.name(),
                OrderLineItemFixture.of(1L, 1L, 후라이드_후라이드.getId(), 1L));
    }

    @Test
    void 주문_발생() throws Exception {
        // given
        given(orderService.create(any())).willReturn(주문);

        // when
        ResultActions actions = mockMvc.perform(post(API_ORDER_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(주문)))
                .andDo(print());

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.orderTableId").value(1L))
                .andExpect(jsonPath("$.orderStatus").value("COOKING"))
                .andExpect(jsonPath("$.orderLineItems.length()").value(1));
    }

    @Test
    void 주문_조회() throws Exception {
        // given
        List<Order> orders = new ArrayList<>();
        orders.add(주문);

        given(orderService.list()).willReturn(orders);

        // when
        ResultActions actions = mockMvc.perform(get(API_ORDER_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(orders)))
                .andDo(print());

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].orderTableId").value(1L))
                .andExpect(jsonPath("$[0].orderStatus").value("COOKING"))
                .andExpect(jsonPath("$[0].orderLineItems.length()").value(1));
    }

    @Test
    void 주문_상태_변경() throws Exception {
        // given
        주문.setOrderStatus(OrderStatus.MEAL.name());
        given(orderService.changeOrderStatus(any(), any())).willReturn(주문);

        // when
        ResultActions actions = mockMvc.perform(put(API_ORDER_ROOT + "/" + 주문.getId() + "/order-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(주문)))
                .andDo(print());

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.orderTableId").value(1L))
                .andExpect(jsonPath("$.orderStatus").value("MEAL"))
                .andExpect(jsonPath("$.orderLineItems.length()").value(1));
    }

}
