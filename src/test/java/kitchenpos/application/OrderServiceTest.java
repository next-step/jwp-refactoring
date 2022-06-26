package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.domain.Quantity;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.GuestNumber;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    private Menu menu;
    private OrderLineItem orderLineItem;
    private Orders order;


    private OrderService orderService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(menuRepository, orderRepository, orderTableRepository);

        MenuGroup menuGroup = new MenuGroup(1L, "메뉴그룹");
        menu = new Menu.Builder("메뉴")
                .setId(1L)
                .setPrice(Price.of(1_000L))
                .setMenuGroup(menuGroup)
                .build();
        orderLineItem = new OrderLineItem.Builder(null)
                .setSeq(1L)
                .setMenu(menu)
                .setQuantity(Quantity.of(1L))
                .builder();
        OrderTable orderTable = new OrderTable(1L, null, GuestNumber.of(5), false);
        OrderLineItems orderLineItems = new OrderLineItems(Arrays.asList(orderLineItem));
        order = new Orders(1L, orderTable, OrderStatus.COOKING, null, orderLineItems);
    }

    @Test
    @DisplayName("주문을 생성한다.")
    void createOrder() {
        // given
        when(menuRepository.countByIdIn(any())).thenReturn(1L);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(new OrderTable(1L, null, GuestNumber.of(3), false)));
        when(menuRepository.findById(any())).thenReturn(Optional.of(menu));
        when(orderRepository.save(any())).thenReturn(order);
        // when
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 1L);
        final OrderResponse actual = orderService.create(new OrderRequest(1L, Arrays.asList(orderLineItemRequest)));
        // then
        assertAll(
                () -> assertThat(actual.getOrderLineItems()).hasSize(1),
                () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(actual.getOrderTable().getId()).isEqualTo(1L)
        );
    }

    @Test
    @DisplayName("주문 할 메뉴가 없으면 에러 발생")
    void invalidZeroOfOrderLineItem() {
        // given
        final OrderRequest invalidOrderRequest = new OrderRequest(1L, Collections.emptyList());

        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(invalidOrderRequest));
    }

    @Test
    @DisplayName("등록되지 않은 메뉴 주문시 에러 발생")
    void orderNotExistMenu() {
        // given
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 1L);
        final OrderRequest notExistMenuOrderRequest = new OrderRequest(1L, Arrays.asList(orderLineItemRequest));

        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(notExistMenuOrderRequest));
    }

    @Test
    @DisplayName("주문 테이블이 존재하지 않으면 에러 발생")
    void notExistOrderTable() {
        // given
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 1L);
        when(menuRepository.countByIdIn(any())).thenReturn(1L);
        when(orderTableRepository.findById(any())).thenReturn(Optional.empty());

        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(new OrderRequest(1L, Arrays.asList(orderLineItemRequest))));
    }

    @Test
    @DisplayName("주문 내역을 조회할 수 있다.")
    void searchOrders() {
        // given
        when(orderRepository.findAll()).thenReturn(Arrays.asList(order));
        // when
        final List<OrderResponse> actual = orderService.list();
        // then
        assertThat(actual).hasSize(1);
    }

    @Test
    @DisplayName("주문의 상태를 변경할 수 있다.")
    void changeOrderStatus() {
        // given
        when(orderRepository.findById(any())).thenReturn(Optional.of(order));
        // when
        final OrderResponse actual = orderService.changeOrderStatus(1L, new OrderStatusRequest(OrderStatus.COOKING));
        // then
        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    @DisplayName("주문 내역이 존재하지 않은 주문 상태 변경시 에러 발생")
    void changeOrderStatusNotExistOrder() {
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(1L, new OrderStatusRequest(OrderStatus.COOKING)));
    }

    @Test
    @DisplayName("완료된 주문을 주문 상태 변경시 에러 발생")
    void changeOrderStatusCompletionOrder() {
        // given
        final OrderTable orderTable = new OrderTable(1L, null, GuestNumber.of(5), false);
        final Orders order = new Orders(1L, orderTable, OrderStatus.COMPLETION, LocalDateTime.now(), null);
        when(orderRepository.findById(any())).thenReturn(Optional.of(order));

        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(1L, new OrderStatusRequest(OrderStatus.COOKING)));
    }
}
