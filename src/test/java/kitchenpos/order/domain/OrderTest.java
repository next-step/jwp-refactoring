package kitchenpos.order.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static kitchenpos.menu.domain.MenuTest.치킨세트;
import static kitchenpos.order.domain.OrderLineItemTest.와퍼_세트_주문;
import static kitchenpos.order.domain.OrderLineItemTest.콜라_주문;
import static kitchenpos.table.domain.OrderTableTest.이인석;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class OrderTest {
    public static final Order 주문통합 = 주문생성();

    private static Order 주문생성() {
        Order order = new Order(
                1L
                , 이인석
                , OrderStatus.COOKING
                , LocalDateTime.now());
        order.addOrderItem(와퍼_세트_주문);
        order.addOrderItem(콜라_주문);

        return order;
    }

    private Order 이인석주문;

    @BeforeEach
    void setUp() {
        이인석주문 = new Order(이인석, OrderStatus.COOKING);
    }


    @Test
    @DisplayName("주문 생성")
    public void create() {
        // given
        // when
        // then
        assertThat(이인석주문).isEqualTo(new Order(이인석, OrderStatus.COOKING));
    }

    @Test
    @DisplayName("주문 상태 변경")
    public void changeOrderStatusTest() {
        // given
        OrderStatus meal = OrderStatus.MEAL;
        // when
        이인석주문.changeOrderStatus(meal);
        // then
        assertThat(이인석주문.getOrderStatus()).isEqualTo(meal);
    }

    @Test
    @DisplayName("주문 항목 추가")
    public void addOrderItemTest() {
        // given
        OrderLineItem 이인분_세트 = new OrderLineItem(치킨세트, 2);
        // when
        이인석주문.addOrderItem(이인분_세트);
        // then
        assertAll(
                () -> assertThat(이인석주문.getOrderLineItems()).hasSize(1),
                () -> assertThat(이인석주문.getOrderLineItems()).contains(이인분_세트),
                () -> assertThat(이인분_세트.getOrder()).isEqualTo(이인석주문)
        );
    }

    private static Stream<Arguments> isCompletionTestParam() {
        return Stream.of(
                Arguments.of(OrderStatus.COMPLETION, true),
                Arguments.of(OrderStatus.COOKING, false),
                Arguments.of(OrderStatus.MEAL, false)
        );
    }

    @ParameterizedTest
    @MethodSource("isCompletionTestParam")
    @DisplayName("계산 완료인지 확인함")
    public void isCompletionTest(OrderStatus status,  boolean result) {
        // given
        Order order = new Order(이인석, status);
        // when
        boolean actual = order.isCompletion();
        // then
        assertThat(actual).isEqualTo(result);
    }

}
