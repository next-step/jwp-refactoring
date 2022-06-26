package kitchenpos.order.application;

import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static kitchenpos.utils.DomainFixtureFactory.createMenu;
import static kitchenpos.utils.DomainFixtureFactory.createMenuGroup;
import static kitchenpos.utils.DomainFixtureFactory.createMenuProduct;
import static kitchenpos.utils.DomainFixtureFactory.createOrder;
import static kitchenpos.utils.DomainFixtureFactory.createOrderLineItem;
import static kitchenpos.utils.DomainFixtureFactory.createOrderRequest;
import static kitchenpos.utils.DomainFixtureFactory.createOrderTable;
import static kitchenpos.utils.DomainFixtureFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.product.domain.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private MenuService menuService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private OrderService orderService;

    private OrderTable 주문테이블;
    private Menu 양념치킨;
    private MenuProduct 양념치킨상품;
    private Product 양념;
    private OrderLineItem 주문항목;
    private Order 주문;

    @BeforeEach
    void setUp() {
        양념 = createProduct(1L, "양념", BigDecimal.valueOf(20000L));
        양념치킨상품 = createMenuProduct(양념, 2L);
        양념치킨 = createMenu("양념치킨", BigDecimal.valueOf(10000L), createMenuGroup(2L, "한마리메뉴"),
                MenuProducts.from(Lists.newArrayList(양념치킨상품)));
        주문테이블 = createOrderTable(1L, 2, false);
        주문항목 = createOrderLineItem(양념치킨, 2L);
        주문 = createOrder(주문테이블, OrderLineItems.from(Lists.newArrayList(주문항목)), 1);
    }

    @DisplayName("주문 생성 테스트")
    @Test
    void create() {
        OrderRequest orderRequest = createOrderRequest(주문테이블.id(), null,
                Lists.newArrayList(new OrderLineItemRequest(1L, 2L)));
        given(menuService.findMenu(1L)).willReturn(양념치킨);
        given(orderTableRepository.findById(orderRequest.getOrderTableId())).willReturn(Optional.ofNullable(주문테이블));
        given(orderRepository.save(주문)).willReturn(주문);
        OrderResponse orderResponse = orderService.create(orderRequest);
        assertAll(
                () -> assertThat(orderResponse.getOrderTableId()).isEqualTo(주문테이블.id()),
                () -> assertThat(orderResponse.getOrderStatus()).isEqualTo(COOKING),
                () -> assertThat(orderResponse.getOrderLineItems()).containsExactlyElementsOf(
                        Lists.newArrayList(OrderLineItemResponse.from(주문항목)))
        );
    }


    @DisplayName("주문 생성시 주문테이블이 비어있는 경우 테스트")
    @Test
    void createWithEmptyOrderTable() {
        OrderRequest orderRequest = createOrderRequest(주문테이블.id(), null,
                Lists.newArrayList(new OrderLineItemRequest(1L, 2L)));
        OrderTable orderTable = createOrderTable(1L, 2, true);
        주문항목.addOrder(주문);
        given(menuService.findMenu(1L)).willReturn(양념치킨);
        given(orderTableRepository.findById(orderRequest.getOrderTableId())).willReturn(Optional.of(orderTable));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(orderRequest))
                .withMessage("주문테이블이 비어있으면 안됩니다.");
    }

    @DisplayName("주문 생성시 주문테이블이 등록이 안된 경우 테스트")
    @Test
    void createNotFoundOrderTable() {
        OrderRequest orderRequest = createOrderRequest(주문테이블.id(), null,
                Lists.newArrayList(new OrderLineItemRequest(1L, 2L)));
        주문항목.addOrder(주문);
        given(menuService.findMenu(1L)).willReturn(양념치킨);
        given(orderTableRepository.findById(주문테이블.id())).willReturn(Optional.empty());
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(orderRequest))
                .withMessage("주문테이블을 찾을 수 없습니다.");
    }

    @DisplayName("주문 목록 조회 테스트")
    @Test
    void list() {
        given(orderRepository.findAll()).willReturn(Lists.newArrayList(주문));
        List<OrderResponse> orders = orderService.list();
        assertThat(orders).containsExactlyElementsOf(Lists.newArrayList(OrderResponse.from(주문)));
    }

    @DisplayName("주문 상태 변경 테스트")
    @Test
    void changeOrderStatus() {
        OrderRequest orderRequest = createOrderRequest(null, COMPLETION, null);
        given(orderRepository.findById(주문.id())).willReturn(Optional.ofNullable(주문));
        given(orderRepository.save(주문)).willReturn(주문);
        OrderResponse savedOrder = orderService.changeOrderStatus(주문.id(), orderRequest);
        assertThat(savedOrder.getOrderStatus()).isEqualTo(orderRequest.getOrderStatus());
    }

    @DisplayName("주문 상태 변경시 주문 조회안되는 경우 테스트")
    @Test
    void changeOrderStatusWithNotFoundOrder() {
        OrderRequest orderRequest = createOrderRequest(null, COMPLETION, null);
        given(orderRepository.findById(주문.id())).willReturn(Optional.empty());
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(주문.id(), orderRequest))
                .withMessage("주문을 찾을 수 없습니다.");
    }

    @DisplayName("완료된 주문 상태 변경시 테스트")
    @Test
    void changeOrderStatusButAlreadyComplete() {
        주문.changeOrderStatus(COMPLETION);
        OrderRequest orderRequest = createOrderRequest(null, COOKING, null);
        given(orderRepository.findById(주문.id())).willReturn(Optional.ofNullable(주문));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(주문.id(), orderRequest))
                .withMessage("완료된 주문은 상태를 변경할 수 없습니다.");
    }

    @DisplayName("주문테이블의 주문이 완료상태가 아닌경우 테스트")
    @Test
    void validateComplete() {
        given(orderRepository.existsByOrderTableAndOrderStatusIn(주문테이블, Arrays.asList(COOKING, MEAL))).willReturn(true);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.validateComplete(주문테이블))
                .withMessage("주문테이블의 주문이 완료상태가 아닙니다.");
    }

    @DisplayName("주문테이블의 주문이 완료상태가 아닌경우 테스트")
    @Test
    void validateComplete2() {
        given(orderRepository.existsByOrderTableInAndOrderStatusIn(Lists.newArrayList(주문테이블),
                Arrays.asList(COOKING, MEAL))).willReturn(true);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.validateComplete(Lists.newArrayList(주문테이블)))
                .withMessage("주문테이블들의 주문이 완료상태가 아닙니다.");
    }
}
