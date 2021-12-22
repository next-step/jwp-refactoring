package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.application.fixture.MenuFixture;
import kitchenpos.application.fixture.MenuGroupFixture;
import kitchenpos.application.fixture.MenuProductFixture;
import kitchenpos.application.fixture.OrderFixture;
import kitchenpos.application.fixture.OrderLineItemFixture;
import kitchenpos.application.fixture.ProductFixture;
import kitchenpos.application.fixture.TableFixture;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItemRepository;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.Quantity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderLineItemRepository orderLineItemRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderService orderService;

    Product 상품_후라이드;
    Product 상품_양념치킨;

    MenuGroup 한마리_메뉴그룹;

    MenuProduct 메뉴상품_후라이드;
    MenuProduct 메뉴상품_양념치킨;

    Menu 후라이드치킨;
    Menu 양념치킨;

    OrderLineItem 주문항목_후라이드;
    OrderLineItem 주문항목_양념치킨;

    OrderTable 후라이드_주문테이블;
    OrderTable 양념치킨_주문테이블;

    Order 후라이드_주문;
    Order 양념치킨_주문;

    @BeforeEach
    void setUp() {

        상품_후라이드 = ProductFixture.create(1L, "후라이드", BigDecimal.valueOf(16_000L));
        상품_양념치킨 = ProductFixture.create(2L, "양념치킨", BigDecimal.valueOf(16_000L));

        한마리_메뉴그룹 = MenuGroupFixture.create(1L, "한마리 메뉴");

        메뉴상품_후라이드 = MenuProductFixture.createMenuProduct(상품_후라이드, Quantity.of(1L));
        메뉴상품_양념치킨 = MenuProductFixture.createMenuProduct(상품_양념치킨, Quantity.of(1L));

        후라이드치킨 = MenuFixture.createMenu(1L, "후라이드치킨", 16_000L, 한마리_메뉴그룹, Arrays.asList(메뉴상품_후라이드));
        양념치킨 = MenuFixture.createMenu(2L, "양념치킨", 16_000L, 한마리_메뉴그룹, Arrays.asList(메뉴상품_양념치킨));

        주문항목_후라이드 = OrderLineItemFixture.create(1L, 후라이드치킨, Quantity.of(1L));
        주문항목_양념치킨 = OrderLineItemFixture.create(2L, 양념치킨, Quantity.of(1L));

        후라이드_주문테이블 = TableFixture.create(1L, 1, false);
        양념치킨_주문테이블 = TableFixture.create(1L, 2, false);

        후라이드_주문 = OrderFixture.create(1L, 후라이드_주문테이블, Arrays.asList(주문항목_후라이드));
        양념치킨_주문 = OrderFixture.create(1L, 양념치킨_주문테이블, Arrays.asList(주문항목_후라이드));
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void create() {
        given(menuRepository.countByIdIn(anyList())).willReturn(1);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(후라이드_주문테이블));
        given(orderRepository.save(후라이드_주문)).willReturn(후라이드_주문);
        given(orderLineItemRepository.save(주문항목_후라이드)).willReturn(주문항목_후라이드);

        Order savedOrder = orderService.create(후라이드_주문);

        assertThat(savedOrder).isEqualTo(후라이드_주문);
    }

    @DisplayName("주문 항목이 하나 이상되지 않으면 예외가 발생한다.")
    @Test
    void createEmptyOrderLineItems() {
        OrderTable 주문테이블 = TableFixture.create(1L, 1, false);
        Order 주문항목없는_주문 = OrderFixture.create(1L, 주문테이블, Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(주문항목없는_주문))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목의 메뉴를 찾을 수 없으면 예외가 발생한다.")
    @Test
    void createNotFoundMenu() {
        given(menuRepository.countByIdIn(anyList())).willReturn(0);

        assertThatThrownBy(() -> orderService.create(후라이드_주문))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블을 찾을 수 없으면 예외가 발생한다.")
    @Test
    void createNotFoundOrderTable() {
        Order 주문테이블없는_주문 = OrderFixture.create(1L, 후라이드_주문테이블, Arrays.asList(주문항목_후라이드));

        given(menuRepository.countByIdIn(anyList())).willReturn(1);
        given(orderTableRepository.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.create(주문테이블없는_주문))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어 있으면 예외가 발생한다.")
    @Test
    void createEmptyOrderTable() {
        OrderTable 빈_주문테이블 = TableFixture.create(1L, 0, true);
        Order 빈_주문테이블로_주문 = OrderFixture.create(1L, 빈_주문테이블, Arrays.asList(주문항목_후라이드));

        given(menuRepository.countByIdIn(anyList())).willReturn(1);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(빈_주문테이블));

        assertThatThrownBy(() -> orderService.create(빈_주문테이블로_주문))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void list() {
        given(orderRepository.findAll()).willReturn(Arrays.asList(후라이드_주문, 양념치킨_주문));

        List<Order> orders = orderService.list();

        assertThat(orders).containsExactly(후라이드_주문, 양념치킨_주문);
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        Order 상태를_변경할_주문 = OrderFixture.create(1L, 후라이드_주문테이블, OrderStatus.COOKING,
            Arrays.asList(주문항목_후라이드));
        given(orderRepository.findById(1L)).willReturn(Optional.of(상태를_변경할_주문));
        given(orderLineItemRepository.findAllByOrderId(1L)).willReturn(Arrays.asList(주문항목_후라이드));

        Order 상태가_변경된_주문 = orderService.changeOrderStatus(1L,
            OrderFixture.create(OrderStatus.MEAL));

        assertThat(상태가_변경된_주문.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("주문 상태를 변경할 주문이 등록되어 있지 않은 경우 예외가 발생한다.")
    @Test
    void changeOrderStatusNotFoundOrder() {
        given(orderRepository.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.changeOrderStatus(1L,
            OrderFixture.create(OrderStatus.MEAL)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("계산이 완료된 주문의 상태를 변경하려고 하면 예외가 발생한다.")
    @Test
    void changeOrderStatusCompletion() {
        Order 상태를_변경할_주문 = OrderFixture.create(1L, 후라이드_주문테이블, OrderStatus.COMPLETION,
            Arrays.asList(주문항목_후라이드));

        given(orderRepository.findById(1L)).willReturn(Optional.of(상태를_변경할_주문));

        assertThatThrownBy(() -> orderService.changeOrderStatus(1L,
            OrderFixture.create(OrderStatus.COMPLETION)))
            .isInstanceOf(IllegalArgumentException.class);
    }
}