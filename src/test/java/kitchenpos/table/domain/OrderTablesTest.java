package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class OrderTablesTest {

    @ParameterizedTest
    @DisplayName("OrderTabe 중 비지 않았거나, tableGroup이 존재한다면 false를 반환한다.")
    @MethodSource("resultSet")
    void avaliableTable(List<OrderTable> list, boolean result) {
        OrderTables orderTables = new OrderTables(list);
        assertThat(orderTables.avaliableTable()).isEqualTo(result);
    }

    private static Stream<Arguments> resultSet() {
        List<OrderTable> list = new ArrayList<>();
        list.add(new OrderTable(1L, 10, false));
        list.add(new OrderTable(1L, 10, true));

        List<OrderTable> list2 = new ArrayList<>();
        list2.add(new OrderTable(1L, 2L, 10, true));
        list2.add(new OrderTable(1L, 10, true));

        List<OrderTable> list3 = new ArrayList<>();
        list3.add(new OrderTable(1L, 10, true));
        list3.add(new OrderTable(1L, 10, true));

        return Stream.of(
            Arguments.of(list, false),
            Arguments.of(list2, false),
            Arguments.of(list3, true));
    }
}
