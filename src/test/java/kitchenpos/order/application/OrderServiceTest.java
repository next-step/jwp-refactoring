package kitchenpos.order.application;

import kitchenpos.menu.domain.*;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.*;
import kitchenpos.order.exception.BadOrderRequestException;
import kitchenpos.order.exception.NotChangeOrderStatusException;
import kitchenpos.order.exception.NotCreateOrderException;
import kitchenpos.order.exception.NotFoundOrderException;
import kitchenpos.table.domain.FakeOrderTableRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.exception.NotFoundOrderTableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;

@DisplayName("주문 테스트")
class OrderServiceTest {

    private final MenuRepository menuRepository = new FakeMenuRepository();
    private final OrderRepository orderRepository = new FakeOrderRepository();
    private final OrderTableRepository orderTableRepository = new FakeOrderTableRepository();
    private final MenuQueryService menuQueryService = new MenuQueryService(menuRepository);
    private final OrderValidator orderValidator = new OrderValidator(menuQueryService);
    private final OrderService orderService = new OrderService(menuRepository, orderRepository, orderTableRepository, orderValidator);

    private Menu 소고기메뉴;

    @BeforeEach
    void setUp() {
        Product 살치살 = Product.of(1L, "살치살", 10000);
        Product 부채살 = Product.of(2L, "부채살", 10000);
        Menu menu = Menu.create("소고기세트", BigDecimal.valueOf(70000), null,
                new MenuProducts(
                        Arrays.asList(
                                MenuProduct.of(살치살, 2),
                                MenuProduct.of(부채살, 1)
                        )
                )
        );
        소고기메뉴 = menuRepository.save(menu);
    }

    @DisplayName("주문 항목이 없으면 예외 발생한다")
    @Test
    void notExistsOrderLineItems() {
        OrderTable orderTable = orderTableRepository.save(OrderTable.of(10, false));
        OrderRequest order = OrderRequest.of(orderTable.getId(), Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(BadOrderRequestException.class);
    }

    @DisplayName("주문 테이블이 없으면 예외 발생한다.")
    @Test
    void notExistsOrderTable() {
        OrderTable orderTable = orderTableRepository.save(OrderTable.of(10, false));
        OrderRequest order = OrderRequest.of(null,
                Arrays.asList(
                        OrderLineItemRequest.of(소고기메뉴.getId(), 10)
                )
        );

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(NotFoundOrderTableException.class);
    }

    @DisplayName("주문 테이블이 공석이면 예외가 발생한다")
    @Test
    void empty() {
        OrderTable orderTable = orderTableRepository.save(OrderTable.of(10, true));
        OrderRequest order = OrderRequest.of(orderTable.getId(),
                Arrays.asList(
                        OrderLineItemRequest.of(소고기메뉴.getId(), 10)
                )
        );

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(NotCreateOrderException.class);
    }

    @DisplayName("주문 저장 성공")
    @Test
    void success() {
        OrderTable orderTable = orderTableRepository.save(OrderTable.of(10, false));
        OrderRequest order = OrderRequest.of(orderTable.getId(),
                Arrays.asList(
                        OrderLineItemRequest.of(소고기메뉴.getId(), 10)
                )
        );

        OrderResponse result = orderService.create(order);
        assertAll(
                () -> assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(result.getOrderTableId()).isEqualTo(orderTable.getId()),
                () -> equalOrderLineItem(result, order)
        );
    }

    @DisplayName("모든 주문 조회")
    @Test
    void list() {
        OrderTable orderTable = orderTableRepository.save(OrderTable.of(10, false));
        OrderRequest order1 = OrderRequest.of(orderTable.getId(),
                Arrays.asList(
                        OrderLineItemRequest.of(소고기메뉴.getId(), 10)
                )
        );
        OrderTable orderTable2 = orderTableRepository.save(OrderTable.of(15, false));
        OrderRequest order2 = OrderRequest.of(orderTable2.getId(),
                Arrays.asList(
                        OrderLineItemRequest.of(소고기메뉴.getId(), 20)
                )
        );

        OrderResponse resultOrder1 = orderService.create(order1);
        OrderResponse resultOrder2 = orderService.create(order2);

        List<OrderResponse> list = orderService.list();

        long count = getOrderLineItemCount(list);
        assertAll(
                () -> assertThat(list.size()).isEqualTo(2),
                () -> assertThat(count).isEqualTo(resultOrder1.getOrderLineItemResponseList().size() + resultOrder2.getOrderLineItemResponseList().size())
        );
    }

    @DisplayName("주문이 없으면 예외가 발생한다.")
    @Test
    void changeStatus() {
        assertThatThrownBy(() -> orderService.changeOrderStatus(33L, OrderStatusUpdateRequest.of(OrderStatus.COMPLETION.name())))
                .isInstanceOf(NotFoundOrderException.class);
    }

    @DisplayName("주문 상태가 COMPLETION 이면 예외가 발생한다.")
    @Test
    void orderStatusComplete() {
        OrderTable orderTable = orderTableRepository.save(OrderTable.of(10, false));
        Order order = new Order(1L,
                orderTable,
                OrderStatus.COMPLETION.name(),
                LocalDateTime.now(),
                Arrays.asList(OrderLineItem.of(소고기메뉴, 20))
        );
        orderRepository.save(order);

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), OrderStatusUpdateRequest.of(OrderStatus.COMPLETION.name())))
                .isInstanceOf(NotChangeOrderStatusException.class);
    }

    @DisplayName("주문 상태 변경 성공")
    @Test
    void successChangeStatus() {
        OrderTable orderTable = orderTableRepository.save(OrderTable.of(10, false));
        OrderRequest order = OrderRequest.of(orderTable.getId(),
                Arrays.asList(
                        OrderLineItemRequest.of(소고기메뉴.getId(), 10)
                )
        );
        OrderResponse result = orderService.create(order);
        OrderResponse resultOrder = orderService.changeOrderStatus(result.getId(), OrderStatusUpdateRequest.of(OrderStatus.COMPLETION.name()));

        assertAll(
                () -> assertThat(resultOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name()),
                () -> equalOrderLineItem(result, order)
        );
    }

    private long getOrderLineItemCount(List<OrderResponse> list) {
        long count = list.stream()
                .map(order -> order.getOrderLineItemResponseList())
                .flatMap(orderLineItems -> orderLineItems.stream())
                .count();
        return count;
    }

    private void equalOrderLineItem(OrderResponse result, OrderRequest order) {
        List<OrderLineItemResponse> resultOrderLineItems = result.getOrderLineItemResponseList();
        List<OrderLineItemRequest> orderLineItems = order.getOrderLineItems();
        assertAll(
                () -> {
                    for (int i = 0; i < resultOrderLineItems.size(); i++) {
                        OrderLineItemResponse resultOrderLineItem = resultOrderLineItems.get(i);
                        OrderLineItemRequest orderLineItem = orderLineItems.get(i);
                        assertThat(resultOrderLineItem.getMenuId()).isEqualTo(orderLineItem.getMenuId());
                        assertThat(resultOrderLineItem.getOrderId()).isEqualTo(result.getId());
                        assertThat(resultOrderLineItem.getQuantity()).isEqualTo(orderLineItem.getQuantity());
                    }
                }
        );
    }

}
