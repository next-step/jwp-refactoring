package kitchenpos.order;

import kitchenpos.application.OrderService;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.util.testFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @InjectMocks
    OrderService orderService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    private OrderLineItem 주문항목_1;
    private OrderLineItem 주문항목_2;
    private OrderLineItem 주문항목_3;
    private OrderTable 주문테이블_1;
    private OrderTable 주문테이블_2;
    private Order 주문_1;
    private Order 주문_2;

    @BeforeEach
    void setUp() {
        주문항목_1 = 주문항목_1_생성();
        주문항목_2 = 주문항목_2_생성();
        주문항목_3 = 주문항목_3_생성();
        주문테이블_1 = 주문테이블_1_생성();
        주문테이블_2 = 주문테이블_2_생성();
        주문_1 = 주문_1_생성(Arrays.asList(주문항목_1, 주문항목_2));
        주문_2 = 주문_2_생성(Arrays.asList(주문항목_3));
    }

    @DisplayName("주문 등록")
    @Test
    void createOrder() {
        // given
        when(menuDao.countByIdIn(Arrays.asList(주문항목_1.getMenuId(), 주문항목_2.getMenuId())))
                .thenReturn(2L);
        when(orderTableDao.findById(주문_1.getOrderTableId()))
                .thenReturn(Optional.ofNullable(주문테이블_1));
        when(orderDao.save(주문_1))
                .thenReturn(주문_1);
        when(orderLineItemDao.save(주문항목_1))
                .thenReturn(주문항목_1);
        when(orderLineItemDao.save(주문항목_2))
                .thenReturn(주문항목_2);

        // when
        Order result = orderService.create(주문_1);

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(주문_1.getId()),
                () -> assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertNotNull(result.getOrderedTime()),
                () -> assertThat(result.getOrderLineItems()).hasSize(2)
        );
    }

    @DisplayName("주문 등록 시 주문 항목 리스트가 없는 경우 등록 불가")
    @Test
    void createOrderAndOrderLineItemEmpty() {
        // given
        주문_1.setOrderLineItems(Collections.EMPTY_LIST);

        // then
        assertThatThrownBy(() -> {
            orderService.create(주문_1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 시 테이블이 미등록인 경우 등록 불가")
    @Test
    void createOrderAndNotExistTable() {
        // given
        when(menuDao.countByIdIn(Arrays.asList(주문항목_1.getMenuId(), 주문항목_2.getMenuId())))
                .thenReturn(2L);
        when(orderTableDao.findById(주문_1.getOrderTableId()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> {
            orderService.create(주문_1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 시 메뉴가 미등록인 경우 등록 불가")
    @Test
    void createOrderAndNotRegisterMenu() {
        // given
        when(menuDao.countByIdIn(Arrays.asList(주문항목_1.getMenuId(), 주문항목_2.getMenuId())))
                .thenReturn(1L);

        // then
        assertThatThrownBy(() -> {
            orderService.create(주문_1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 시 빈 테이블인 경우 등록 불가")
    @Test
    void createOrderAndEmptyTable() {
        // given
        주문테이블_1.setEmpty(true);
        when(menuDao.countByIdIn(Arrays.asList(주문항목_1.getMenuId(), 주문항목_2.getMenuId())))
                .thenReturn(2L);
        when(orderTableDao.findById(주문_1.getOrderTableId()))
                .thenReturn(Optional.ofNullable(주문테이블_1));

        // then
        assertThatThrownBy(() -> {
            orderService.create(주문_1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 전체 조회")
    @Test
    void findAllOrders() {
        // given
        when(orderDao.findAll())
                .thenReturn(Arrays.asList(주문_1, 주문_2));
        when(orderLineItemDao.findAllByOrderId(주문_1.getId()))
                .thenReturn(Arrays.asList(주문항목_1, 주문항목_2));
        when(orderLineItemDao.findAllByOrderId(주문_2.getId()))
                .thenReturn(Arrays.asList(주문항목_3));

        // when
        List<Order> list = orderService.list();

        // then
        assertAll(
                () -> assertThat(list).hasSize(2),
                () -> assertThat(list).containsExactly(주문_1, 주문_2)
        );
    }

    @DisplayName("주문 상태 변경")
    @Test
    void changeOrderStatusTest() {
        // given
        when(orderDao.findById(주문_1.getId()))
                .thenReturn(Optional.ofNullable(주문_1));
        주문_1.setOrderStatus(OrderStatus.MEAL.name());
        when(orderDao.save(주문_1))
                .thenReturn(주문_1);
        when(orderLineItemDao.findAllByOrderId(주문_1.getId()))
                .thenReturn(Arrays.asList(주문항목_1, 주문항목_2));

        // when
        Order result = orderService.changeOrderStatus(주문_1.getId(), 주문_1);

        // then
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문 상태 변경 시 주문 완료인 경우 변경 불가")
    @Test
    void changeOrderStatusAndIsOrderStatusCompletion() {
        // given
        when(orderDao.findById(주문_1.getId()))
                .thenReturn(Optional.ofNullable(주문_1));
        주문_1.setOrderStatus(OrderStatus.COMPLETION.name());

        // then
        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(주문_1.getId(), 주문_1);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
