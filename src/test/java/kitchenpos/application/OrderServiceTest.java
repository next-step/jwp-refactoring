package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

  @DisplayName("주문테이블, 주문 항목을 입력받아 저장할 수 있다.")
  @Test
  void createTest() {
    //given
    long savedMenu1Id = 1L;
    long savedOrderId = 1L;
    OrderLineItem orderLineItem = new OrderLineItem(savedMenu1Id, 2);
    OrderTable orderTable = new OrderTable(1L, 1L, 4, false);
    Order order = new Order(orderTable.getId(), Arrays.asList(orderLineItem));
    when(menuDao.countByIdIn(Arrays.asList(savedMenu1Id))).thenReturn(1L);
    when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));
    orderLineItem.setOrderId(savedOrderId);
    OrderLineItem savedOrderLineItem = new OrderLineItem(1L, savedOrderId, savedMenu1Id, 2);
    when(orderLineItemDao.save(orderLineItem)).thenReturn(savedOrderLineItem);
    when(orderDao.save(order)).thenReturn(new Order(savedOrderId, orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.of(2021,6,29,3,45), Arrays.asList(savedOrderLineItem)));
    OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

    //when
    Order savedOrder = orderService.create(order);

    //then
    assertAll(
        () -> assertThat(savedOrder.getId()).isNotNull(),
        () -> assertThat(savedOrder.getOrderTableId()).isEqualTo(order.getOrderTableId()),
        () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
        () -> assertThat(savedOrder.getOrderedTime()).isNotNull(),
        () -> assertThat(savedOrder.getOrderLineItems()).containsExactly(savedOrderLineItem)
    );
    verify(menuDao, VerificationModeFactory.times(1)).countByIdIn(Arrays.asList(savedMenu1Id));
    verify(orderTableDao, VerificationModeFactory.times(1)).findById(orderTable.getId());
    verify(orderLineItemDao, VerificationModeFactory.times(1)).save(orderLineItem);
    verify(orderDao, VerificationModeFactory.times(1)).save(order);
  }

  @DisplayName("주문 항목은 반드시 1개 이상이어야 한다.")
  @Test
  void createFailCauseEmptyOrderLineItemTest() {
    //given
    Order order = new Order(1L, Collections.emptyList());
    OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

    //when & then
    assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("주문 항목은 각기 다른 메뉴여야 한다.")
  @Test
  void createFailCauseDuplicateMenuTest() {
    //given
    OrderLineItem duplicateMenuItem1 = new OrderLineItem(1L, 2);
    OrderLineItem duplicateMenuItem2 = new OrderLineItem(1L, 3);
    Order order = new Order(1L, Arrays.asList(duplicateMenuItem1, duplicateMenuItem2));
    OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

    //when & then
    assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("주문테이블은 존재하는 주문테이블만 지정할 수 있다.")
  @Test
  void createFailCauseNotExistOrderTableTest() {
    //given
    long notExistOrderTableId = 1L;
    OrderLineItem orderLineItem = new OrderLineItem(1L, 2);
    Order order = new Order(notExistOrderTableId, Arrays.asList(orderLineItem));
    when(menuDao.countByIdIn(Arrays.asList(orderLineItem.getMenuId()))).thenReturn(1L);
    when(orderTableDao.findById(notExistOrderTableId)).thenReturn(Optional.empty());
    OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

    //when & then
    assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("주문테이블이 주문을 등록할 수 있는 테이블이어야 한다.")
  @Test
  void createFailCauseOrderTableIsEmptyTableTest() {
    //given
    long emptyTableId = 1L;
    OrderLineItem orderLineItem = new OrderLineItem(1L, 2);
    Order order = new Order(emptyTableId, Arrays.asList(orderLineItem));
    when(menuDao.countByIdIn(Arrays.asList(orderLineItem.getMenuId()))).thenReturn(1L);
    when(orderTableDao.findById(emptyTableId)).thenReturn(Optional.of(new OrderTable(1L, 1L, 4, true)));
    OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

    //when & then
    assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("전체 주문 목록을 조회할 수 있다.")
  @Test
  void findAllTest() {
    //given
    long savedOrder1Id = 1L;
    long savedOrder2Id = 2L;
    OrderLineItem savedOrderLineItem1 = new OrderLineItem(1L, savedOrder1Id, 1L, 2);
    OrderLineItem savedOrderLineItem2 = new OrderLineItem(2L, savedOrder2Id, 2L, 4);
    Order order1 = new Order(savedOrder1Id, 1L, OrderStatus.COOKING.name(), LocalDateTime.of(2021, 6, 29, 3, 45), Arrays.asList(savedOrderLineItem1));
    Order order2 = new Order(savedOrder2Id, 1L, OrderStatus.COOKING.name(), LocalDateTime.of(2021, 6, 29, 3, 45), Arrays.asList(savedOrderLineItem2));
    when(orderDao.findAll()).thenReturn(Arrays.asList(order1, order2));
    when(orderLineItemDao.findAllByOrderId(order1.getId())).thenReturn(Arrays.asList(savedOrderLineItem1));
    when(orderLineItemDao.findAllByOrderId(order2.getId())).thenReturn(Arrays.asList(savedOrderLineItem2));
    OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

    //when
    List<Order> orderList = orderService.list();

    //then
    assertThat(orderList).containsExactly(order1, order2);
    verify(orderDao, VerificationModeFactory.times(1)).findAll();
    verify(orderLineItemDao, VerificationModeFactory.times(2)).findAllByOrderId(anyLong());
  }

  @DisplayName("주문  ID와 주문 상태를 입력받아 주문의 현재 상태를 변경할 수 있다.")
  @Test
  void changeOrderStatusTest() {
    //given
    long savedOrderId = 1L;
    OrderLineItem savedOrderLineItem = new OrderLineItem(1L, savedOrderId, 1L, 2);
    Order order = new Order(savedOrderId, 1L, OrderStatus.COOKING.name(), LocalDateTime.of(2021, 6, 29, 3, 45), Arrays.asList(savedOrderLineItem));
    when(orderDao.findById(savedOrderId)).thenReturn(Optional.of(order));
    Order savedOrder = new Order(savedOrderId, 1L, OrderStatus.MEAL.name(), LocalDateTime.of(2021, 6, 29, 3, 45), Arrays.asList(savedOrderLineItem));
    when(orderDao.save(any())).thenReturn(savedOrder);
    when(orderLineItemDao.findAllByOrderId(savedOrderId)).thenReturn(Arrays.asList(savedOrderLineItem));
    OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

    //when
    Order changeParameter = new Order();
    changeParameter.setOrderStatus(OrderStatus.MEAL.name());
    Order changedOrder = orderService.changeOrderStatus(savedOrderId, changeParameter);

    //then
    assertThat(changedOrder).isEqualTo(savedOrder);
    verify(orderDao, VerificationModeFactory.times(1)).findById(savedOrderId);
    verify(orderDao, VerificationModeFactory.times(1)).save(any());
    verify(orderLineItemDao, VerificationModeFactory.times(1)).findAllByOrderId(savedOrderId);
  }

  @DisplayName("존재하는 주문만 상태를 변경할 수 있다.")
  @Test
  void changeOrderStatusFailCauseNotExistOrderTest() {
    //given
    long notExistOrderId = 1L;
    when(orderDao.findById(notExistOrderId)).thenReturn(Optional.empty());
    OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

    //when & then
    assertThatThrownBy(() -> orderService.changeOrderStatus(notExistOrderId, new Order())).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("완료 상태의 주문이 아니어야 상태를 변경할 수 있다.")
  @Test
  void changeOrderStatusFailCauseCompleteOrderTest() {
    //given
    long completeOrderId = 1L;
    OrderLineItem savedOrderLineItem = new OrderLineItem(1L, completeOrderId, 1L, 2);
    when(orderDao.findById(completeOrderId)).thenReturn(Optional.of(new Order(completeOrderId, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.of(2021, 6, 29, 3, 45), Arrays.asList(savedOrderLineItem))));
    OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

    //when & then
    assertThatThrownBy(() -> orderService.changeOrderStatus(completeOrderId, new Order())).isInstanceOf(IllegalArgumentException.class);
  }
}
