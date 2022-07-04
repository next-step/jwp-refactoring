package kitchenpos.application;

import static kitchenpos.utils.generator.MenuFixtureGenerator.메뉴_생성;
import static kitchenpos.utils.generator.MenuGroupFixtureGenerator.메뉴_그룹_생성;
import static kitchenpos.utils.generator.OrderFixtureGenerator.주문_생성;
import static kitchenpos.utils.generator.OrderTableFixtureGenerator.비어있지_않은_주문_테이블_생성;
import static kitchenpos.utils.generator.ProductFixtureGenerator.상품_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.Optional;
import kitchenpos.application.order.OrderService;
import kitchenpos.dao.order.OrderDao;
import kitchenpos.dao.order.OrderLineItemDao;
import kitchenpos.dao.table.OrderTableDao;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.utils.generator.OrderFixtureGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service:Order")
class OrderServiceTest {

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

    private Menu menu;
    private MenuGroup menuGroup;
    private Product firstProduct, secondProduct;

    private OrderTable orderTable;
    private Order order;

    @BeforeEach
    void setUp() {
        firstProduct = 상품_생성();
        secondProduct = 상품_생성();
        menuGroup = 메뉴_그룹_생성();
        menu = 메뉴_생성(menuGroup, firstProduct, secondProduct);

        orderTable = 비어있지_않은_주문_테이블_생성();
        order = OrderFixtureGenerator.주문_생성(orderTable, menu);
    }

    @Test
    @DisplayName("주문을 생성한다.")
    void createOrder() {
        // Given
        given(menuRepository.countByIdIn(anyList())).willReturn((long) order.getOrderLineItems().size());
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));
        given(orderDao.save(any(Order.class))).will(AdditionalAnswers.returnsFirstArg());

        // When
        orderService.create(order);

        // Then
        verify(menuRepository).countByIdIn(anyList());
        verify(orderTableDao).findById(any());
        verify(orderDao).save(any(Order.class));
        verify(orderLineItemDao, times(order.getOrderLineItems().size())).save(any(OrderLineItem.class));
    }

    @Test
    @DisplayName("주문 항목이 없는 주문을 생성하는 경우 예외 발생 검증")
    public void throwException_WhenOrderLineItemIsEmpty() {
        // Given
        order.setOrderLineItems(Collections.emptyList());

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderService.create(order));
    }

    @Test
    @DisplayName("존재하지 않는 메뉴를 주문한 경우 예외 발생 검증")
    public void throwException_WhenOrderMenuCountIsOverThanPersistMenusCount() {
        // Given
        given(menuRepository.countByIdIn(anyList())).willReturn(0L);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderService.create(order));

        verify(menuRepository).countByIdIn(anyList());
    }

    @Test
    @DisplayName("주문 테이블이 존재하지 않는 주문을 생성하는 경우 예외 발생 검증")
    public void throwException_WhenOrderTableIsNotExist() {
        // Given
        given(menuRepository.countByIdIn(anyList())).willReturn((long) order.getOrderLineItems().size());
        given(orderTableDao.findById(any())).willThrow(IllegalArgumentException.class);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderService.create(order));

        verify(menuRepository).countByIdIn(anyList());
        verify(orderTableDao).findById(any());
    }

    @Test
    @DisplayName("주문에 포함된 주문테이블이 비어있는경우(주문을 요청한 테이블이 `isEmpty() = true`인 경우) 예외가 발생 검증")
    public void throwException_When() {
        given(menuRepository.countByIdIn(anyList())).willReturn((long) order.getOrderLineItems().size());
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));
        orderTable.setEmpty(true);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderService.create(order));

        verify(menuRepository).countByIdIn(anyList());
        verify(orderTableDao).findById(any());
    }

    @Test
    @DisplayName("주문 목록을 조회한다.")
    public void getAllOrders() {
        // Given
        given(orderDao.findAll()).willReturn(Collections.singletonList(order));

        // When
        orderService.list();

        // Then
        verify(orderDao).findAll();
        verify(orderLineItemDao).findAllByOrderId(any());
    }

    @Test
    @DisplayName("주문 상태를 변경한다.")
    public void changeOrderStatus() {
        // Given
        given(orderDao.findById(any())).willReturn(Optional.of(order));

        Order updateOrderRequest = new Order();
        String newOrderStatus = OrderStatus.MEAL.name();
        updateOrderRequest.setOrderStatus(newOrderStatus);

        // When
        Order actualOrder = orderService.changeOrderStatus(order.getId(), updateOrderRequest);

        // Then
        verify(orderDao).findById(any());
        verify(orderDao).save(any(Order.class));
        verify(orderLineItemDao).findAllByOrderId(any());
        assertThat(actualOrder.getOrderStatus()).isEqualTo(newOrderStatus);
    }

    @Test
    @DisplayName("존재하지 않는 주문의 주문 상태를 수정하는 경우 예외 발생 검증")
    public void throwException_WhenOrderIsNotExist() {
        // Given
        given(orderDao.findById(any())).willThrow(IllegalArgumentException.class);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderService.changeOrderStatus(any(), order));

        verify(orderDao).findById(any());
    }

    @Test
    @DisplayName("주문 상태 값이 없는 주문의 주문 상태를 수정하는 경우 예외 발생 검증")
    public void throwException_WhenOrderStatusIsNull() {
        // Given
        Order order = new Order();
        given(orderDao.findById(any())).willReturn(Optional.of(order));

        // When & Then
        assertThatExceptionOfType(NullPointerException.class)
            .isThrownBy(() -> orderService.changeOrderStatus(order.getId(), order));

        verify(orderDao).findById(any());
    }

    @Test
    @DisplayName("완료 상태인 주문의 주문 상태를 수정하는 경우 예외 발생 검증")
    public void throwException_WhenOrderStatusIsCompletion() {
        // Given
        Order order = new Order();
        order.setOrderStatus(OrderStatus.COMPLETION.name());

        given(orderDao.findById(any())).willReturn(Optional.of(order));

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderService.changeOrderStatus(any(), order));

        verify(orderDao).findById(any());
    }
}
