package kitchenpos.order.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderSaveRequest;
import kitchenpos.order.dto.OrderStatusUpdateRequest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static kitchenpos.menugroup.fixtures.MenuGroupFixtures.메뉴그룹;
import static kitchenpos.order.fixtures.OrderFixtures.식사중으로_변경요청;
import static kitchenpos.order.fixtures.OrderFixtures.주문등록요청;
import static kitchenpos.table.fixtures.OrderTableFixtures.주문가능_다섯명테이블;
import static kitchenpos.product.fixtures.ProductFixtures.양념치킨;
import static kitchenpos.product.fixtures.ProductFixtures.후라이드;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * packageName : kitchenpos.ui
 * fileName : OrderRestControllerTest
 * author : haedoang
 * date : 2021-12-15
 * description :
 */
@DisplayName("주문 컨트롤러 테스트")
@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {
    private OrderResponse response;
    private OrderResponse changeOrderStatusResponse;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        BigDecimal 메뉴가격 = new BigDecimal(32000);

        MenuProduct 양념치킨메뉴상품 = new MenuProduct(양념치킨().getId(), 1L);
        MenuProduct 후라이드메뉴상품 = new MenuProduct(후라이드().getId(), 1L);

        Menu 후라이드반양념반메뉴 = new Menu("후라이드반양념반메뉴", 메뉴가격, 메뉴그룹("반반메뉴").getId(), Lists.newArrayList(양념치킨메뉴상품, 후라이드메뉴상품));

        OrderLineItem 주문정보_후라이드양념반두개 = new OrderLineItem(후라이드반양념반메뉴.getId(), 2L);

        Order 후라이드반양념반두개주문 = new Order(주문가능_다섯명테이블().getId(), Lists.newArrayList(주문정보_후라이드양념반두개));
        response = OrderResponse.of(후라이드반양념반두개주문);

        후라이드반양념반두개주문.changeStatus(OrderStatus.COMPLETION);
        changeOrderStatusResponse = OrderResponse.of(후라이드반양념반두개주문);
    }

    @Test
    @DisplayName("주문을 조회한다.")
    public void getOrders() throws Exception {
        // given
        List<OrderResponse> orders = Collections.singletonList(response);
        given(orderService.list()).willReturn(orders);

        // when
        ResultActions actions = mockMvc.perform(
                get("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].orderStatus", is(OrderStatus.COOKING.name())))
                .andExpect(jsonPath("$[0].orderLineItems", hasSize(1)))
                .andDo(print());
    }


    @Test
    @DisplayName("주문을 등록한다.")
    public void postOrder() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();
        given(orderService.create(any(OrderSaveRequest.class))).willReturn(response);

        // when
        ResultActions actions = mockMvc.perform(
                post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(주문등록요청()))
        ).andDo(print());

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.orderStatus", is(OrderStatus.COOKING.name())))
                .andExpect(jsonPath("$.orderLineItems", hasSize(1)))
                .andDo(print());
    }


    @Test
    @DisplayName("주문상태를 변경한다.")
    public void putOrder() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();
        given(orderService.changeOrderStatus(anyLong(), any(OrderStatusUpdateRequest.class))).willReturn(changeOrderStatusResponse);

        // when
        ResultActions actions = mockMvc.perform(
                put("/api/orders/" + 1L + "/order-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(식사중으로_변경요청()))
        ).andDo(print());

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.orderStatus", is(OrderStatus.COMPLETION.name())))
                .andExpect(jsonPath("$.orderLineItems", hasSize(1)))
                .andDo(print());
    }
}
