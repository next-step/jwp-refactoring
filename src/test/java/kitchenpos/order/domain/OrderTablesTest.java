package kitchenpos.order.domain;

import static kitchenpos.common.exception.ExceptionMessage.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ExceptionMessage;

class OrderTablesTest {
    private List<OrderTable> orderTables;

    @BeforeEach
    void setUp() {
        orderTables = Arrays.asList(
            OrderTable.of(0, true),
            OrderTable.of(1, true));
    }

    @DisplayName("단체 지정은 주문테이블이 2개 이상일 경우 가능하다")
    @Test
    void validateOrderTables() {
        // when && then
        assertDoesNotThrow(() -> new OrderTables(orderTables));
    }

    @DisplayName("단체 지정은 주문테이블이 2개 미만일 경우 불가능하다")
    @Test
    void validateOrderTablesLessThanTwo() {
        // given
        List<OrderTable> emptyOrderTables = Collections.emptyList();
        List<OrderTable> singleOrderTables = Collections.singletonList(
            OrderTable.of(0, true));

        // when && then
        assertAll(
            () -> assertThatThrownBy(() -> new OrderTables(emptyOrderTables))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessage.WRONG_VALUE.getMessage()),
            () -> assertThatThrownBy(() -> new OrderTables(singleOrderTables))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessage.WRONG_VALUE.getMessage())
        );
    }

    @DisplayName("빈테이블이 아니거나 이미 단체로 지정되어있는 테이블은 단체 지정을 할 수 없다")
    @Test
    void validateOrderTableEmptyAndNonExistGroupTable() {
        List<OrderTable> existTableGroup = Arrays.asList(
            OrderTable.of(1L, TableGroup.of(this.orderTables), 0, true),
            OrderTable.of(0, true));

        List<OrderTable> emptyOrderTables = Arrays.asList(
            OrderTable.of(0, false),
            OrderTable.of(0, true));

        assertAll(
            () -> assertThatThrownBy(() -> new OrderTables(existTableGroup))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(WRONG_VALUE.getMessage()),

            () -> assertThatThrownBy(() -> new OrderTables(emptyOrderTables))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(WRONG_VALUE.getMessage())
        );
    }
}
