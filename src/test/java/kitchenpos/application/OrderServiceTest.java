package kitchenpos.application;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.UpdateOrderStatusRequest;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@DisplayName("OrderService 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderService orderService;

    private Product 불고기;
    private Product 김치;
    private Product 공기밥;
    private MenuGroup 한식;
    private MenuProduct 불고기상품;
    private MenuProduct 김치상품;
    private MenuProduct 공기밥상품;
    private Menu 불고기정식;
    private Order 주문;
    private OrderTable 주문테이블;
    private OrderLineItem 불고기정식주문;
    private OrderLineItemRequest 불고기정식주문요청;

    @BeforeEach
    void setUp() {
        불고기 = new Product(1L, new Name("불고기"), new Price(BigDecimal.valueOf(10_000)));
        김치 = new Product(2L, new Name("김치"), new Price(BigDecimal.valueOf(1_000)));
        공기밥 = new Product(3L, new Name("공기밥"), new Price(BigDecimal.valueOf(1_000)));
        한식 = new MenuGroup(1L, new Name("한식"));
        불고기정식 = new Menu(1L, new Name("불고기정식"), new Price(BigDecimal.valueOf(12_000L)), 한식);
        불고기상품 = new MenuProduct(1L, new Quantity(1L), 불고기정식, 불고기);
        김치상품 = new MenuProduct(2L, new Quantity(1L), 불고기정식, 김치);
        공기밥상품 = new MenuProduct(3L, new Quantity(1L), 불고기정식, 공기밥);

        주문테이블 = new OrderTable(1L, new NumberOfGuests(0), false);
        불고기정식주문 = new OrderLineItem(new Quantity(1L), 불고기정식);
        주문 = new Order(주문테이블, OrderStatus.COOKING, LocalDateTime.now());
        주문.addOrderLineItem(불고기정식주문);
        불고기정식주문요청 = OrderLineItemRequest.of(불고기정식.getMenuGroup().getId(), 1L);
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void createOrder() {
        // given
        List<Long> menuIds = 주문.getOrderLineItems()
                        .stream()
                        .map(OrderLineItem::getMenu)
                        .map(Menu::getId)
                        .collect(Collectors.toList());
        when(menuRepository.findAllById(menuIds)).thenReturn(Arrays.asList(불고기정식));
        when(menuRepository.countByIdIn(menuIds)).thenReturn(menuIds.size());
        when(orderTableRepository.findById(주문.getOrderTable().getId())).thenReturn(Optional.of(주문테이블));
        when(orderRepository.save(주문)).thenReturn(주문);
        OrderRequest request = OrderRequest.of(주문테이블.getId(), Arrays.asList(불고기정식주문요청));

        // when
        OrderResponse result = orderService.create(request);

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(주문.getId()),
                () -> assertThat(result.getOrderStatus()).isEqualTo(주문.getOrderStatus().name())
        );
    }

    @DisplayName("주문메뉴가 비어있으면 예외가 발생한다.")
    @Test
    void emptyOrderLineItemsException() {
        // given
        OrderRequest request = OrderRequest.of(주문테이블.getId(), new ArrayList<>());

        // when & then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문메뉴가 존재하지 않으면 예외가 발생한다.")
    @Test
    void notExistOrderLineItemsException() {
        // given
        OrderRequest request = OrderRequest.of(주문테이블.getId(), Arrays.asList(불고기정식주문요청));
        when(menuRepository.countByIdIn(anyList())).thenReturn(10);

        // when & then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 존재하지 않으면 예외가 발생한다.")
    @Test
    void notExistOrderTableException() {
        // given
        List<Long> menuIds = 주문.getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenu)
                .map(Menu::getId)
                .collect(Collectors.toList());
        OrderRequest request = OrderRequest.of(주문테이블.getId(), Arrays.asList(불고기정식주문요청));

        when(menuRepository.countByIdIn(menuIds)).thenReturn(menuIds.size());
        when(orderTableRepository.findById(주문.getOrderTable().getId())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어있다면 예외가 발생한다.")
    @Test
    void emptyOrderTableException() {
        // given
        Order order = new Order(주문테이블, OrderStatus.COMPLETION, LocalDateTime.now());
        주문테이블.updateEmpty(true, Arrays.asList(order));
        List<Long> menuIds = 주문.getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenu)
                .map(Menu::getId)
                .collect(Collectors.toList());
        OrderRequest request = OrderRequest.of(주문테이블.getId(), Arrays.asList(불고기정식주문요청));
        when(menuRepository.countByIdIn(menuIds)).thenReturn(menuIds.size());
        when(orderTableRepository.findById(주문.getOrderTable().getId())).thenReturn(Optional.of(주문테이블));

        // when & then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 조회할 수 있다.")
    @Test
    void findAllOrder() {
        // given
        when(orderRepository.findAll()).thenReturn(Arrays.asList(주문));

        // when
        List<OrderResponse> results = orderService.findAll();

        // then
        assertAll(
                () -> assertThat(results).hasSize(1),
                () -> assertThat(results.get(0).getId()).isEqualTo(주문.getId()),
                () -> assertThat(results.get(0).getOrderStatus()).isEqualTo(주문.getOrderStatus().name())
        );
    }

    @DisplayName("주문 상태를 수정할 수 있다.")
    @Test
    void updateOrderStatus() {
        // given
        OrderStatus expectedStatus = OrderStatus.MEAL;
        UpdateOrderStatusRequest request = UpdateOrderStatusRequest.of(expectedStatus.name());
        when(orderRepository.findById(주문.getId())).thenReturn(Optional.of(주문));
        when(orderRepository.save(주문)).thenReturn(주문);

        // when
        OrderResponse result = orderService.changeOrderStatus(주문.getId(), request);

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(주문.getId()),
                () -> assertThat(result.getOrderStatus()).isEqualTo(expectedStatus.name())
        );
    }

    @DisplayName("등록되지 않은 주문 상태를 수정하면 예외가 발생한다.")
    @Test
    void notExistOrderUpdateStatusException() {
        // given
        UpdateOrderStatusRequest request = UpdateOrderStatusRequest.of(OrderStatus.COOKING.name());

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(3L, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("계산 완료된 주문 상태를 수정하면 예외가 발생한다.")
    @Test
    void updateCompletionOrderStatusException() {
        // given
        주문.setOrderStatus(OrderStatus.COMPLETION);
        UpdateOrderStatusRequest request = UpdateOrderStatusRequest.of(주문.getOrderStatus().name());


        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
