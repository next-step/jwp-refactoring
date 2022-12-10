package kitchenpos.application;

import static java.util.Collections.singletonList;
import static kitchenpos.domain.MenuGroupTestFixture.generateMenuGroup;
import static kitchenpos.domain.MenuProductTestFixture.generateMenuProduct;
import static kitchenpos.domain.MenuTestFixture.generateMenu;
import static kitchenpos.domain.OrderLineItemTestFixture.generateOrderLineItem;
import static kitchenpos.domain.OrderTableTestFixture.generateOrderTable;
import static kitchenpos.domain.OrderTestFixture.generateOrder;
import static kitchenpos.domain.ProductTestFixture.generateProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.Product;
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
    private OrderTableRepository orderTableRepository;

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
    private OrderTable 주문테이블A;
    private OrderTable 주문테이블B;
    private OrderLineItem 불고기버거세트주문;
    private OrderLineItem 치킨버거세트주문;
    private Order 주문A;
    private Order 주문B;

    @BeforeEach
    void setUp() {
        감자튀김 = generateProduct(1L, "감자튀김", BigDecimal.valueOf(3000L));
        콜라 = generateProduct(2L, "콜라", BigDecimal.valueOf(1500L));
        불고기버거 = generateProduct(3L, "불고기버거", BigDecimal.valueOf(4000L));
        치킨버거 = generateProduct(4L, "치킨버거", BigDecimal.valueOf(4500L));
        햄버거세트 = generateMenuGroup(1L, "햄버거세트");
        감자튀김상품 = generateMenuProduct(1L, null, 감자튀김, 1L);
        콜라상품 = generateMenuProduct(2L, null, 콜라, 1L);
        불고기버거상품 = generateMenuProduct(3L, null, 불고기버거, 1L);
        치킨버거상품 = generateMenuProduct(3L, null, 치킨버거, 1L);
        불고기버거세트 = generateMenu(1L, "불고기버거세트", BigDecimal.valueOf(8500L), 햄버거세트,
                Arrays.asList(감자튀김상품, 콜라상품, 불고기버거상품));
        치킨버거세트 = generateMenu(2L, "치킨버거세트", BigDecimal.valueOf(9000L), 햄버거세트,
                Arrays.asList(감자튀김상품, 콜라상품, 치킨버거상품));
        주문테이블A = generateOrderTable(1L, null, 5, false);
        주문테이블B = generateOrderTable(2L, null, 7, false);
        불고기버거세트주문 = generateOrderLineItem(1L, null, 불고기버거세트.getId(), 2);
        치킨버거세트주문 = generateOrderLineItem(2L, null, 치킨버거세트.getId(), 1);
        주문A = generateOrder(주문테이블A.getId(), null, null, Arrays.asList(불고기버거세트주문, 치킨버거세트주문));
        주문B = generateOrder(주문테이블B.getId(), null, null, singletonList(불고기버거세트주문));
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void createOrder() {
        // given
        List<Long> menuIds = 주문A.getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        given(menuRepository.countByIdIn(menuIds)).willReturn((long) menuIds.size());
        given(orderTableRepository.findById(주문A.getOrderTableId())).willReturn(Optional.of(주문테이블A));
        given(orderDao.save(주문A)).willReturn(주문A);
        given(orderLineItemDao.save(불고기버거세트주문)).willReturn(불고기버거세트주문);
        given(orderLineItemDao.save(치킨버거세트주문)).willReturn(치킨버거세트주문);

        // when
        Order order = orderService.create(주문A);

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
    void createOrderThrowErrorWhenOrderLineItemIsEmpty() {
        // given
        Order order = generateOrder(주문테이블A.getId(), null, null, null);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("주문 생성 시, 주문 항목 내에 등록되지 않은 메뉴가 있다면 예외가 발생한다.")
    @Test
    void createOrderThrowErrorWhenOrderLineItemHasUnregisteredMenu() {
        // given
        List<Long> menuIds = 주문A.getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        given(menuRepository.countByIdIn(menuIds)).willReturn(0L);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(주문A));
    }

    @DisplayName("등록되어 있지않은 주문 테이블을 가진 주문은 생성될 수 없다.")
    @Test
    void createOrderThrowErrorWhenOrderTableIsNotExists() {
        // given
        List<Long> menuIds = 주문A.getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        given(menuRepository.countByIdIn(menuIds)).willReturn((long) menuIds.size());
        given(orderTableRepository.findById(주문A.getOrderTableId())).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(주문A));
    }

    @DisplayName("주문 테이블이 비어있을 경우 주문은 생성될 수 없다.")
    @Test
    void createOrderThrowErrorWhenOrderTableIsEmpty() {
        // given
        OrderTable orderTable = generateOrderTable(4L, null, 6, true);
        Order order = generateOrder(orderTable.getId(), null, null, singletonList(불고기버거세트주문));
        List<Long> menuIds = order.getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        given(menuRepository.countByIdIn(menuIds)).willReturn((long) menuIds.size());
        given(orderTableRepository.findById(order.getOrderTableId())).willReturn(Optional.of(orderTable));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("주문 전체 목록을 조회한다.")
    @Test
    void findAllOrders() {
        // given
        List<Order> orders = Arrays.asList(주문A, 주문B);
        given(orderDao.findAll()).willReturn(orders);
        for(Order order: orders) {
            given(orderLineItemDao.findAllByOrderId(order.getId())).willReturn(order.getOrderLineItems());
        }

        // when
        List<Order> findOrders = orderService.list();

        // then
        assertAll(
                () -> assertThat(findOrders).hasSize(orders.size()),
                () -> assertThat(findOrders).containsExactly(주문A, 주문B)
        );
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // given
        String expectOrderStatus = OrderStatus.MEAL.name();
        Order changeOrder = generateOrder(주문B.getId(), 주문B.getOrderTableId(), expectOrderStatus, 주문B.getOrderedTime(), 주문B.getOrderLineItems());
        given(orderDao.findById(주문B.getId())).willReturn(Optional.of(주문B));
        given(orderDao.save(주문B)).willReturn(주문B);

        // when
        Order resultOrder = orderService.changeOrderStatus(주문B.getId(), changeOrder);

        // then
        assertThat(resultOrder.getOrderStatus()).isEqualTo(expectOrderStatus);
    }

    @DisplayName("등록되지 않은 주문에 대해 주문 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatusThrowErrorWhenOrderIsNotExists() {
        // given
        Long notExistsOrderId = 5L;
        given(orderDao.findById(notExistsOrderId)).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.changeOrderStatus(notExistsOrderId, 주문B));
    }

    @DisplayName("이미 완료된 주문이면 주문 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatusThrowErrorWhenOrderStatusAlreadyComplete() {
        // given
        Order order = generateOrder(주문테이블B.getId(), OrderStatus.COMPLETION.name(), null,
                Arrays.asList(불고기버거세트주문, 치킨버거세트주문));
        Order changeOrder = generateOrder(order.getId(), order.getOrderTableId(), OrderStatus.MEAL.name(), order.getOrderedTime(), order.getOrderLineItems());
        given(orderDao.findById(order.getId())).willReturn(Optional.of(order));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.changeOrderStatus(order.getId(), changeOrder));
    }
}
