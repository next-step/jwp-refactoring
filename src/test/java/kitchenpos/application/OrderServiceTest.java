package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
    private OrderService service;

    private OrderLineItem 첫번째_주문항목;
    private OrderLineItem 두번째_주문항목;
    private List<OrderLineItem> 주문항목목록;
    private OrderTable 일번테이블;

    @BeforeEach
    void setUp() {
        첫번째_주문항목 = new OrderLineItem(1L, null, 1L, 1L);
        두번째_주문항목 = new OrderLineItem(2L, null, 2L, 3L);
        주문항목목록 = Arrays.asList(첫번째_주문항목, 두번째_주문항목);
        일번테이블 = new OrderTable(1L, null, 2, false);
    }

    @DisplayName("주문 등록 성공")
    @Test
    void create() {
        //given
        Order 주문 = new Order(1L, 1L, null, LocalDateTime.now(), 주문항목목록);
        when(menuDao.countByIdIn(Arrays.asList(첫번째_주문항목.getSeq(), 두번째_주문항목.getSeq()))).thenReturn(2L);
        when(orderTableDao.findById(주문.getOrderTableId())).thenReturn(Optional.of(일번테이블));
        when(orderDao.save(주문)).thenReturn(주문);
        when(orderLineItemDao.save(첫번째_주문항목)).thenReturn(첫번째_주문항목);
        when(orderLineItemDao.save(두번째_주문항목)).thenReturn(두번째_주문항목);

        //when
        Order result = service.create(주문);

        //then
        assertAll(
                () -> assertThat(result.getOrderTableId()).isEqualTo(주문.getOrderTableId()),
                () -> assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(result.getOrderLineItems()).contains(첫번째_주문항목, 두번째_주문항목),
                () -> assertThat(
                        result.getOrderLineItems().stream().findFirst().map(OrderLineItem::getOrderId).get()).isEqualTo(
                        result.getId())
        );
    }

    @DisplayName("주문 항목 없는 주문 등록")
    @Test
    void createWithoutMenu() {
        //given
        Order 주문 = new Order(1L, 1L, null, LocalDateTime.now(), new ArrayList<>());

        //when & then
        assertThatThrownBy(() -> service.create(주문)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("시스템 상에 등록되지 않은 메뉴가 포함된 주문 등록")
    @Test
    void createWithNotExistsMenu() {
        //given
        Order 주문 = new Order(1L, 1L, null, LocalDateTime.now(), 주문항목목록);
        when(menuDao.countByIdIn(Arrays.asList(첫번째_주문항목.getSeq(), 두번째_주문항목.getSeq()))).thenReturn(1L);

        //when & then
        assertThatThrownBy(() -> service.create(주문)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("시스템 상에 등록되지 않은 테이블에 지정된 주문 등록")
    @Test
    void createWithNotExistsTable() {
        //given
        Order 주문 = new Order(1L, 2L, null, LocalDateTime.now(), 주문항목목록);
        when(menuDao.countByIdIn(Arrays.asList(첫번째_주문항목.getSeq(), 두번째_주문항목.getSeq()))).thenReturn(2L);
        when(orderTableDao.findById(주문.getOrderTableId())).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> service.create(주문)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("비어있는 테이블에 지정된 주문 등록")
    @Test
    void createWithEmptyTable() {
        //given
        OrderTable 빈테이블 = new OrderTable(2L, null, 2, true);
        Order 주문 = new Order(1L, 빈테이블.getId(), null, LocalDateTime.now(), 주문항목목록);
        when(menuDao.countByIdIn(Arrays.asList(첫번째_주문항목.getSeq(), 두번째_주문항목.getSeq()))).thenReturn(2L);
        when(orderTableDao.findById(주문.getOrderTableId())).thenReturn(Optional.of(빈테이블));

        //when & then
        assertThatThrownBy(() -> service.create(주문)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 주문 조회")
    @Test
    void list() {
        //given
        OrderLineItem 세번째_주문항목 = new OrderLineItem(3L, null, 3L, 2L);
        Order 첫번째주문 = new Order(1L, 1L, null, LocalDateTime.now(), 주문항목목록);
        Order 두번째주문 = new Order(2L, 2L, null, LocalDateTime.now(), Collections.singletonList(세번째_주문항목));
        when(orderDao.findAll()).thenReturn(Arrays.asList(첫번째주문, 두번째주문));
        when(orderLineItemDao.findAllByOrderId(첫번째주문.getId())).thenReturn(Arrays.asList(첫번째_주문항목, 두번째_주문항목));
        when(orderLineItemDao.findAllByOrderId(두번째주문.getId())).thenReturn(Collections.singletonList(세번째_주문항목));

        //when
        List<Order> orders = service.list();

        //then
        assertAll(
                () -> assertThat(orders).contains(첫번째주문, 두번째주문),
                () -> assertThat(orders.stream().filter(order -> order.equals(첫번째주문)).findFirst().get()
                        .getOrderLineItems()).contains(첫번째_주문항목, 두번째_주문항목),
                () -> assertThat(orders.stream().filter(order -> order.equals(두번째주문)).findFirst().get()
                        .getOrderLineItems()).contains(세번째_주문항목)
        );
    }

    @DisplayName("주문의 상태 변경 성공")
    @Test
    void changeOrderStatus() {
        //given
        Order 주문 = new Order(1L, 2L, null, LocalDateTime.now(), 주문항목목록);
        Order 식사중주문 = new Order(null, null, OrderStatus.MEAL.name(), null, null);
        when(orderDao.findById(주문.getId())).thenReturn(Optional.of(주문));
        when(orderDao.save(주문)).thenReturn(주문);
        when(orderLineItemDao.findAllByOrderId(주문.getId())).thenReturn(주문항목목록);

        //when
        Order result = service.changeOrderStatus(주문.getId(), 식사중주문);

        //then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(주문.getId()),
                () -> assertThat(result.getOrderStatus()).isEqualTo(식사중주문.getOrderStatus())
        );
    }

    @DisplayName("시스템상에 등록되지 않은 주문의 상태 변경")
    @Test
    void changeOrderStatusWithNotExistsOrder() {
        //given
        Order 주문 = new Order(1L, 2L, null, LocalDateTime.now(), 주문항목목록);
        Order 식사중주문 = new Order(null, null, OrderStatus.MEAL.name(), null, null);
        when(orderDao.findById(주문.getId())).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> service.changeOrderStatus(주문.getId(), 식사중주문)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @DisplayName("주문 상태가 계산 완료인 주문의 상태 변경")
    @Test
    void changeOrderStatusWithCompletedOrder() {
        //given
        Order 주문 = new Order(1L, 2L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), 주문항목목록);
        Order 식사중주문 = new Order(null, null, OrderStatus.MEAL.name(), null, null);
        when(orderDao.findById(주문.getId())).thenReturn(Optional.of(주문));

        //when & then
        assertThatThrownBy(() -> service.changeOrderStatus(주문.getId(), 식사중주문)).isInstanceOf(
                IllegalArgumentException.class);
    }
}
