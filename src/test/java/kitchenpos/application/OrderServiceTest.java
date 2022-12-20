package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@DisplayName("주문 관련 비즈니스 기능 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
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

    private Product 삼겹살;
    private Product 김치;
    private MenuGroup 한식;
    private Menu 삼겹살세트메뉴;
    private MenuProduct 삼겹살메뉴상품;
    private MenuProduct 김치메뉴상품;
    private OrderTable 주문테이블;
    private Order 주문;
    private OrderLineItem 삼겹살세트메뉴주문;

    @BeforeEach
    void setUp() {
        삼겹살 = new Product(1L, "삼겹살", BigDecimal.valueOf(5_000));
        김치 = new Product(2L, "김치", BigDecimal.valueOf(3_000));
        한식 = new MenuGroup(1L, "한식");
        삼겹살세트메뉴 = new Menu(1L, "삼겹살세트메뉴", BigDecimal.valueOf(8_000), 한식.getId(), new ArrayList<>());
        삼겹살메뉴상품 = new MenuProduct(1L, 삼겹살세트메뉴.getId(), 삼겹살.getId(), 1L);
        김치메뉴상품 = new MenuProduct(2L, 삼겹살세트메뉴.getId(), 김치.getId(), 1L);
        삼겹살세트메뉴.setMenuProducts(Arrays.asList(삼겹살메뉴상품, 김치메뉴상품));
        주문테이블 = new OrderTable(1L, null, 0, false);
        주문 = new Order(1L, 주문테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), new ArrayList<>());
        삼겹살세트메뉴주문 = new OrderLineItem(1L, 주문.getId(), 삼겹살세트메뉴.getId(), 1);
        주문.setOrderLineItems(Arrays.asList(삼겹살세트메뉴주문));
    }

    @DisplayName("주문 생성 테스트")
    @Test
    void createOrderTest() {
        // given
        List<Long> menuIds = 주문.getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        when(menuDao.countByIdIn(menuIds)).thenReturn((long) menuIds.size());
        when(orderTableDao.findById(주문.getOrderTableId())).thenReturn(Optional.of(주문테이블));
        when(orderDao.save(주문)).thenReturn(주문);
        when(orderLineItemDao.save(삼겹살세트메뉴주문)).thenReturn(삼겹살세트메뉴주문);

        // when
        Order result = orderService.create(주문);

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(주문.getId()),
                () -> assertThat(result.getOrderStatus()).isEqualTo(주문.getOrderStatus())
        );
    }

    @DisplayName("주문 생성 테스트 - 주문 테이블이 등록되지 않은 경우")
    @Test
    void createOrderTest2() {
        // given
        List<Long> menuIds = 주문.getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        when(menuDao.countByIdIn(menuIds)).thenReturn((long) menuIds.size());
        when(orderTableDao.findById(주문.getOrderTableId())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> orderService.create(주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 테스트 - 주문 항목 메뉴가 등록되지 않은 경우")
    @Test
    void createOrderTest3() {
        // given
        when(menuDao.countByIdIn(anyList())).thenReturn(10L);

        // when & then
        assertThatThrownBy(() -> orderService.create(주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 테스트 - 주문 항목 메뉴가 빈 값인 경우")
    @Test
    void createOrderTest4() {
        // given
        주문 = new Order(1L, 주문테이블.getId(), null, LocalDateTime.now(), new ArrayList<>());

        // when & then
        assertThatThrownBy(() -> orderService.create(주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 테스트 - 오더 테이블이 Empty인 경우")
    @Test
    void createOrderTest5() {
        // given
        주문테이블 = new OrderTable(1L, null, 0, true);
        List<Long> menuIds = 주문.getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        when(menuDao.countByIdIn(menuIds)).thenReturn((long) menuIds.size());
        when(orderTableDao.findById(주문.getOrderTableId())).thenReturn(Optional.of(주문테이블));

        // when & then
        assertThatThrownBy(() -> orderService.create(주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태 수정 테스트")
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

    @DisplayName("주문 상태 수정 테스트 - 등록되지 않은 주문의 상태를 수정하는 경우")
    @Test
    void updateOrderStatus2() {
        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(3L, 주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태 수정 테스트 - 계산 완료된 상태의 주문을 수정하는 경우")
    @Test
    void updateOrderStatus3() {
        // given
        주문.setOrderStatus(OrderStatus.COMPLETION.name());
        Order updatedOrder = new Order(
                주문.getId(),
                주문.getOrderTableId(),
                OrderStatus.MEAL.name(),
                주문.getOrderedTime(),
                주문.getOrderLineItems()
        );
        when(orderDao.findById(주문.getId())).thenReturn(Optional.of(주문));

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), updatedOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 목록 조회 테스트")
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