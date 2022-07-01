package kitchenpos.order.application;

import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.COOKING;
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
import static org.mockito.BDDMockito.willThrow;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.validator.OrderValidator;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.product.domain.Product;
import org.assertj.core.api.Assertions;
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
    private OrderRepository orderRepository;
    @Mock
    private MenuService menuService;
    @Mock
    private OrderValidator orderValidator;
    @InjectMocks
    private OrderService orderService;

    private Menu 양념치킨;
    private OrderTable 주문테이블;
    private OrderLineItem 주문항목;
    private Order 주문;

    @BeforeEach
    void setUp() {
        Product 양념 = createProduct(1L, "양념", BigDecimal.valueOf(20000L));
        MenuGroup 한마리메뉴 = createMenuGroup(1L, "한마리메뉴");
        MenuProduct 양념치킨상품 = createMenuProduct(양념.id(), 2L);
        양념치킨 = createMenu(1L, "양념치킨", BigDecimal.valueOf(40000L), 한마리메뉴.id(),
                MenuProducts.from(Lists.newArrayList(양념치킨상품)));
        OrderMenu 주문메뉴 = OrderMenu.from(양념치킨);
        주문테이블 = createOrderTable(1L, 2, false);
        주문항목 = createOrderLineItem(주문메뉴, 2L);
        주문 = createOrder(주문테이블.id(), OrderLineItems.from(Lists.newArrayList(주문항목)));
    }

    @DisplayName("주문 생성 테스트")
    @Test
    void create() {
        OrderRequest orderRequest = createOrderRequest(주문테이블.id(), null,
                Lists.newArrayList(new OrderLineItemRequest(양념치킨.id(), 2L)));
        given(menuService.findMenu(양념치킨.id())).willReturn(양념치킨);
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
        willThrow(new IllegalArgumentException("주문테이블이 비어있으면 안됩니다.")).given(orderValidator).validate(orderRequest);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(orderRequest))
                .withMessage("주문테이블이 비어있으면 안됩니다.");
    }

    @DisplayName("주문 생성시 주문테이블이 등록이 안된 경우 테스트")
    @Test
    void createNotFoundOrderTable() {
        OrderRequest orderRequest = createOrderRequest(주문테이블.id(), null,
                Lists.newArrayList(new OrderLineItemRequest(1L, 2L)));
        willThrow(new IllegalArgumentException("주문테이블을 찾을 수 없습니다.")).given(orderValidator).validate(orderRequest);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(orderRequest))
                .withMessage("주문테이블을 찾을 수 없습니다.");
    }

    @DisplayName("주문 목록 조회 테스트")
    @Test
    void list() {
        given(orderRepository.findAllWithFetchJoin()).willReturn(Lists.newArrayList(주문));
        List<OrderResponse> orders = orderService.list();
        Assertions.assertThat(orders).containsExactlyElementsOf(Lists.newArrayList(OrderResponse.from(주문)));
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
}
