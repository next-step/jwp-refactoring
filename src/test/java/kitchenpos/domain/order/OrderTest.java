package kitchenpos.domain.order;

import static kitchenpos.utils.generator.OrderTableFixtureGenerator.비어있는_주문_테이블_생성;
import static kitchenpos.utils.generator.OrderTableFixtureGenerator.비어있지_않은_주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.utils.generator.ScenarioTestFixtureGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Domain:Order")
class OrderTest extends ScenarioTestFixtureGenerator {

    private OrderTable 비어있지_않은_주문_테이블_생성, 비어있는_주문_테이블_생성;

    @BeforeEach
    public void setUp() {
        비어있지_않은_주문_테이블_생성 = 비어있지_않은_주문_테이블_생성();
        비어있는_주문_테이블_생성 = 비어있는_주문_테이블_생성();
    }

    @Test
    @DisplayName("주문 시간이 없는 경우 예외 발생 검증")
    public void throwException_WhenOrderedDateIsNull() {
        // Given
        OrderLineItem firstOrderLineItem = new OrderLineItem(고기_더블_더블_메뉴, 1);
        OrderLineItem secondOrderLineItem = new OrderLineItem(커플_냉삼_메뉴, 1);

        // When
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new Order(비어있지_않은_주문_테이블_생성, Arrays.asList(firstOrderLineItem, secondOrderLineItem), null));
    }

    @Test
    @DisplayName("주문 항목이 없는 경우 예외 발생 검증")
    public void throwException_WhenOrderLineItemIsEmpty() {
        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new Order(비어있지_않은_주문_테이블_생성, Collections.emptyList(), LocalDateTime.now()));
    }

    @Test
    @DisplayName("비어있는 주문 테이블의 주문 생성 시 예외 발생 검증")
    public void throwException_WhenOrderTableIsEmpty() {
        // Given
        OrderLineItem firstOrderLineItem = new OrderLineItem(고기_더블_더블_메뉴, 1);
        OrderLineItem secondOrderLineItem = new OrderLineItem(커플_냉삼_메뉴, 1);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(
                () -> new Order(비어있는_주문_테이블_생성, Arrays.asList(firstOrderLineItem, secondOrderLineItem), LocalDateTime.now()));
    }

    @Test
    @DisplayName("주문 상태 변경")
    public void changeOrderStatus() {
        // Given
        OrderLineItem firstOrderLineItem = new OrderLineItem(고기_더블_더블_메뉴, 1);
        Order givenOrder = new Order(비어있지_않은_주문_테이블_생성, Collections.singletonList(firstOrderLineItem), LocalDateTime.now());
        OrderStatus changeRequestOrderStatus = OrderStatus.MEAL;

        // When
        givenOrder.changeOrderStatus(changeRequestOrderStatus);

        // Then
        assertThat(givenOrder.getOrderStatus()).isEqualTo(changeRequestOrderStatus);
    }

    @Test
    @DisplayName("주문 상태를 null로 변경 시 예외 발생 검증")
    public void throwException_WhenRequestOrderStatusIsNull() {
        // Given
        OrderLineItem firstOrderLineItem = new OrderLineItem(고기_더블_더블_메뉴, 1);
        Order givenOrder = new Order(비어있지_않은_주문_테이블_생성, Collections.singletonList(firstOrderLineItem), LocalDateTime.now());

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> givenOrder.changeOrderStatus(null));
    }

    @Test
    @DisplayName("이미 완료된 주문의 주문 상태를 변경하는 경우 예외 발생")
    public void throwException_WhenRequestOrderStatusIsAlreadyCompletion() {
        // Given
        OrderLineItem firstOrderLineItem = new OrderLineItem(고기_더블_더블_메뉴, 1);
        Order givenOrder = new Order(비어있지_않은_주문_테이블_생성, Collections.singletonList(firstOrderLineItem), LocalDateTime.now());
        givenOrder.changeOrderStatus(OrderStatus.COMPLETION);

        // When & Then
        OrderStatus changeRequestOrderStatus = OrderStatus.MEAL;
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> givenOrder.changeOrderStatus(changeRequestOrderStatus));
    }
}
