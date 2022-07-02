package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

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
    private Order 수정된_주문;
    private OrderTable 테이블;
    private OrderTable 빈_테이블;
    private OrderLineItem 주문내역;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

        테이블 = new OrderTable(1L, 1L, 1, false);
        빈_테이블 = new OrderTable(2L, 2L, 0, true);
        주문내역 = new OrderLineItem(1L, 1L, 1L, 1);
    }

    @DisplayName("주문 생성")
    @Test
    void create() {
        // given
        주문 = new Order(1L, null, null, null, Collections.singletonList(주문내역));
        given(menuDao.countByIdIn(any())).willReturn(1L);
        given(orderTableDao.findById(any())).willReturn(Optional.of(테이블));
        given(orderDao.save(any())).willReturn(주문);
        given(orderLineItemDao.save(any())).willReturn(주문내역);

        // when
        Order order = orderService.create(주문);

        // then
        assertAll(
                () -> assertThat(order).isEqualTo(주문),
                () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name())
        );
    }

    @DisplayName("주문 목록 조회")
    @Test
    void list() {
        // given
        주문 = new Order(1L, null, null, null, Collections.singletonList(주문내역));
        given(orderDao.findAll()).willReturn(Collections.singletonList(주문));
        given(orderLineItemDao.findAllByOrderId(any())).willReturn(Collections.singletonList(주문내역));

        // when
        List<Order> orders = orderService.list();

        // then
        assertAll(
                () -> assertThat(orders).hasSize(1),
                () -> assertThat(orders).containsExactly(주문),
                () -> assertThat(orders.get(0).getOrderLineItems()).containsExactly(주문내역)
        );
    }

    @DisplayName("주문상태 변경")
    @Test
    void changeOrderStatus() {
        // given
        주문 = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now().minusMinutes(1), Collections.singletonList(주문내역));
        수정된_주문 = new Order(1L, 1L, OrderStatus.MEAL.name(), LocalDateTime.now(), Collections.singletonList(주문내역));
        given(orderDao.findById(any())).willReturn(Optional.of(주문));
        given(orderLineItemDao.findAllByOrderId(any())).willReturn(Collections.singletonList(주문내역));

        // when
        orderService.changeOrderStatus(수정된_주문.getId(), 수정된_주문);

        // then
        assertAll(
                () -> assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name())
        );
    }

    @Test
    void 주문의_개수가_0개인_경우() {
        // given
        주문 = new Order(1L, 1L, null, null, Collections.emptyList());

        // when & then
        assertThatThrownBy(() -> orderService.create(주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_내역의_건수가_주문한_메뉴들의_수와_같지_않은경우() {
        // given
        주문 = new Order(1L, 1L, null, null, Collections.singletonList(주문내역));
        given(menuDao.countByIdIn(any())).willReturn(3L);

        // when & then
        assertThatThrownBy(() -> orderService.create(주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 빈_테이블에서_주문한_경우() {
        // given
        주문 = new Order(1L, 1L, null, null, Collections.singletonList(주문내역));
        given(menuDao.countByIdIn(any())).willReturn(1L);
        given(orderTableDao.findById(주문.getOrderTableId())).willReturn(Optional.of(빈_테이블));


        // when & then
        assertThatThrownBy(() -> orderService.create(주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문상태가_완료인_경우에_상태_변경을_요청한_경우() {
        // given
        주문 = new Order(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now().minusMinutes(1), Collections.emptyList());
        수정된_주문 = new Order(1L, 1L, OrderStatus.MEAL.name(), LocalDateTime.now(), Collections.singletonList(주문내역));
        given(orderDao.findById(any())).willReturn(Optional.of(주문));

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(수정된_주문.getId(), 수정된_주문))
                .isInstanceOf(IllegalArgumentException.class);
    }
}