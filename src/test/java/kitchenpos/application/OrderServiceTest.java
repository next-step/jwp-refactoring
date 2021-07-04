package kitchenpos.application;

import static kitchenpos.utils.DataInitializer.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.utils.DataInitializer;

@ExtendWith(MockitoExtension.class)
@DisplayName("주문 서비스")
class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    MenuDao menuDao;
    @Mock
    OrderDao orderDao;
    @Mock
    OrderLineItemDao orderLineItemDao;
    @Mock
    OrderTableDao orderTableDao;

    OrderLineItem 신규_주문_치쏘세트;
    OrderLineItem 신규_주문_피맥세트;

    Order 신규_주문;

    @BeforeEach
    void setUp() {
        DataInitializer.reset();

        신규_주문_치쏘세트 = new OrderLineItem(치쏘세트.getId(), 2);
        신규_주문_피맥세트 = new OrderLineItem(피맥세트.getId(), 1);

        신규_주문 = new Order(1L, 테이블1번_USING.getId(),
            Arrays.asList(신규_주문_치쏘세트, 신규_주문_피맥세트));
    }

    @Test
    @DisplayName("주문을 생성한다")
    void create() {
        // given
        when(menuDao.countByIdIn(any())).thenReturn(2L);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(테이블1번_USING));
        when(orderDao.save(신규_주문)).thenReturn(신규_주문);
        when(orderLineItemDao.save(신규_주문_치쏘세트)).thenReturn(신규_주문_치쏘세트);
        when(orderLineItemDao.save(신규_주문_피맥세트)).thenReturn(신규_주문_피맥세트);

        // when
        Order savedOrder = orderService.create(신규_주문);

        // then
        assertThat(savedOrder.getId()).isEqualTo(신규_주문.getId());
        assertThat(savedOrder.getOrderTableId()).isEqualTo(신규_주문.getOrderTableId());
        assertThat(savedOrder.getOrderLineItems()).containsExactly(신규_주문_치쏘세트, 신규_주문_피맥세트);
    }

    @Test
    @DisplayName("주문 생성 실패(주문 항목 비어있음)")
    void create_failed1() {
        // given
        신규_주문 = new Order(1L, 테이블1번_USING.getId(), Collections.emptyList());

        // then
        assertThatThrownBy(() -> orderService.create(신규_주문))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 생성 실패(항목 갯수 불일치")
    void create_failed2() {
        // given
        when(menuDao.countByIdIn(any())).thenReturn(4L);

        // then
        assertThatThrownBy(() -> orderService.create(신규_주문))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 생성 실패(테이블 없음)")
    void create_failed3() {
        // given
        when(menuDao.countByIdIn(any())).thenReturn(2L);
        when(orderTableDao.findById(any())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> orderService.create(신규_주문))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 생성 실패(테이블이 empty)")
    void create_failed4() {
        // given
        when(menuDao.countByIdIn(any())).thenReturn(2L);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(테이블3번_EMPTY));

        // then
        assertThatThrownBy(() -> orderService.create(신규_주문))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 목록을 가져온다")
    void list() {
        // given
        when(orderDao.findAll()).thenReturn(Arrays.asList(주문_테이블1_결제완료, 주문_테이블2_식사중));
        when(orderLineItemDao.findAllByOrderId(주문_테이블1_결제완료.getId()))
            .thenReturn(주문_테이블1_결제완료.getOrderLineItems());
        when(orderLineItemDao.findAllByOrderId(주문_테이블2_식사중.getId()))
            .thenReturn(주문_테이블2_식사중.getOrderLineItems());

        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders.size()).isEqualTo(2);
        assertThat(orders).containsExactly(주문_테이블1_결제완료, 주문_테이블2_식사중);
    }

    @Test
    @DisplayName("주문 상태를 변경한다")
    void changeOrderStatus() {
        // given
        Order 조리중인_주문 = new Order(1L, 테이블2번_USING.getId(), OrderStatus.COOKING.name(),
            Arrays.asList(신규_주문_치쏘세트, 신규_주문_피맥세트));
        Order 식사중인_주문 = new Order(OrderStatus.MEAL.name());
        when(orderDao.findById(any())).thenReturn(Optional.of(조리중인_주문));
        when(orderLineItemDao.findAllByOrderId(조리중인_주문.getId()))
            .thenReturn(조리중인_주문.getOrderLineItems());

        // when
        Order changedOrder = orderService.changeOrderStatus(조리중인_주문.getId(), 식사중인_주문);

        // then
        assertThat(changedOrder.getId()).isEqualTo(조리중인_주문.getId());
        assertThat(changedOrder.getOrderStatus()).isEqualTo(식사중인_주문.getOrderStatus());
    }

    @Test
    @DisplayName("주문 상태 변경 실패(주문이 존재하지 않음)")
    void changeOrderStatus_failed1() {
        // given
        Order 조리중인_주문 = new Order(1L, 테이블2번_USING.getId(), OrderStatus.COOKING.name(),
            Arrays.asList(신규_주문_치쏘세트, 신규_주문_피맥세트));
        Order 식사중인_주문 = new Order(OrderStatus.MEAL.name());
        when(orderDao.findById(any())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(조리중인_주문.getId(), 식사중인_주문))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 상태 변경 실패(계산완료 상태)")
    void changeOrderStatus_failed2() {
        // given
        Order 계산완료_주문 = new Order(1L, 테이블2번_USING.getId(), OrderStatus.COMPLETION.name(),
            Arrays.asList(신규_주문_치쏘세트, 신규_주문_피맥세트));
        Order 식사중인_주문 = new Order(OrderStatus.MEAL.name());
        when(orderDao.findById(any())).thenReturn(Optional.of(계산완료_주문));

        // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(계산완료_주문.getId(), 식사중인_주문))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
