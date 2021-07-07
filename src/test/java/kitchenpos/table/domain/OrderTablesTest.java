package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import kitchenpos.order.domain.Order;
import kitchenpos.product.constant.OrderStatus;

public class OrderTablesTest {

    @ParameterizedTest
    @DisplayName("OrderTabe 중 비지 않았거나, tableGroup이 존재한다면 false를 반환한다.")
    @MethodSource("resultSet")
    void avaliableTable(List<OrderTable> list, Boolean result) {
        OrderTables orderTables = new OrderTables(list);
        assertThat(orderTables.avaliableTable()).isEqualTo(result);
    }

    @ParameterizedTest
    @DisplayName("개별 테이블의 주문상태가 조리중이거나 식사중이면  exception을 반환한다.")
    @MethodSource("cookingSet")
    void ungroup(OrderTables orderTables) {
        assertThrows(IllegalArgumentException.class, () -> {
            orderTables.ungroup();
        });
    }

    private static Stream<Arguments> resultSet() {
        List<OrderTable> list = new ArrayList<>();
        list.add(new OrderTable(1L, 10, false));
        list.add(new OrderTable(1L, 10, true));

        List<OrderTable> list2 = new ArrayList<>();
        list2.add(new OrderTable(1L, new TableGroup(2L, LocalDateTime.now()), 10, true));
        list2.add(new OrderTable(1L, 10, true));

        List<OrderTable> list3 = new ArrayList<>();
        list3.add(new OrderTable(1L, 10, true));
        list3.add(new OrderTable(1L, 10, true));

        return Stream.of(Arguments.of(list, false), Arguments.of(list2, false), Arguments.of(list3, true));
    }

    private static Stream<Arguments> cookingSet() {
        Order isCooking = new Order(1L, OrderStatus.COOKING, null, null);
        OrderTables orderTablesCooking = new OrderTables(
            Arrays.asList(
                new OrderTable(1L, new TableGroup(1L, LocalDateTime.now()), 10, false, Arrays.asList(isCooking))));

        Order isMeal = new Order(1L, OrderStatus.MEAL, null, null);
        OrderTables orderTablesMeal = new OrderTables(
            Arrays
                .asList(new OrderTable(1L, new TableGroup(1L, LocalDateTime.now()), 10, false, Arrays.asList(isMeal))));

        return Stream.of(Arguments.of(orderTablesCooking), Arguments.of(orderTablesMeal));
    }
}
