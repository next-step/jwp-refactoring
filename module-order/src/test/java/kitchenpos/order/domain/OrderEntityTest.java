package kitchenpos.order.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderEntityTest {

  private long orderTableId;
  private OrderLineItemEntity orderLineItemEntity1;
  private OrderLineItemEntity orderLineItemEntity2;

  @BeforeEach
  void setUp() {
    orderTableId = 1L;
    orderLineItemEntity1 = new OrderLineItemEntity(1L, 2L);
    orderLineItemEntity2 = new OrderLineItemEntity(2L, 1L);
  }

  @DisplayName("주문테이블, 주문 항목을 입력받아 저장할 수 있다.")
  @Test
  void createTest() {
    //when
    OrderEntity orderEntity = new OrderEntity(orderTableId, Arrays.asList(orderLineItemEntity1, orderLineItemEntity2));

    //then
    assertAll(
        () -> assertThat(orderEntity.getOrderTableId()).isEqualTo(orderTableId),
        () -> assertThat(orderEntity.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
        () -> assertThat(orderEntity.getOrderedTime()).isNotNull(),
        () -> assertThat(orderEntity.getOrderLineItems()).contains(orderLineItemEntity1, orderLineItemEntity2)
    );
  }

  @DisplayName("주문 항목은 반드시 1개 이상이어야 한다.")
  @Test
  void createFailCauseEmptyOrderLineItemTest() {
    //when & then
    assertThatThrownBy(() -> new OrderEntity(orderTableId, Collections.emptyList()))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("주문 항목은 각기 다른 메뉴여야 한다.")
  @Test
  void createFailCauseDuplicateMenuTest() {
    //when & then
    assertThatThrownBy(() -> new OrderEntity(orderTableId, Arrays.asList(orderLineItemEntity1, orderLineItemEntity1)))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("주문 상태를 입력받아 바꿀 수 있다.")
  @Test
  void changeStatusTest() {
    // given
    OrderEntity orderEntity = new OrderEntity(orderTableId, Arrays.asList(orderLineItemEntity1, orderLineItemEntity2));

    //when
    orderEntity.changeStatus(OrderStatus.MEAL.name());

    //then
    assertThat(orderEntity.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
  }

  @DisplayName("완료 상태의 주문이 아니어야 상태를 변경할 수 있다.")
  @Test
  void changeStatusFailCauseCompleteStatusTest() {
    // given
    OrderEntity orderEntity = new OrderEntity(orderTableId, OrderStatus.COMPLETION, Arrays.asList(orderLineItemEntity1, orderLineItemEntity2));

    //when & then
    assertThatThrownBy(() -> orderEntity.changeStatus(OrderStatus.MEAL.name())).isInstanceOf(IllegalArgumentException.class);
  }

}
