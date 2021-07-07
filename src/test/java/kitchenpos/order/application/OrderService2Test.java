package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.OrderEntity;
import kitchenpos.order.domain.OrderLineItemEntity;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderService2Test {

  @Mock
  private MenuRepository menuRepository;

  @Mock
  private OrderRepository orderRepository;

  //TODO : TableRepository 로 리팩토링 후 변경
  @Mock
  private OrderTableDao orderTableDao;

  private OrderTable orderTable;
  private OrderLineItemEntity orderLineItemEntity1;
  private OrderLineItemEntity orderLineItemEntity2;
  private OrderRequest.OrderLineItemRequest orderLineItemRequest1;
  private OrderRequest.OrderLineItemRequest orderLineItemRequest2;

  private OrderService2 orderService;

  @BeforeEach
  void setUp() {
    orderTable = new OrderTable(1L, 1L, 4, false);
    orderLineItemEntity1 = new OrderLineItemEntity(1L, 1L, 2L);
    orderLineItemEntity2 = new OrderLineItemEntity(2L, 2L, 1L);
    orderLineItemRequest1 = new OrderRequest.OrderLineItemRequest(1L, 2L);
    orderLineItemRequest2 = new OrderRequest.OrderLineItemRequest(2L, 1L);

    orderService = new OrderService2(menuRepository, orderRepository, orderTableDao);
  }

  @DisplayName("주문테이블, 주문 항목을 입력받아 저장할 수 있다.")
  @Test
  void createTest() {
    //given
    OrderRequest orderRequest = new OrderRequest(orderTable.getId(), Arrays.asList(orderLineItemRequest1, orderLineItemRequest2));
    when(menuRepository.countByIdIn(Arrays.asList(orderLineItemRequest1.getMenuId(), orderLineItemRequest2.getMenuId())))
          .thenReturn(2L);
    when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));
    when(orderRepository.save(any())).thenReturn(new OrderEntity(1L, orderTable.getId(), Arrays.asList(orderLineItemEntity1, orderLineItemEntity2)));

    //when
    OrderResponse savedOrder = orderService.create(orderRequest);

    //then
    assertAll(
        () -> assertThat(savedOrder.getId()).isPositive(),
        () -> assertThat(savedOrder.getOrderTableId()).isEqualTo(orderTable.getId()),
        () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
        () -> assertThat(savedOrder.getOrderedTime()).isNotNull(),
        () -> assertThat(savedOrder.getOrderLineItems()).contains(OrderResponse.OrderLineItemResponse.from(orderLineItemEntity1), OrderResponse.OrderLineItemResponse.from(orderLineItemEntity2))
    );
    verify(menuRepository, VerificationModeFactory.times(1)).countByIdIn(Arrays.asList(orderLineItemRequest1.getMenuId(), orderLineItemRequest2.getMenuId()));
    verify(orderTableDao, VerificationModeFactory.times(1)).findById(orderTable.getId());
    verify(orderRepository, VerificationModeFactory.times(1)).save(any());
  }

  @DisplayName("주문 항목은 반드시 1개 이상이어야 한다.")
  @Test
  void createFailCauseEmptyOrderLineItemTest() {
    //given
    OrderRequest emptyMenuRequest = new OrderRequest(orderTable.getId(), Collections.emptyList());


    //when & then
    assertThatThrownBy(() -> orderService.create(emptyMenuRequest)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("주문 항목은 각기 다른 메뉴여야 한다.")
  @Test
  void createFailCauseDuplicateMenuTest() {
    //given
    OrderRequest duplicatedMenuRequest = new OrderRequest(orderTable.getId(), Arrays.asList(orderLineItemRequest1, orderLineItemRequest1));
    when(menuRepository.countByIdIn(Arrays.asList(orderLineItemRequest1.getMenuId(), orderLineItemRequest1.getMenuId())))
        .thenReturn(1L);

    //when & then
    assertThatThrownBy(() -> orderService.create(duplicatedMenuRequest)).isInstanceOf(IllegalArgumentException.class);
    verify(menuRepository, VerificationModeFactory.times(1)).countByIdIn(Arrays.asList(orderLineItemRequest1.getMenuId(), orderLineItemRequest1.getMenuId()));
  }

  @DisplayName("주문테이블은 존재하는 주문테이블만 지정할 수 있다.")
  @Test
  void createFailCauseNotExistOrderTableTest() {
    //given
    long notExistTableId = 999L;
    OrderRequest notExistTableRequest = new OrderRequest(notExistTableId, Arrays.asList(orderLineItemRequest1, orderLineItemRequest2));
    when(menuRepository.countByIdIn(Arrays.asList(orderLineItemRequest1.getMenuId(), orderLineItemRequest2.getMenuId())))
        .thenReturn(2L);
    when(orderTableDao.findById(notExistTableId)).thenReturn(Optional.empty());

    //when & then
    assertThatThrownBy(() -> orderService.create(notExistTableRequest)).isInstanceOf(IllegalArgumentException.class);
    verify(menuRepository, VerificationModeFactory.times(1)).countByIdIn(Arrays.asList(orderLineItemRequest1.getMenuId(), orderLineItemRequest2.getMenuId()));
    verify(orderTableDao, VerificationModeFactory.times(1)).findById(notExistTableId);
  }

  @DisplayName("주문테이블이 주문을 등록할 수 있는 테이블이어야 한다.")
  @Test
  void createFailCauseOrderTableIsEmptyTableTest() {
    //given
    OrderTable emptyTable = new OrderTable(1L, 1L, 4, true);
    OrderRequest emptyTableRequest = new OrderRequest(emptyTable.getId(), Arrays.asList(orderLineItemRequest1, orderLineItemRequest2));
    when(menuRepository.countByIdIn(Arrays.asList(orderLineItemRequest1.getMenuId(), orderLineItemRequest2.getMenuId())))
        .thenReturn(2L);
    when(orderTableDao.findById(emptyTable.getId())).thenReturn(Optional.of(emptyTable));

    //when & then
    assertThatThrownBy(() -> orderService.create(emptyTableRequest)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("전체 주문 목록을 조회할 수 있다.")
  @Test
  void findAllTest() {
    //given
    OrderEntity orderEntity1 = new OrderEntity(1L, 1L, Collections.singletonList(orderLineItemEntity1));
    OrderEntity orderEntity2 = new OrderEntity(2L, 2L, Collections.singletonList(orderLineItemEntity2));
    when(orderRepository.findAll()).thenReturn(Arrays.asList(orderEntity1, orderEntity2));

    //when
    List<OrderResponse> orderList = orderService.findAllOrders();

    //then
    assertThat(orderList).contains(OrderResponse.from(orderEntity1), OrderResponse.from(orderEntity2));
    verify(orderRepository, VerificationModeFactory.times(1)).findAll();
  }

  @DisplayName("주문  ID와 주문 상태를 입력받아 주문의 현재 상태를 변경할 수 있다.")
  @Test
  void changeOrderStatusTest() {
    //given
    long savedOrderId = 1L;
    OrderEntity savedOrder = new OrderEntity(savedOrderId, orderTable.getId(), Arrays.asList(orderLineItemEntity1, orderLineItemEntity2));
    when(orderRepository.findById(savedOrderId)).thenReturn(Optional.of(savedOrder));
    OrderStatusRequest request = new OrderStatusRequest("MEAL");


    //when
    OrderResponse orderResponse = orderService.changeOrderStatus(savedOrderId, request);

    //then
    assertAll(
        () -> assertThat(orderResponse.getId()).isEqualTo(savedOrder.getId()),
        () -> assertThat(orderResponse.getOrderTableId()).isEqualTo(savedOrder.getOrderTableId()),
        () -> assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name())
    );
    verify(orderRepository, VerificationModeFactory.times(1)).findById(savedOrderId);
  }

  @DisplayName("존재하는 주문만 상태를 변경할 수 있다.")
  @Test
  void changeOrderStatusFailCauseNotExistOrderTest() {
    //given
    long notExistOrderId = 1L;
    when(orderRepository.findById(notExistOrderId)).thenReturn(Optional.empty());
    OrderStatusRequest request = new OrderStatusRequest("MEAL");

    //when & then
    assertThatThrownBy(() -> orderService.changeOrderStatus(notExistOrderId, request)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("완료 상태의 주문이 아니어야 상태를 변경할 수 있다.")
  @Test
  void changeOrderStatusFailCauseCompleteOrderTest() {
    //given
    long completeOrderId = 1L;
    OrderEntity savedOrder = new OrderEntity(completeOrderId, orderTable.getId(), OrderStatus.COMPLETION, Arrays.asList(orderLineItemEntity1, orderLineItemEntity2));
    when(orderRepository.findById(completeOrderId)).thenReturn(Optional.of(savedOrder));
    OrderStatusRequest request = new OrderStatusRequest("MEAL");

    //when & then
    assertThatThrownBy(() -> orderService.changeOrderStatus(completeOrderId, request)).isInstanceOf(IllegalArgumentException.class);
  }
}
