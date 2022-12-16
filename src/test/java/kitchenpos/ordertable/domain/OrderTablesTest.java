package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.Collections;
import kitchenpos.exception.ExceptionMessage;
import kitchenpos.ordertable.exception.CannotGroupOrderTablesException;
import kitchenpos.tablegroup.domain.TableGroup;
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
                .isInstanceOf(CannotGroupOrderTablesException.class)
                .hasMessageStartingWith(ExceptionMessage.INVALID_ORDER_TABLE_SIZE);
    }

    @DisplayName("단체 지정 시 주문 테이블이 2개 미만이면 예외가 발생한다.")
    @Test
    void registerTableGroupException2() {
        OrderTables orderTables = OrderTables.from(
                Collections.singletonList(OrderTable.of(10, true))
        );

        Assertions.assertThatThrownBy(() -> orderTables.registerTableGroup(TableGroup.createEmpty()))
                .isInstanceOf(CannotGroupOrderTablesException.class)
                .hasMessageStartingWith(ExceptionMessage.INVALID_ORDER_TABLE_SIZE);
    }

    @DisplayName("단체 지정 시 주문 테이블 하나라도 empty 상태가 아니면 예외가 발생한다.")
    @Test
    void registerTableGroupException3() {
        OrderTables orderTables = OrderTables.from(
                Arrays.asList(
                        OrderTable.of(10, false),
                        OrderTable.of(10, true))
        );

        Assertions.assertThatThrownBy(() -> orderTables.registerTableGroup(TableGroup.createEmpty()))
                .isInstanceOf(CannotGroupOrderTablesException.class)
                .hasMessageStartingWith(ExceptionMessage.NOT_EMPTY_ORDER_TABLE_EXIST);
    }

    @DisplayName("단체 지정 시 주문 테이블 하나라도 이미 단체지정이 되어있으면 예외가 발생한다.")
    @Test
    void registerTableGroupException4() {
        OrderTables orderTables = OrderTables.from(
                Arrays.asList(
                        OrderTable.of(1L, 1L, 10, true),
                        OrderTable.of(1L, 10, true))
        );

        Assertions.assertThatThrownBy(() -> orderTables.registerTableGroup(TableGroup.createEmpty()))
                .isInstanceOf(CannotGroupOrderTablesException.class)
                .hasMessageStartingWith(ExceptionMessage.ALREADY_GROUPED_ORDER_TABLE_EXIST);
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

}
