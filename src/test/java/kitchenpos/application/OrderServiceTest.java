package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 관련 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private OrderService orderService;

    private Product 하와이안피자;
    private Product 콜라;
    private Product 피클;
    private MenuGroup 피자;
    private Menu 하와이안피자세트;
    private MenuProduct 하와이안피자상품;
    private MenuProduct 콜라상품;
    private MenuProduct 피클상품;
    private OrderTable 주문테이블;
    private Order 주문;
    private OrderLineItem 하와이안피자세트주문;

    @BeforeEach
    void setUp() {
        하와이안피자 = new Product(1L, "하와이안피자", BigDecimal.valueOf(15_000));
        콜라 = new Product(2L, "콜라", BigDecimal.valueOf(2_000));
        피클 = new Product(3L, "피클", BigDecimal.valueOf(1_000));

        피자 = new MenuGroup(1L, "피자");

        하와이안피자상품 = new MenuProduct(1L, 하와이안피자세트, 하와이안피자, 1L);
        콜라상품 = new MenuProduct(2L, 하와이안피자세트, 콜라, 1L);
        피클상품 = new MenuProduct(3L, 하와이안피자세트, 피클, 1L);

        하와이안피자세트 = new Menu(1L, "하와이안피자세트", BigDecimal.valueOf(18_000L), 피자,
            MenuProducts.from(Arrays.asList(하와이안피자상품, 콜라상품, 피클상품)));

        주문테이블 = new OrderTable(1L, null, 0, false);
        주문 = new Order(1L, 주문테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), new ArrayList<>());
        하와이안피자세트주문 = new OrderLineItem(1L, 주문.getId(), 하와이안피자세트.getId(), 1);
        주문.setOrderLineItems(Arrays.asList(하와이안피자세트주문));
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void createOrder() {
        // given
        List<Long> menuIds = 주문.getOrderLineItems()
            .stream()
            .map(OrderLineItem::getMenuId)
            .collect(Collectors.toList());
        // when(menuDao.countByIdIn(menuIds)).thenReturn((long) menuIds.size());
        when(orderTableDao.findById(주문.getOrderTableId())).thenReturn(Optional.of(주문테이블));
        when(orderDao.save(주문)).thenReturn(주문);
        when(orderLineItemDao.save(하와이안피자세트주문)).thenReturn(하와이안피자세트주문);

        // when
        Order result = orderService.create(주문);

        // then
        assertAll(
            () -> assertThat(result.getId()).isEqualTo(주문.getId()),
            () -> assertThat(result.getOrderStatus()).isEqualTo(주문.getOrderStatus())
        );
    }

    @DisplayName("주문 테이블이 등록되지 않으면 예외가 발생한다.")
    @Test
    void crateOrderNotExistOrderTableException() {
        // given
        List<Long> menuIds = 주문.getOrderLineItems()
            .stream()
            .map(OrderLineItem::getMenuId)
            .collect(Collectors.toList());
        // when(menuDao.countByIdIn(menuIds)).thenReturn((long) menuIds.size());
        when(orderTableDao.findById(주문.getOrderTableId())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> orderService.create(주문))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목 메뉴가 등록되지 않으면 예외가 발생한다.")
    @Test
    void createOrderNotExistOrderLineItemException() {
        // given
        // when(menuDao.countByIdIn(anyList())).thenReturn(10L);

        // when & then
        assertThatThrownBy(() -> orderService.create(주문))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목 메뉴가 빈 값이면 예외가 발생한다.")
    @Test
    void CreateOrderEmptyOrderLineItemException() {
        // given
        주문 = new Order(1L, 주문테이블.getId(), null, LocalDateTime.now(), new ArrayList<>());

        // when & then
        assertThatThrownBy(() -> orderService.create(주문))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태를 수정한다.")
    @Test
    void updateOrderStatus() {
        // given
        String expectedStatus = OrderStatus.MEAL.name();
        Order updatedOrder = new Order(
            주문.getId(),
            주문.getOrderTableId(),
            expectedStatus,
            주문.getOrderedTime(),
            주문.getOrderLineItems()
        );
        when(orderDao.findById(주문.getId())).thenReturn(Optional.of(주문));
        when(orderLineItemDao.findAllByOrderId(주문.getId())).thenReturn(주문.getOrderLineItems());

        // when
        Order result = orderService.changeOrderStatus(주문.getId(), updatedOrder);

        // then
        assertThat(result.getOrderStatus()).isEqualTo(expectedStatus);
    }

    @DisplayName("등록되지 않은 주문의 상태를 수정하면 예외가 발생한다.")
    @Test
    void updateOrderStatusNotExistException() {
        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(3L, 주문))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("계산 완료된 상태의 주문을 수정하면 예외가 발생한다.")
    @Test
    void updateOrderStatusCompleteException() {
        // given
        주문.setOrderStatus(OrderStatus.COMPLETION.name());
        Order updatedOrder = new Order(
            주문.getId(),
            주문.getOrderTableId(),
            OrderStatus.MEAL.name(),
            주문.getOrderedTime(),
            주문.getOrderLineItems()
        );

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), updatedOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 목록을 조회한다.")
    @Test
    void findAllOrder() {
        // given
        when(orderDao.findAll()).thenReturn(Arrays.asList(주문));
        when(orderLineItemDao.findAllByOrderId(주문.getId())).thenReturn(주문.getOrderLineItems());

        // when
        List<Order> results = orderService.list();

        // then
        assertThat(results).hasSize(1)
            .containsExactly(주문);
    }
}
