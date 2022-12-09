package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("주문 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    private Order 주문;
    private OrderLineItem 주문_항목;

    @BeforeEach
    void set_up() {
        주문_항목 = OrderLineItemFixture.create(1L, 1L, 1L, 2L);
        주문 = OrderFixture.create(
                1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), Arrays.asList(주문_항목)
        );
    }

    @DisplayName("주문을 등록할 수 있다.")
    @Test
    void create() {
        // given
        when(menuDao.countByIdIn(any())).thenReturn((long) 주문.getOrderLineItems().size());
        when(orderTableDao.findById(any())).thenReturn(Optional.of(new OrderTable()));
        when(orderDao.save(any())).thenReturn(주문);

        // when
        Order 주문_생성_결과 = orderService.create(주문);

        // then
        assertThat(주문_생성_결과).isEqualTo(주문);
    }

    @DisplayName("주문 항목이 없으면 주문할 수 없다.")
    @Test
    void create_error_without_order_item() {
        // given
        주문.setOrderLineItems(null);

        // when && then
        assertThatThrownBy(() -> orderService.create(주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목의 메뉴의 개수가 일치하지 않으면 주문 할 수 없다.")
    @Test
    void create_error_duplicated_order_item() {
        // given
        when(menuDao.countByIdIn(any())).thenReturn(100L);

        // when && then
        assertThatThrownBy(() -> orderService.create(주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 값이 저장되어 있지 않으면 주문 할 수 없다.")
    @Test
    void create_error_order_table_empty() {
        // given
        when(menuDao.countByIdIn(any())).thenReturn((long) 주문.getOrderLineItems().size());
        when(orderTableDao.findById(any())).thenReturn(Optional.empty());

        // when && then
        assertThatThrownBy(() -> orderService.create(주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 값이 비어있다면 주문 할 수 없다.")
    @Test
    void create_error_order_table_value_empty() {
        // given
        OrderTable 주문_테이블 = new OrderTable();
        주문_테이블.setEmpty(true);
        when(menuDao.countByIdIn(any())).thenReturn((long) 주문.getOrderLineItems().size());
        when(orderTableDao.findById(any())).thenReturn(Optional.of(주문_테이블));

        // when && then
        assertThatThrownBy(() -> orderService.create(주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 목록을 조회할 수 있다.")
    @Test
    void list() {
        when(orderDao.findAll()).thenReturn(Arrays.asList(주문));
        when(orderLineItemDao.findAllByOrderId(any())).thenReturn(Arrays.asList(주문_항목));

        List<Order> 주문_목록_조회_결과 = orderService.list();

        assertAll(
                () -> assertThat(주문_목록_조회_결과).hasSize(1),
                () -> assertThat(주문_목록_조회_결과).containsExactly(주문)
        );
    }

    @DisplayName("주문의 상태를 변경할 수 있다.")
    @Test
    void update_order_status() {
        // given
        Order 주문상태_식사중_변경 = new Order();
        주문상태_식사중_변경.setOrderStatus(OrderStatus.MEAL.name());

        when(orderDao.findById(any())).thenReturn(Optional.of(주문));
        when(orderLineItemDao.findAllByOrderId(any())).thenReturn(Arrays.asList(주문_항목));

        // when
        Order 주문_상태_변경_결과 = orderService.changeOrderStatus(주문상태_식사중_변경.getId(), 주문상태_식사중_변경);

        // then
        assertThat(주문_상태_변경_결과.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문이 저장되어 있지 않으면 주문의 상태를 변경할 수 없다.")
    @Test
    void update_fail_order_status_not_save_order() {
        // given
        Order 주문상태_식사중_변경 = new Order();
        주문상태_식사중_변경.setOrderStatus(OrderStatus.MEAL.name());

        assertThatThrownBy(() -> orderService.changeOrderStatus(주문상태_식사중_변경.getId(), 주문상태_식사중_변경))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 상태가 완료인 경우 주문의 상태를 변경할 수 없다.")
    @Test
    void update_fail_not_change_order_complete() {
        // given
        주문.setOrderStatus(OrderStatus.COMPLETION.name());
        Order 주문상태_식사중_변경 = new Order();
        주문상태_식사중_변경.setOrderStatus(OrderStatus.MEAL.name());

        when(orderDao.findById(any())).thenReturn(Optional.of(주문));

        // when && then
        assertThatThrownBy(() -> orderService.changeOrderStatus(주문상태_식사중_변경.getId(), 주문상태_식사중_변경))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
