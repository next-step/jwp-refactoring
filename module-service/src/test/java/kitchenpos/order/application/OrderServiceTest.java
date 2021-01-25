package kitchenpos.order.application;

import kitchenpos.BaseServiceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class OrderServiceTest extends BaseServiceTest {

    @Autowired
    private OrderService orderService;

    private OrderLineItemRequest orderLineItemRequest1;
    private OrderLineItemRequest orderLineItemRequest2;

    private List<OrderLineItemRequest> orderLineItemRequests;

    private OrderTable 비어있지_않은_테이블;
    private OrderTable 빈테이블;

    private Menu 메뉴_후라이드;
    private Menu 메뉴_양념;

    public Product 상품_후라이드;
    public Product 상품_양념;

    public MenuProduct 메뉴상품_후라이드;
    public MenuProduct 메뉴상품_양념;

    private Order 주문_테이블_조리중;
    private Order 주문_테이블_계산완료;

    private OrderRequest ORDER_STATUS_CHANGE_REQUEST = new OrderRequest(OrderStatus.MEAL.name());

    @BeforeEach
    public void setUp() {
        super.setUp();

        빈테이블 = new OrderTable(1L, null, 0, true);
        비어있지_않은_테이블 = new OrderTable(9L, null,2, false);

        MenuGroup 메뉴그룹_한마리메뉴 = new MenuGroup(2L, "한마리메뉴");

        상품_후라이드 = new Product("후라이드", BigDecimal.valueOf(16000));
        상품_양념 = new Product("양념치킨", BigDecimal.valueOf(16000));

        메뉴상품_후라이드 = new MenuProduct(상품_후라이드, 1);
        메뉴상품_양념 = new MenuProduct(상품_양념, 1);

        메뉴_후라이드 = new Menu.Builder()
                .id(1L)
                .name("후라이드")
                .price(BigDecimal.valueOf(16000))
                .menuGroup(메뉴그룹_한마리메뉴)
                .menuProducts(Arrays.asList(메뉴상품_후라이드))
                .build();

        메뉴_양념 = new Menu.Builder()
                .id(2L)
                .name("양념")
                .price(BigDecimal.valueOf(16000))
                .menuGroup(메뉴그룹_한마리메뉴)
                .menuProducts(Arrays.asList(메뉴상품_양념))
                .build();

        orderLineItemRequest1 = new OrderLineItemRequest(메뉴_후라이드.getId(), 1);
        orderLineItemRequest2 = new OrderLineItemRequest(메뉴_양념.getId(), 1);
        orderLineItemRequests = Arrays.asList(orderLineItemRequest1, orderLineItemRequest2);

        OrderTable 빈테이블_1 = new OrderTable(1L, null,0, true);
        OrderTable 빈테이블_2 = new OrderTable(2L, null,0, true);

        TableGroup 그룹_테이블 = new TableGroup(1L, new OrderTables(Arrays.asList(빈테이블_1, 빈테이블_2)));
        OrderTable 그룹_지정된_테이블_10 = new OrderTable(10L, 그룹_테이블.getId(), 0, false);
        OrderTable 그룹_지정되지_않은_테이블_11 = new OrderTable(11L,5, false);

        OrderLineItem 주문_아이템_후라이드_1개 = new OrderLineItem(1L, 메뉴_후라이드.getId(), 1);
        OrderLineItem 주문_아이템_양념_1개 = new OrderLineItem(2L, 메뉴_양념.getId(), 1);

        주문_테이블_조리중 = new Order.Builder()
                .id(1L)
                .orderTable(그룹_지정된_테이블_10)
                .orderLineItems(Arrays.asList(주문_아이템_후라이드_1개, 주문_아이템_양념_1개))
                .orderedTime(LocalDateTime.of(2021, 1, 20, 03, 30))
                .build();

        주문_테이블_계산완료 = new Order.Builder()
                .id(2L)
                .orderTable(그룹_지정되지_않은_테이블_11)
                .orderLineItems(Arrays.asList(주문_아이템_후라이드_1개, 주문_아이템_양념_1개))
                .orderedTime(LocalDateTime.of(2021, 1, 20, 03, 30))
                .orderStatus(OrderStatus.COMPLETION)
                .build();
    }

    @Test
    @DisplayName("주문을 등록할 수 있다.")
    void create() {
        // given
        OrderRequest orderRequest = new OrderRequest(비어있지_않은_테이블.getId(), orderLineItemRequests);

        // when
        OrderResponse result = orderService.create(orderRequest);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(result.getOrderedTime()).isNotNull();
        assertThat(result.getOrderTableId()).isEqualTo(비어있지_않은_테이블.getId());
        assertThat(result.getOrderLineItems().size()).isEqualTo(2);
        assertThat(result.getOrderLineItems().get(0).getOrderId()).isEqualTo(result.getId());
        assertThat(result.getOrderLineItems().get(0).getMenuId()).isEqualTo(메뉴_후라이드.getId());
        assertThat(result.getOrderLineItems().get(1).getOrderId()).isEqualTo(result.getId());
        assertThat(result.getOrderLineItems().get(1).getMenuId()).isEqualTo(메뉴_양념.getId());
    }

    @DisplayName("하나 이상의 주문 항목을 가져야 한다.")
    @Test
    void requireOneMoreLineItem() {
        //given
        OrderRequest emptyItemOrder = new OrderRequest(비어있지_않은_테이블.getId(), new ArrayList<>());

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            OrderResponse orderResponse = orderService.create(emptyItemOrder);
        }).withMessageMatching("주문은 1개 이상의 메뉴가 포함되어 있어야 합니다.");
    }

    @DisplayName("주문 테이블 상태가 비어있음인 경우 생성할 수 없다.")
    @Test
    void notCreateStatusIsEmpty() {
        // given
        OrderRequest emptyStatusOrder = new OrderRequest(빈테이블.getId(), orderLineItemRequests);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            OrderResponse orderResponse = orderService.create(emptyStatusOrder);
        }).withMessageMatching("빈 테이블은 주문할 수 없습니다.");
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void findAllOrders() {
        // when
        List<OrderResponse> results = orderService.findAll();

        // then
        assertThat(results).isNotEmpty();
        assertThat(results.get(0).getOrderLineItems()).isNotEmpty();
        assertThat(results.get(0).getOrderLineItems().get(0).getSeq()).isNotNull();
    }

    @Test
    @DisplayName("주문 상태를 변경할 수 있다.")
    void changeOrderStatus() {
        //when
        OrderResponse result = orderService.changeOrderStatus(주문_테이블_조리중.getId(), ORDER_STATUS_CHANGE_REQUEST);

        //then
        assertThat(result.getId()).isEqualTo(주문_테이블_조리중.getId());
        assertThat(result.getOrderStatus()).isEqualTo(ORDER_STATUS_CHANGE_REQUEST.getOrderStatus());
    }

    @DisplayName("주문 상태가 완료인 경우 변경할 수 없다.")
    @Test
    void notChangeStatusIsComplete() {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            OrderResponse result = orderService.changeOrderStatus(주문_테이블_계산완료.getId(), ORDER_STATUS_CHANGE_REQUEST);
        }).withMessageMatching("주문 완료 시 상태를 변경할 수 없습니다.");
    }
}
