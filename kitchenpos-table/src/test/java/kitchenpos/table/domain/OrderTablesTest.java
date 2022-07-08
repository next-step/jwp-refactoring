package kitchenpos.table.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.testfixture.TableTestFixture.createOrderTable;
import static kitchenpos.testfixture.TableTestFixture.createTableGroup;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTablesTest {
    private List<OrderTable> orderTables;
    private TableGroup tableGroup;
    private OrderTable 단체지정_테이블;
    private OrderTable 빈_테이블_1, 빈_테이블_2;

    @BeforeEach
    void setUp() {
        // given
        빈_테이블_1 = createOrderTable(1L, null, 0, true);
        빈_테이블_2 = createOrderTable(2L, null, 0, true);
        orderTables = Arrays.asList(빈_테이블_1, 빈_테이블_2);
        tableGroup = createTableGroup(orderTables);
        단체지정_테이블 = createOrderTable(1L, tableGroup, 4, false);
    }

    @Test
    @DisplayName("OrderTables 생성 시 빈 테이블이어야 한다.")
    void validateTablesEmpty_notEmpty() {
        OrderTable 주문_테이블 = createOrderTable(3L, null, 0, false);
        orderTables = Arrays.asList(빈_테이블_1, 주문_테이블);

        // then
        assertThatThrownBy(() -> {
            new OrderTables(orderTables, null);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("OrderTables 생성 시 단체 지정되지 않은 테이블이어야 한다.")
    void validateTablesEmpty_alreadyGrouped() {
        orderTables = Arrays.asList(빈_테이블_1, 단체지정_테이블);

        // then
        assertThatThrownBy(() -> {
            new OrderTables(orderTables, null);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("OrderTables 생성 시 주문테이블은 2개 이상이어야 한다.")
    void validateOrderTableSize() {
        orderTables = Arrays.asList(
                createOrderTable(1L, null, 0, true),
                단체지정_테이블
        );

        // then
        assertThatThrownBy(() -> {
            new OrderTables(orderTables, null);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
