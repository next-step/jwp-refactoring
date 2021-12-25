package kitchenpos.table.domain;


import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

public class OrderTablesTest {
    private OrderTableValidatable orderTableValidator;

    @BeforeEach
    void setUp() {
        orderTableValidator = Mockito.mock(OrderTableValidatable.class);
    }

    @DisplayName("OrderTables를 생성한다")
    @Test
    void testCreate() {
        // given
        List<OrderTable> orderTables = Arrays.asList(new OrderTable(), new OrderTable(), new OrderTable());

        // when
        OrderTables result = new OrderTables(orderTables);

        // then
        assertThat(result.values()).isEqualTo(orderTables);
    }

    @DisplayName("기대한 숫자와 동일한 갯수의 리스트를 생성한다")
    @Test
    void testExpectedSize() {
        // given
        List<OrderTable> orderTables = Arrays.asList(new OrderTable(4, true), new OrderTable(4, true), new OrderTable(4, true));

        // when
        OrderTables result = OrderTables.of(orderTables, orderTables.size());

        // then
        assertThat(result.values()).isEqualTo(orderTables);
    }

    @DisplayName("기대한 숫자와 동일한 갯수의 리스트를 생성해야 한다")
    @Test
    void testGivenDifferentExpectedSize() {
        // given
        List<OrderTable> orderTables = Arrays.asList(new OrderTable(4, true), new OrderTable(4, true), new OrderTable(4, true));

        // when
        ThrowableAssert.ThrowingCallable callable = () -> OrderTables.of(orderTables, orderTables.size() + 1);

        // then
        assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정한다")
    @Test
    void testGroup() {
        // given
        List<OrderTable> orderTableList = Arrays.asList(new OrderTable(4, true), new OrderTable(4, true), new OrderTable(4, true));
        OrderTables orderTables = OrderTables.of(orderTableList, orderTableList.size());
        Long tableGroupId = 1L;

        // when
        orderTables.group(tableGroupId);

        // then
        assertThat(orderTableList).allMatch(orderTable -> tableGroupId.equals(orderTable.getTableGroupId()));
    }

    @DisplayName("비어있는 테이블만 단체지정을 할 수 있다")
    @Test
    void givenNotEmptyTableWhenGroupThenThrowException() {
        // given
        List<OrderTable> orderTableList = Arrays.asList(new OrderTable(4, false), new OrderTable(4, false), new OrderTable(4, true));
        OrderTables orderTables = OrderTables.of(orderTableList, orderTableList.size());
        Long tableGroupId = 1L;

        // when
        ThrowableAssert.ThrowingCallable callable = () ->orderTables.group(tableGroupId);

        // then
        assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해제한다")
    @Test
    void testUngroup() {
        // given
        List<OrderTable> orderTableList = Arrays.asList(new OrderTable(), new OrderTable(), new OrderTable());
        OrderTables orderTables = new OrderTables(orderTableList);

        // when
        orderTables.ungroup(orderTableValidator);

        // then
        assertAll(
                () -> assertThat(orderTables.isEmpty()).isTrue(),
                () -> assertThat(orderTableList)
                        .map(OrderTable::getTableGroupId)
                        .allMatch(Objects::isNull)
        );
    }

    @DisplayName("주문이 생성되지 않아야 단체 지정을 해제할 수 있다")
    @Test
    void testHasNotOrder() {
        // given
        OrderTable one = new OrderTable(1L, 1L, 4, false);
        OrderTable two = new OrderTable(2L, 1L, 4, false);
        List<OrderTable> orderTableList = Arrays.asList(one, two);
        OrderTables orderTables = new OrderTables(orderTableList);

        doThrow(IllegalArgumentException.class).when(orderTableValidator).validateHasProgressOrder(any(OrderTable.class));

        // when
        ThrowableAssert.ThrowingCallable callable = () -> orderTables.ungroup(orderTableValidator);

        // then
        assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
    }
}
