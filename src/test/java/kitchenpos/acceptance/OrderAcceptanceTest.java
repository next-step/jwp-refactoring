package kitchenpos.acceptance;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import kitchenpos.BaseAcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class OrderAcceptanceTest extends BaseAcceptanceTest {

    MenuGroup 후라이드치킨_메뉴그룹 = new MenuGroup(1L, "후라이드치킨");
    Product 후라이드치킨_상품 = new Product(1L, "후라이드치킨", new BigDecimal(16000.00));
    MenuProduct 후라이드치킨_메뉴상품 = new MenuProduct(1L, 1L, 1L, 1);
    Menu 후라이드치킨 = new Menu(1L, "후라이드치킨", new BigDecimal(16000.00), 1L, Collections.singletonList(후라이드치킨_메뉴상품));
    OrderTable 빈_주문_테이블 = new OrderTable(null, 1L, 1, true);
    OrderTable 주문_테이블 = new OrderTable(null, 1L, 1, false);

    @Test
    void 수량이_남은_메뉴만_주문할_수_있다() throws Exception {
        Order 수량이_남지_않은_메뉴 = new Order();

        ResultActions resultActions = 주문_등록(수량이_남지_않은_메뉴);

        주문_등록_실패(resultActions);
    }

    @Test
    void 등록_된_메뉴만_지정할_수_있다() throws Exception {
        Order 없는_메뉴가_포함된_주문 = new Order(null, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(),
                Collections.singletonList(new OrderLineItem(1L, 1L, 1L, 1)));

        ResultActions resultActions = 주문_등록(없는_메뉴가_포함된_주문);

        주문_등록_실패(resultActions);
    }

    @Test
    void 등록_된_주문_테이블만_지정할_수_있다() throws Exception {
        메뉴_등록(후라이드치킨);
        Order 등록_되지_않은_주문테이블_지정 = new Order(null, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(),
                Collections.singletonList(new OrderLineItem(1L, 1L, 1L, 1)));

        ResultActions resultActions = 주문_등록(등록_되지_않은_주문테이블_지정);

        주문_등록_실패(resultActions);
    }

    @Test
    void 주문_테이블은_비어있으면_안된다() throws Exception {
        메뉴_등록(후라이드치킨);
        주문_테이블_등록(빈_주문_테이블);
        Order 빈_주문_테이블_지정 = new Order(null, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(),
                Collections.singletonList(new OrderLineItem(1L, 1L, 1L, 1)));

        ResultActions resultActions = 주문_등록(빈_주문_테이블_지정);

        주문_등록_실패(resultActions);
    }

    @Test
    void 주문을_등록할_수_있다() throws Exception {
        메뉴그룹_등록(후라이드치킨_메뉴그룹);
        상품_등록(후라이드치킨_상품);
        메뉴_등록(후라이드치킨);
        주문_테이블_등록(주문_테이블);
        Order 주문 = new Order(null, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(),
                Collections.singletonList(new OrderLineItem(1L, 1L, 1L, 1)));

        ResultActions resultActions = 주문_등록(주문);

        주문_등록_성공(resultActions, 주문);
    }

    @Test
    void 주문_목록을_조회할_수_있다() throws Exception {
        메뉴그룹_등록(후라이드치킨_메뉴그룹);
        상품_등록(후라이드치킨_상품);
        메뉴_등록(후라이드치킨);
        주문_테이블_등록(주문_테이블);
        Order 주문 = new Order(null, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(),
                Collections.singletonList(new OrderLineItem(1L, 1L, 1L, 1)));
        주문_등록(주문);

        ResultActions resultActions = 주문_목록_조회();

        주문_목록_조회_성공(resultActions, 주문);
    }

    @Test
    void 등록_된_주문의_상태만_변경할_수_있다() throws Exception {
        Order 등록되지_않은_주문 = new Order(null, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(),
                Collections.singletonList(new OrderLineItem(1L, 1L, 1L, 1)));

        ResultActions resultActions = 주문_상태를_변경(등록되지_않은_주문);

        주문_상태_변경_실패(resultActions);
    }


    @Test
    void 이미_완료된_주문의_상태는_변경할_수_없다() throws Exception {
        Order 이미_완료된_주문 = 이미_완료된_주문();

        ResultActions resultActions = 주문_상태를_변경(이미_완료된_주문);

        주문_상태_변경_실패(resultActions);
    }

    @Test
    void 주문_상태를_변경할_수_있다() throws Exception {
        Order 주문 = 주문이_등록되어_있다();

        ResultActions resultActions = 주문_상태를_변경(주문);

        주문_상태_변경_성공(resultActions);
    }

    private void 주문_상태_변경_성공(ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().isOk());
    }

    private Order 주문이_등록되어_있다() throws Exception {
        메뉴그룹_등록(후라이드치킨_메뉴그룹);
        상품_등록(후라이드치킨_상품);
        메뉴_등록(후라이드치킨);
        주문_테이블_등록(주문_테이블);
        Order 주문 = new Order(null, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(),
                Collections.singletonList(new OrderLineItem(1L, 1L, 1L, 1)));
        주문_등록(주문);
        return 주문;
    }

    private Order 이미_완료된_주문() throws Exception {
        Order 주문 = 주문이_등록되어_있다();
        주문_상태를_변경(주문);
        return 주문;
    }

    private void 주문_상태_변경_실패(ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().is4xxClientError());
    }

    private ResultActions 주문_상태를_변경(Order order) throws Exception {
        return mvc.perform(put("/api/orders/{orderId}/order-status", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private void 주문_목록_조회_성공(ResultActions resultActions, Order order) throws Exception {
        OrderLineItem orderLineItems = order.getOrderLineItems().get(0);
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].orderTableId").value(order.getOrderTableId()))
                .andExpect(jsonPath("$.[0].orderStatus").value(order.getOrderStatus()))
                .andExpect(jsonPath("$.[0].orderLineItems[0].seq").value(orderLineItems.getSeq()))
                .andExpect(jsonPath("$.[0].orderLineItems[0].orderId").value(orderLineItems.getOrderId()))
                .andExpect(jsonPath("$.[0].orderLineItems[0].menuId").value(orderLineItems.getMenuId()))
                .andExpect(jsonPath("$.[0].orderLineItems[0].quantity").value(orderLineItems.getQuantity()));
    }

    private ResultActions 주문_목록_조회() throws Exception {
        return mvc.perform(get("/api/orders")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions 주문_테이블_등록(OrderTable orderTable) throws Exception {
        return mvc.perform(post("/api/tables")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderTable))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions 메뉴_등록(Menu menu) throws Exception {
        return mvc.perform(post("/api/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menu))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private void 주문_등록_성공(ResultActions resultActions, Order 주문) throws Exception {
        OrderLineItem orderLineItems = 주문.getOrderLineItems().get(0);
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("orderTableId").value(주문.getOrderTableId()))
                .andExpect(jsonPath("orderStatus").value(주문.getOrderStatus()))
                .andExpect(jsonPath("$.orderLineItems[0].seq").value(orderLineItems.getSeq()))
                .andExpect(jsonPath("$.orderLineItems[0].orderId").value(orderLineItems.getOrderId()))
                .andExpect(jsonPath("$.orderLineItems[0].menuId").value(orderLineItems.getMenuId()))
                .andExpect(jsonPath("$.orderLineItems[0].quantity").value(orderLineItems.getQuantity()));
    }

    private void 주문_등록_실패(ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().is4xxClientError());
    }

    private ResultActions 주문_등록(Order order) throws Exception {
        return mvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions 메뉴그룹_등록(MenuGroup menuGroup) throws Exception {
        return mvc.perform(post("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuGroup))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions 상품_등록(Product product) throws Exception {
        return mvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}
