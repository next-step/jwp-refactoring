package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.product.fixture.ProductFixture;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

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

    private final Long menuId1 = 1L;
    private final Long menuId2 = 2L;
    private Menu menu1;
    private Menu menu2;
    private OrderCreateRequest orderCreateRequest;
    private OrderTable orderTable;
    private Order order;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = new MenuGroup("한가지 메뉴");
        MenuProduct menuProduct = MenuProduct.of(ProductFixture.후라이드, 1L);
        MenuProduct menuProduct2 = MenuProduct.of(ProductFixture.강정치킨, 1L);
        menu1 = Menu.of("후라이드치킨", 16_000L, menuGroup, Arrays.asList(menuProduct));
        menu2 = Menu.of("강정치킨", 12_000L, menuGroup, Arrays.asList(menuProduct2));
        List<OrderLineItemRequest> orderLineItems = Arrays.asList(
                new OrderLineItemRequest(menuId1, 1L),
                new OrderLineItemRequest(menuId2, 1L)
        );
        this.orderCreateRequest = new OrderCreateRequest(1L, orderLineItems);
        this.orderTable = OrderTable.of(1, false);
        this.order = Order.cooking(orderTable, Arrays.asList(
                OrderLineItem.of(menu1, 1L),
                OrderLineItem.of(menu2, 1L)
        ));
    }

    @Test
    @DisplayName("주문 등록시 등록에 성공하고 주문 정보를 반환한다")
    void createOrderThenReturnResponseTest() {
        // given
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));
        given(menuRepository.findById(menuId1)).willReturn(Optional.ofNullable(menu1));
        given(menuRepository.findById(menuId2)).willReturn(Optional.ofNullable(menu2));
        given(orderRepository.save(any())).willReturn(order);

        // when
        OrderResponse response = orderService.createOrder(orderCreateRequest);

        // then
        then(orderTableRepository).should(times(1)).findById(any());
        then(menuRepository).should(times(2)).findById(any());
        then(orderRepository).should(times(1)).save(any());

        List<Long> orderLineMenuIds = orderCreateRequest.getOrderLineItems().stream().map(OrderLineItemRequest::getMenuId).collect(Collectors.toList());
        List<Long> expectedOrderLineMenuIds = response.getOrderLineItems().stream().map(OrderLineItemResponse::getMenuId).collect(Collectors.toList());
        assertAll(
                () -> assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
                () -> assertThat(orderLineMenuIds.size()).isEqualTo(expectedOrderLineMenuIds.size())
        );
    }

    @Test
    @DisplayName("주문 등록시 주문 상품이 미동록된 경우 예외처리되어 등록에 실패한다")
    void createOrderThrownByNotFoundProductTest() {
        // given
        given(orderTableRepository.findById(any())).willReturn(Optional.ofNullable(orderTable));
        given(menuRepository.findById(any())).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> orderService.createOrder(orderCreateRequest))
                .isInstanceOf(EntityNotFoundException.class);

        // then
        then(orderTableRepository).should(times(1)).findById(any());
        then(menuRepository).should(times(1)).findById(any());
    }

    @Test
    @DisplayName("주문 등록시 주문 테이블이 미동록된 경우 예외처리되어 등록에 실패한다")
    void createOrderThrownByNotFoundOrderTableTest() {
        // given
        given(orderTableRepository.findById(any())).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> orderService.createOrder(orderCreateRequest))
                .isInstanceOf(EntityNotFoundException.class);

        // then
        then(orderTableRepository).should(times(1)).findById(any());
    }

    @Test
    @DisplayName("주문 등록시 주문 테이블이 사용중인 경우 예외처리되어 등록에 실패한다")
    void createOrderThrownByOrderTableIsNotEmptyTest() {
        // given
        orderTable.changeEmpty(true);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));
        given(menuRepository.findById(menuId1)).willReturn(Optional.of(menu1));
        given(menuRepository.findById(menuId2)).willReturn(Optional.of(menu2));

        // when
        assertThatThrownBy(() -> orderService.createOrder(orderCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        then(orderTableRepository).should(times(1)).findById(any());
        then(menuRepository).should(times(2)).findById(any());
    }

    @Test
    @DisplayName("주문 목록 조회시 주문 목록을 반환한다")
    void findAllOrdersThenReturnOrderResponses() {
        // given
        given(orderRepository.findAllWithLineItems()).willReturn(Arrays.asList(order));

        // when
        List<OrderResponse> orderResponses = orderService.findAllOrders();

        // then
        then(orderRepository).should(times(1)).findAllWithLineItems();
        List<Long> orderTableIds = Arrays.asList(order.getId());
        List<Long> expectedOrderTableIds = orderResponses.stream().map(OrderResponse::getId).collect(Collectors.toList());
        assertAll(
                () -> assertThat(orderTableIds).hasSize(orderTableIds.size()),
                () -> assertThat(orderTableIds).containsAll(expectedOrderTableIds)
        );
    }

    @Test
    @DisplayName("주문 상태 변경시 변경에 성공한다")
    void changeOrderStateTest() {
        given(orderRepository.findWithLineItemById(any())).willReturn(Optional.of(order));

        // when
        OrderResponse orderResponse = orderService.changeOrderStatus(1L, OrderStatus.COMPLETION);

        // then
        then(orderRepository).should(times(1)).findWithLineItemById(any());
        assertThat(order.getOrderStatus()).isEqualTo(orderResponse.getOrderStatus());
    }

    @Test
    @DisplayName("주문 상태 변경시 주문이 미등록된경우 예외처리되어 변경에_실패한다")
    void changeOrderStateThrownByNotFoundOrderTest() {
        given(orderRepository.findWithLineItemById(any())).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, OrderStatus.COMPLETION))
                .isInstanceOf(EntityNotFoundException.class);

        // then
        then(orderRepository).should(times(1)).findWithLineItemById(any());
    }

    @Test
    @DisplayName("주문 상태 변경시 주문이 이미 완료된경우 예외처리되어 변경에 실패한다")
    void changeOrderStateThrownByOrderIsCompletedTest() {
        order.changeState(OrderStatus.COMPLETION);
        given(orderRepository.findWithLineItemById(any())).willReturn(Optional.of(order));

        // when
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, OrderStatus.COMPLETION))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        then(orderRepository).should(times(1)).findWithLineItemById(any());
    }
}
