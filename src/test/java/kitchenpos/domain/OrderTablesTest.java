package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.Collections;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테이블 일급 콜렉션 태스트")
class OrderTablesTest {


    @DisplayName("단체 지정 시 주문 테이블이 없으면 예외가 발생한다.")
    @Test
    void registerTableGroupException() {
        OrderTables orderTables = OrderTables.from(Collections.emptyList());

        Assertions.assertThatThrownBy(() -> orderTables.registerTableGroup(TableGroup.createEmpty()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 시 주문 테이블이 2개 미만이면 예외가 발생한다.")
    @Test
    void registerTableGroupException2() {
        OrderTables orderTables = OrderTables.from(
                Collections.singletonList(OrderTable.of(10, true))
        );

        Assertions.assertThatThrownBy(() -> orderTables.registerTableGroup(TableGroup.createEmpty()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 시 주문 테이블 하나라도 empty 상태가 아니면 예외가 발생한다.")
    @Test
    void registerTableGroupException3() {
        OrderTables orderTables = OrderTables.from(
                Collections.singletonList(OrderTable.of(10, false))
        );

        Assertions.assertThatThrownBy(() -> orderTables.registerTableGroup(TableGroup.createEmpty()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 시 주문 테이블 하나라도 이미 단체지정이 되어있으면 예외가 발생한다.")
    @Test
    void registerTableGroupException4() {
        OrderTables orderTables = OrderTables.from(
                Collections.singletonList(OrderTable.of(1L, 10, true))
        );

        Assertions.assertThatThrownBy(() -> orderTables.registerTableGroup(TableGroup.createEmpty()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블들을 단체 지정한다.")
    @Test
    void registerTableGroup() {
        OrderTables orderTables = OrderTables.from(
                Arrays.asList(
                        OrderTable.of(10, true),
                        OrderTable.of(5, true)
                )
        );

        orderTables.registerTableGroup(TableGroup.createEmpty());

        assertAll(
                () -> assertThat(orderTables.getOrderTables().get(0).isEmpty()).isFalse(),
                () -> assertThat(orderTables.getOrderTables().get(1).isEmpty()).isFalse()
        );
    }

    @DisplayName("주문 상태가 조리인 주문 테이블이 하나라도 있으면 단체 지정 취소 시 예외가 발생한다.")
    @Test
    void ungroupException() {
        OrderTable 주문_테이블1 = OrderTable.of(10, true);
        Order.of(주문_테이블1, Arrays.asList(OrderLineItem.of(1L, 2)));
        OrderTable 주문_테이블2 = OrderTable.of(10, true);
        Order.of(주문_테이블2, Arrays.asList(OrderLineItem.of(2L, 2)));

        OrderTables orderTables = OrderTables.from(Arrays.asList(주문_테이블1, 주문_테이블2));

        Assertions.assertThatThrownBy(orderTables::unGroup)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태가 식사인 주문 테이블이 하나라도 있으면 단체 지정 취소 시 예외가 발생한다.")
    @Test
    void ungroupException2() {
        OrderTable 주문_테이블1 = OrderTable.of(10, true);
        Order 주문1 = Order.of(주문_테이블1, Arrays.asList(OrderLineItem.of(1L, 2)));
        주문1.setOrderStatus(OrderStatus.COMPLETION.name());

        OrderTable 주문_테이블2 = OrderTable.of(10, true);
        Order 주문2 = Order.of(주문_테이블2, Arrays.asList(OrderLineItem.of(2L, 2)));
        주문2.setOrderStatus(OrderStatus.MEAL.name());

        OrderTables orderTables = OrderTables.from(Arrays.asList(주문_테이블1, 주문_테이블2));

        Assertions.assertThatThrownBy(orderTables::unGroup)
                .isInstanceOf(IllegalArgumentException.class);
    }
}
