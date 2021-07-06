package kitchenpos.application;

import static kitchenpos.domain.OrderTableTest.*;
import static kitchenpos.domain.ProductTest.*;
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

    OrderLineItem 후라이드_한마리 = new OrderLineItem(후라이드.getId(), 1);
    OrderLineItem 양념치킨_한마리 = new OrderLineItem(양념치킨.getId(), 1);
    Order 양념_후라이드_각_한마리
        = new Order(100L, 테이블9_사용중.getId(), Arrays.asList(후라이드_한마리, 양념치킨_한마리));

    @BeforeEach
    void setUp() {
        후라이드_한마리 = new OrderLineItem(후라이드.getId(), 1);
        양념치킨_한마리 = new OrderLineItem(양념치킨.getId(), 1);
        양념_후라이드_각_한마리
            = new Order(100L, 테이블9_사용중.getId(), Arrays.asList(후라이드_한마리, 양념치킨_한마리));
    }

    @Test
    @DisplayName("주문을 생성한다")
    void create() {
        // given
        when(menuDao.countByIdIn(any())).thenReturn(2L);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(테이블9_사용중));
        when(orderDao.save(양념_후라이드_각_한마리)).thenReturn(양념_후라이드_각_한마리);
        when(orderLineItemDao.save(후라이드_한마리)).thenReturn(후라이드_한마리);
        when(orderLineItemDao.save(양념치킨_한마리)).thenReturn(양념치킨_한마리);

        // when
        Order savedOrder = orderService.create(양념_후라이드_각_한마리);

        // then
        assertThat(savedOrder.getId()).isEqualTo(양념_후라이드_각_한마리.getId());
        assertThat(savedOrder.getOrderTableId()).isEqualTo(양념_후라이드_각_한마리.getOrderTableId());
        assertThat(savedOrder.getOrderLineItems()).containsExactly(후라이드_한마리, 양념치킨_한마리);
    }

    @Test
    @DisplayName("주문 생성 실패(주문 항목 비어있음)")
    void create_failed1() {
        // given
        Order 주문내역이_없는_주문 = new Order(1L, 테이블9_사용중.getId(), Collections.emptyList());

        // then
        assertThatThrownBy(() -> orderService.create(주문내역이_없는_주문))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 생성 실패(항목 갯수 불일치")
    void create_failed2() {
        // given
        when(menuDao.countByIdIn(any())).thenReturn(4L);

        // then
        assertThatThrownBy(() -> orderService.create(양념_후라이드_각_한마리))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 생성 실패(테이블 없음)")
    void create_failed3() {
        // given
        when(menuDao.countByIdIn(any())).thenReturn(2L);
        when(orderTableDao.findById(any())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> orderService.create(양념_후라이드_각_한마리))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 생성 실패(테이블이 empty)")
    void create_failed4() {
        // given
        when(menuDao.countByIdIn(any())).thenReturn(2L);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(테이블1));

        // then
        assertThatThrownBy(() -> orderService.create(양념_후라이드_각_한마리))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 목록을 가져온다")
    void list() {
        // given
        Order 양념_후라이드_추가 = new Order(1L, 테이블9_사용중.getId(),
            OrderStatus.COOKING.name(), Arrays.asList(후라이드_한마리, 양념치킨_한마리));
        when(orderDao.findAll()).thenReturn(Arrays.asList(양념_후라이드_각_한마리, 양념_후라이드_추가));
        when(orderLineItemDao.findAllByOrderId(양념_후라이드_각_한마리.getId()))
            .thenReturn(양념_후라이드_각_한마리.getOrderLineItems());
        when(orderLineItemDao.findAllByOrderId(양념_후라이드_추가.getId()))
            .thenReturn(양념_후라이드_추가.getOrderLineItems());

        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders.size()).isEqualTo(2);
        assertThat(orders).containsExactly(양념_후라이드_각_한마리, 양념_후라이드_추가);
    }

    @Test
    @DisplayName("주문 상태를 변경한다")
    void changeOrderStatus() {
        // given
        Order 조리중인_주문 = 양념_후라이드_각_한마리;
        Order 식사중_상태 = new Order(OrderStatus.MEAL.name());
        when(orderDao.findById(any())).thenReturn(Optional.of(조리중인_주문));
        when(orderLineItemDao.findAllByOrderId(조리중인_주문.getId()))
            .thenReturn(조리중인_주문.getOrderLineItems());

        // when
        Order changedOrder = orderService.changeOrderStatus(조리중인_주문.getId(), 식사중_상태);

        // then
        assertThat(changedOrder.getId()).isEqualTo(조리중인_주문.getId());
        assertThat(changedOrder.getOrderStatus()).isEqualTo(식사중_상태.getOrderStatus());
    }

    @Test
    @DisplayName("주문 상태 변경 실패(주문이 존재하지 않음)")
    void changeOrderStatus_failed1() {
        // given
        Order 조리중인_주문 = 양념_후라이드_각_한마리;
        Order 식사중_상태 = new Order(OrderStatus.MEAL.name());
        when(orderDao.findById(any())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(조리중인_주문.getId(), 식사중_상태))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 상태 변경 실패(계산완료 상태)")
    void changeOrderStatus_failed2() {
        // given
        Order 계산완료_주문 = new Order(1L, 테이블9_사용중.getId(),
            OrderStatus.COMPLETION.name(), Arrays.asList(후라이드_한마리, 양념치킨_한마리));
        Order 식사중인_주문 = new Order(OrderStatus.MEAL.name());
        when(orderDao.findById(any())).thenReturn(Optional.of(계산완료_주문));

        // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(계산완료_주문.getId(), 식사중인_주문))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
