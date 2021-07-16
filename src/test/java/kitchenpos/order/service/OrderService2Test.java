package kitchenpos.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.domain.entity.Menu;
import kitchenpos.order.domain.entity.Order;
import kitchenpos.order.domain.entity.OrderLineItem;
import kitchenpos.table.domain.entity.OrderTable;
import kitchenpos.order.domain.value.OrderLineItems;
import kitchenpos.order.domain.value.OrderStatus;
import kitchenpos.order.domain.value.Quantity;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderService2Test {

    @Autowired
    OrderService orderService;

    private Menu 메뉴_후라이드_후라이드;
    private OrderLineItem 주문라인아이템;
    private OrderTable 주문테이블;
    private Order 주문;
    private Order 주문_변경;
    private OrderRequest 주문_리퀘스트;
    private OrderLineItemRequest 주문라인아이템_리퀘스트;
    private OrderRequest 주문_변경_리퀘스트;

    @BeforeEach
    void setUp() {
        메뉴_후라이드_후라이드 = new Menu();
        주문라인아이템 = new OrderLineItem(1L, null, 메뉴_후라이드_후라이드, Quantity.of(1L));
        주문라인아이템_리퀘스트 = new OrderLineItemRequest(1L, 1L, 1L, 1L);
        주문테이블 = new OrderTable();
        주문 = new Order(1L, 주문테이블, OrderStatus.MEAL.name(),
            new OrderLineItems(Arrays.asList(주문라인아이템)));
        주문_리퀘스트 = new OrderRequest(100L, 7L, Arrays.asList(주문라인아이템_리퀘스트));
        주문_변경 = new Order(1L, 주문테이블, OrderStatus.COMPLETION.name(),
            new OrderLineItems(Arrays.asList(주문라인아이템)));
        주문_변경_리퀘스트 = new OrderRequest(OrderStatus.COMPLETION.name());
    }

    @Test
    @DisplayName("주문을 생성한다.")
    void create() {
        //when
        OrderResponse createdOrder = orderService.create(주문_리퀘스트);

        //then
        assertThat(createdOrder).isNotNull();
    }

    @Test
    @DisplayName("주문항목이 비어있을 경우 주문 생성을 실패한다.")
    void create_with_exception_when_order_line_items_is_empty() {
        //given
        주문_리퀘스트 = new OrderRequest(100L, 7L, null);

        //when
        assertThatThrownBy(() -> orderService.create(주문_리퀘스트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("저장된 메뉴에 없는 메뉴일 경우 주문 생성을 실패한다.")
    void create_with_exception_when_menu_not_in_saved_menus() {
        //given
        주문라인아이템_리퀘스트 = new OrderLineItemRequest(1L, 1L, 999L, 1L);
        주문_리퀘스트 = new OrderRequest(100L, 4L, Arrays.asList(주문라인아이템_리퀘스트));

        //when && then
        assertThatThrownBy(() -> orderService.create(주문_리퀘스트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 테이블에서 주문할 경우 주문 생성을 실패한다.")
    void create_with_exception_when_table_is_null() {
        //given
        주문_리퀘스트 = new OrderRequest(1L, 1L, Arrays.asList());
        //when && then
        assertThatThrownBy(() -> orderService.create(주문_리퀘스트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("전체 주문을 조회한다.")
    void list() {
        //given
        //when
        List<OrderResponse> foundOrders = orderService.list();

        //then
        assertThat(foundOrders).hasSizeGreaterThan(0);
    }

    @Test
    @DisplayName("주문 상태를 변경한다.")
    void changeOrderStatus() {
        //when
        OrderResponse changedOrder = orderService.changeOrderStatus(주문.getId(), 주문_변경_리퀘스트);

        //then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(주문_변경.getOrderStatus());
    }

    @Test
    @DisplayName("존재하지않는 주문의 경우 주문 상태 변경을 실패한다.")
    void changeOrderStatus_with_exception_when_order_is_null() {
        //when
        assertThatThrownBy(() -> orderService.changeOrderStatus(999L, 주문_리퀘스트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문상태가 계산완료인경우 주문 상태 변경을 실패한다.")
    void changeOrderStatus_with_exception_when_order_status_is_completion() {
        //given
        주문 = new Order(100L, 주문테이블, OrderStatus.COMPLETION.name(),
            new OrderLineItems(Arrays.asList(주문라인아이템)));
        주문_리퀘스트 = new OrderRequest(1L, 주문테이블.getId(), OrderStatus.COMPLETION.name(),
            Arrays.asList(주문라인아이템_리퀘스트));

        //when
        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), 주문_리퀘스트))
            .isInstanceOf(IllegalArgumentException.class);
    }
}