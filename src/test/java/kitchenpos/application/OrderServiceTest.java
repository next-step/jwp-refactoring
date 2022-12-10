package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
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
import static kitchenpos.domain.MenuProductTestFixture.createMenuProduct;
import static kitchenpos.domain.MenuTestFixture.createMenu;
import static kitchenpos.domain.OrderLineItemTestFixture.createOrderLineItem;
import static kitchenpos.domain.OrderTableTestFixture.createOrderTable;
import static kitchenpos.domain.OrderTestFixture.createOrder;
import static kitchenpos.domain.ProductTestFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("주문 관련 서비스 테스트")
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

    private Product 짜장면;
    private Product 짬뽕;
    private Product 단무지;
    private Product 탕수육;
    private MenuGroup 중국집_1인_메뉴_세트;
    private MenuProduct 짜장면상품;
    private MenuProduct 짬뽕상품;
    private MenuProduct 단무지상품;
    private MenuProduct 탕수육상품;
    private Menu 짜장면_탕수육_1인_메뉴_세트;
    private Menu 짬뽕_탕수육_1인_메뉴_세트;
    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;
    private OrderLineItem 짜장면_탕수육_1인_메뉴_세트주문;
    private OrderLineItem 짬뽕_탕수육_1인_메뉴_세트주문;
    private Order 주문1;
    private Order 주문2;

    @BeforeEach
    public void setUp() {
        중국집_1인_메뉴_세트 = createMenuGroup(1L, "중국집_1인_메뉴_세트");
        짜장면 = createProduct(1L, "짜장면", BigDecimal.valueOf(8000L));
        탕수육 = createProduct(2L, "탕수육", BigDecimal.valueOf(12000L));
        짬뽕 = createProduct(3L, "짬뽕", BigDecimal.valueOf(9000L));
        단무지 = createProduct(4L, "단무지", BigDecimal.valueOf(0L));
        짜장면상품 = createMenuProduct(1L, null, 짜장면.getId(), 1L);
        탕수육상품 = createMenuProduct(2L, null, 탕수육.getId(), 1L);
        짬뽕상품 = createMenuProduct(3L, null, 짬뽕.getId(), 1L);
        단무지상품 = createMenuProduct(4L, null, 단무지.getId(), 1L);
        짜장면_탕수육_1인_메뉴_세트 = createMenu(1L, "짜장면_탕수육_1인_메뉴_세트", BigDecimal.valueOf(20000L), 중국집_1인_메뉴_세트.getId(), Arrays.asList(짜장면상품, 탕수육상품, 단무지상품));
        짬뽕_탕수육_1인_메뉴_세트 = createMenu(2L, "짬뽕_탕수육_1인_메뉴_세트", BigDecimal.valueOf(21000L), 중국집_1인_메뉴_세트.getId(), Arrays.asList(짬뽕상품, 탕수육상품, 단무지상품));
        주문테이블1 = createOrderTable(1L, null, 10, false);
        주문테이블2 = createOrderTable(2L, null, 20, false);
        짜장면_탕수육_1인_메뉴_세트주문 = createOrderLineItem(1L, null, 짜장면_탕수육_1인_메뉴_세트.getId(), 1);
        짬뽕_탕수육_1인_메뉴_세트주문 = createOrderLineItem(2L, null, 짬뽕_탕수육_1인_메뉴_세트.getId(), 1);
        주문1 = createOrder(주문테이블1.getId(), null, null, Arrays.asList(짜장면_탕수육_1인_메뉴_세트주문, 짬뽕_탕수육_1인_메뉴_세트주문));
        주문2 = createOrder(주문테이블2.getId(), null, null, singletonList(짜장면_탕수육_1인_메뉴_세트주문));
    }

    @DisplayName("주문 생성 작업을 성공한다.")
    @Test
    void create() {
        // given
        List<Long> menuIds = 주문1.getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        when(menuDao.countByIdIn(menuIds)).thenReturn((long) menuIds.size());
        when(orderTableDao.findById(주문1.getOrderTableId())).thenReturn(Optional.of(주문테이블1));
        when(orderDao.save(주문1)).thenReturn(주문1);
        when(orderLineItemDao.save(짜장면_탕수육_1인_메뉴_세트주문)).thenReturn(짜장면_탕수육_1인_메뉴_세트주문);
        when(orderLineItemDao.save(짬뽕_탕수육_1인_메뉴_세트주문)).thenReturn(짬뽕_탕수육_1인_메뉴_세트주문);

        // when
        Order order = orderService.create(주문1);

        // then
        assertAll(
                () -> assertThat(order.getOrderedTime()).isNotNull(),
                () -> assertThat(order.getId()).isEqualTo(짜장면_탕수육_1인_메뉴_세트주문.getOrderId()),
                () -> assertThat(order.getId()).isEqualTo(짬뽕_탕수육_1인_메뉴_세트주문.getOrderId())
        );
    }

    @DisplayName("주문을 생성할 떄, 주문 항목이 비어있으면 IllegalArgumentException을 반환한다.")
    @Test
    void createWithException1() {
        // given
        Order order = createOrder(주문테이블1.getId(), null, null, null);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("주문을 생성할 때, 주문 항목 내에 등록되지 않은 메뉴가 있다면 IllegalArgumentException을 반환한다.")
    @Test
    void createWithException2() {
        // given
        when(menuDao.countByIdIn(any())).thenReturn(0L);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(주문1));
    }

    @DisplayName("주문을 생성할 때, 주문 테이블이 등록되어 있지 않으면 IllegalArgumentException을 반환한다.")
    @Test
    void createWithException3() {
        // given
        List<Long> menuIds = 주문1.getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        when(menuDao.countByIdIn(menuIds)).thenReturn((long) menuIds.size());
        when(orderTableDao.findById(주문1.getOrderTableId())).thenReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(주문1));
    }

    @DisplayName("주문 목록 조회 작업을 성공한다.")
    @Test
    void list() {
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

    @DisplayName("주문 상태 변경을 성공한다.")
    @Test
    void changeOrderStatus() {
        // given
        주문2.setOrderStatus(OrderStatus.MEAL.name());
        when(orderDao.findById(주문2.getId())).thenReturn(Optional.of(주문2));
        when(orderDao.save(주문2)).thenReturn(주문2);

        // when
        Order order = orderService.changeOrderStatus(주문2.getId(), 주문2);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(주문2.getOrderStatus());
    }

    @DisplayName("주문 상태를 변경할 때, 주문이 등록되어 있지 않으면 IllegalArgumentException을 반환한다.")
    @Test
    void changeOrderWithException1() {
        // given
        Order 미등록_주문 = createOrder(10L, null, null, singletonList(짜장면_탕수육_1인_메뉴_세트주문));
        when(orderDao.findById(미등록_주문.getId())).thenReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.changeOrderStatus(미등록_주문.getId(), 미등록_주문));
    }

    @DisplayName("주문 상태를 변경할 때, 주문이 이미 완료되어 있으면 IllegalArgumentException을 반환한다.")
    @Test
    void changeOrderWithException2() {
        // given
        주문2.setOrderStatus(OrderStatus.COMPLETION.name());
        when(orderDao.findById(주문2.getId())).thenReturn(Optional.of(주문2));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.changeOrderStatus(주문2.getId(), 주문2));
    }
}
