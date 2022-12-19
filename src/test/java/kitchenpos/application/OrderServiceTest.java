package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;
import static kitchenpos.domain.MenuGroupTestFixture.createMenuGroup;
import static kitchenpos.domain.MenuTestFixture.createMenu;
import static kitchenpos.domain.MenuTestFixture.createMenuProduct;
import static kitchenpos.domain.OrderTableTestFixture.createOrderTable;
import static kitchenpos.domain.OrderTestFixture.createOrder;
import static kitchenpos.domain.OrderTestFixture.createOrderLineItem;
import static kitchenpos.domain.ProductTestFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@DisplayName("주문 관련 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private OrderService orderService;

    private Product 감자튀김;
    private Product 불고기버거;
    private Product 치킨버거;
    private Product 콜라;
    private MenuGroup 햄버거세트;
    private MenuProduct 감자튀김상품;
    private MenuProduct 불고기버거상품;
    private MenuProduct 치킨버거상품;
    private MenuProduct 콜라상품;
    private Menu 불고기버거세트;
    private Menu 치킨버거세트;
    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;
    private OrderLineItem 불고기버거세트주문;
    private OrderLineItem 치킨버거세트주문;
    private Order 주문1;
    private Order 주문2;

    @BeforeEach
    void setUp() {
        감자튀김 = createProduct(1L, "감자튀김", BigDecimal.valueOf(3000L));
        콜라 = createProduct(2L, "콜라", BigDecimal.valueOf(1500L));
        불고기버거 = createProduct(3L, "불고기버거", BigDecimal.valueOf(4000L));
        치킨버거 = createProduct(4L, "치킨버거", BigDecimal.valueOf(4500L));
        햄버거세트 = createMenuGroup(1L, "햄버거세트");
        감자튀김상품 = createMenuProduct(1L, null, 감자튀김.getId(), 1L);
        콜라상품 = createMenuProduct(2L, null, 콜라.getId(), 1L);
        불고기버거상품 = createMenuProduct(3L, null, 불고기버거.getId(), 1L);
        치킨버거상품 = createMenuProduct(3L, null, 치킨버거.getId(), 1L);
        불고기버거세트 = createMenu(1L, "불고기버거세트", BigDecimal.valueOf(8500L), 햄버거세트.getId(),
                Arrays.asList(감자튀김상품, 콜라상품, 불고기버거상품));
        치킨버거세트 = createMenu(2L, "치킨버거세트", BigDecimal.valueOf(9000L), 햄버거세트.getId(),
                Arrays.asList(감자튀김상품, 콜라상품, 치킨버거상품));
        주문테이블1 = createOrderTable(1L, null, 5, false);
        주문테이블2 = createOrderTable(2L, null, 7, false);
        불고기버거세트주문 = createOrderLineItem(1L, null, 불고기버거세트.getId(), 2);
        치킨버거세트주문 = createOrderLineItem(2L, null, 치킨버거세트.getId(), 1);
        주문1 = createOrder(주문테이블1.getId(), null, null, Arrays.asList(불고기버거세트주문, 치킨버거세트주문));
        주문2 = createOrder(주문테이블2.getId(), null, null, singletonList(불고기버거세트주문));
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void 주문_생성() {
        // given
        List<Long> menuIds = 주문1.getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        when(menuDao.countByIdIn(menuIds)).thenReturn((long) menuIds.size());
        when(orderTableDao.findById(주문1.getOrderTableId())).thenReturn(Optional.of(주문테이블1));
        when(orderDao.save(주문1)).thenReturn(주문1);
        when(orderLineItemDao.save(불고기버거세트주문)).thenReturn(불고기버거세트주문);
        when(orderLineItemDao.save(치킨버거세트주문)).thenReturn(치킨버거세트주문);
        // when
        Order order = orderService.create(주문1);
        // then
        assertAll(
                () -> assertThat(order.getOrderedTime()).isNotNull(),
                () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(order.getId()).isEqualTo(불고기버거세트주문.getOrderId()),
                () -> assertThat(order.getId()).isEqualTo(치킨버거세트주문.getOrderId())
        );
    }

    @DisplayName("주문 항목이 비어있으면 주문 생성 시 예외가 발생한다.")
    @Test
    void 주문_생성_예외1() {
        // given
        Order order = createOrder(주문테이블1.getId(), null, null, null);
        // when & then
        Assertions.assertThatThrownBy(
                () -> orderService.create(order)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 시, 주문 항목 내에 등록되지 않은 메뉴가 있다면 예외가 발생한다.")
    @Test
    void 주문_생성_예외2() {
        // given
        List<Long> menuIds = 주문1.getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        when(menuDao.countByIdIn(menuIds)).thenReturn(0L);
        // when & then
        Assertions.assertThatThrownBy(
                () -> orderService.create(주문1)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되어 있지않은 주문 테이블을 가진 주문은 생성될 수 없다.")
    @Test
    void 주문_생성_예외3() {
        // given
        List<Long> menuIds = 주문1.getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        when(menuDao.countByIdIn(menuIds)).thenReturn((long) menuIds.size());
        when(orderTableDao.findById(주문1.getOrderTableId())).thenReturn(Optional.empty());
        // when & then
        Assertions.assertThatThrownBy(
                () -> orderService.create(주문1)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어있을 경우 주문은 생성될 수 없다.")
    @Test
    void 주문_생성_예외4() {
        // given
        OrderTable orderTable = createOrderTable(4L, null, 6, true);
        Order order = createOrder(orderTable.getId(), null, null, singletonList(불고기버거세트주문));
        List<Long> menuIds = order.getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        when(menuDao.countByIdIn(menuIds)).thenReturn((long) menuIds.size());
        when(orderTableDao.findById(order.getOrderTableId())).thenReturn(Optional.of(orderTable));
        // when & then
        Assertions.assertThatThrownBy(
                () -> orderService.create(order)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 전체 목록을 조회한다.")
    @Test
    void 주문_전체_목록_조회() {
        // given
        List<Order> orders = Arrays.asList(주문1, 주문2);
        when(orderDao.findAll()).thenReturn(orders);
        for (Order order : orders) {
            when(orderLineItemDao.findAllByOrderId(order.getId())).thenReturn(order.getOrderLineItems());
        }
        // when
        List<Order> findOrders = orderService.list();
        // then
        assertAll(
                () -> assertThat(findOrders).hasSize(orders.size()),
                () -> assertThat(findOrders).containsExactly(주문1, 주문2)
        );
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void 주문_상태_변경() {
        // given
        String expectOrderStatus = OrderStatus.MEAL.name();
        Order changeOrder = createOrder(주문2.getId(), 주문2.getOrderTableId(), expectOrderStatus, 주문2.getOrderedTime(), 주문2.getOrderLineItems());
        when(orderDao.findById(주문2.getId())).thenReturn(Optional.of(주문2));
        when(orderDao.save(주문2)).thenReturn(주문2);
        // when
        Order resultOrder = orderService.changeOrderStatus(주문2.getId(), changeOrder);
        // then
        assertThat(resultOrder.getOrderStatus()).isEqualTo(expectOrderStatus);
    }

    @DisplayName("등록되지 않은 주문에 대해 주문 상태를 변경할 수 없다.")
    @Test
    void 주문_상태_변경_예외1() {
        // given
        Long notExistsOrderId = 5L;
        when(orderDao.findById(notExistsOrderId)).thenReturn(Optional.empty());
        // when & then
        Assertions.assertThatThrownBy(
                () -> orderService.changeOrderStatus(notExistsOrderId, 주문2)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 완료된 주문이면 주문 상태를 변경할 수 없다.")
    @Test
    void 주문_상태_변경_예외2() {
        // given
        Order order = createOrder(주문테이블2.getId(), OrderStatus.COMPLETION.name(), null,
                Arrays.asList(불고기버거세트주문, 치킨버거세트주문));
        Order changeOrder = createOrder(order.getId(), order.getOrderTableId(), OrderStatus.MEAL.name(), order.getOrderedTime(), order.getOrderLineItems());
        when(orderDao.findById(order.getId())).thenReturn(Optional.of(order));
        // when & then
        Assertions.assertThatThrownBy(
                () -> orderService.changeOrderStatus(order.getId(), changeOrder)
        ).isInstanceOf(IllegalArgumentException.class);

    }

}
