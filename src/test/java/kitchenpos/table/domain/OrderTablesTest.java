package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class OrderTablesTest {

    @ParameterizedTest(name = "{0} -> {1}")
    @DisplayName("테이블 정보없이 생성할 경우 에러를 던진다.")
    @MethodSource("providerAddOrderTablesCase")
    void addOrderTables(List<OrderTable> inputOrderTables, Class<? extends Exception> exception) {
        OrderTables orderTables = new OrderTables();
        assertThatExceptionOfType(exception)
            .isThrownBy(() -> orderTables.addOrderTables(inputOrderTables));
    }

    private static Stream<Arguments> providerAddOrderTablesCase() {
        return Stream.of(
            Arguments.of(null, NullPointerException.class),
            Arguments.of(Collections.emptyList(), IllegalArgumentException.class),
            Arguments.of(Collections.singletonList(new OrderTable(0, true)), IllegalArgumentException.class),
            Arguments.of(Arrays.asList(new OrderTable(0, true), null), NullPointerException.class)
        );
    }

}
