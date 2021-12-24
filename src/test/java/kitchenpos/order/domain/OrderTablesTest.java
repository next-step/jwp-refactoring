package kitchenpos.order.domain;


import kitchenpos.tablegroup.domain.TableGroup;
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
    private OrderRepository orderRepository;
    private OrderTableValidator orderTableValidator;

    @BeforeEach
    void setUp() {
        orderRepository = Mockito.mock(OrderRepository.class);
        orderTableValidator = new OrderTableValidator(orderRepository);
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
        OrderTables result = OrderTables.of(new TableGroup(), orderTables, orderTables.size());

        // then
        assertThat(result.values()).isEqualTo(orderTables);
    }

    @DisplayName("기대한 숫자와 동일한 갯수의 리스트를 생성해야 한다")
    @Test
    void testGivenDifferentExpectedSize() {
        // given
        List<OrderTable> orderTables = Arrays.asList(new OrderTable(4, true), new OrderTable(4, true), new OrderTable(4, true));

        // when
        ThrowableAssert.ThrowingCallable callable = () -> OrderTables.of(new TableGroup(), orderTables, orderTables.size() + 1);

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
                        .map(OrderTable::getTableGroup)
                        .allMatch(Objects::isNull)
        );
    }

    @DisplayName("주문이 생성되지 않아야 단체 지정을 해제할 수 있다")
    @Test
    void testHasNotOrder() {
        // given
        TableGroup tableGroup = new TableGroup();
        OrderTable one = new OrderTable(1L, tableGroup, 4, false);
        OrderTable two = new OrderTable(2L, tableGroup, 4, false);
        List<OrderTable> orderTableList = Arrays.asList(one, two);
        OrderTables orderTables = new OrderTables(orderTableList);

        doThrow(new IllegalArgumentException()).when(orderTableValidator).validateHasProgressOrder(any(OrderTable.class));

        // when
        ThrowableAssert.ThrowingCallable callable = () -> orderTables.ungroup(orderTableValidator);

        // then
        assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
    }
}
