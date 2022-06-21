package kitchenpos.domain.tablegroup;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Collections;
import kitchenpos.application.fixture.OrderTableFixtureFactory;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.exception.CreateTableGroupException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    private OrderTable 주문테이블_1;
    private OrderTable 주문테이블_2;

    @BeforeEach
    void setUp() {
        주문테이블_1 = OrderTableFixtureFactory.createWithGuest(true, 2);
        주문테이블_2 = OrderTableFixtureFactory.createWithGuest(true, 4);
    }

    @DisplayName("TableGroup을 생성할 수 있다. (OrderTables)")
    @Test
    void create01() {
        // given & when & then
        assertThatNoException().isThrownBy(() -> TableGroup.from(Lists.newArrayList(주문테이블_1, 주문테이블_2)));
    }

    @DisplayName("TableGroup 생성 시 OrderTables가 존재하지 않으면 생성할 수 없다.")
    @Test
    void create02() {
        // given & when & then
        assertThrows(CreateTableGroupException.class, () -> TableGroup.from(Collections.emptyList()));
    }

    @DisplayName("TableGroup 생성 시 OrderTables 이 하나만 존재하면 생성할 수 없다.")
    @Test
    void create03() {
        // given & when & then
        assertThrows(CreateTableGroupException.class, () -> TableGroup.from(Lists.newArrayList(주문테이블_1)));
    }

    @DisplayName("TableGroup 생성 시 OrderTables 중 비어있지 않은 OrderTable이 존재하면 생성할 수 없다.")
    @Test
    void create04() {
        // given
        OrderTable orderTable = OrderTableFixtureFactory.createWithGuest(false, 1);

        // when & then
        assertThrows(CreateTableGroupException.class, () -> TableGroup.from(Lists.newArrayList(주문테이블_1, orderTable)));
    }

    @DisplayName("TableGroup 생성 시 OrderTables 중 이미 TableGroup에 속해있는 OrderTable이 존재하면 생성할 수 없다.")
    @Test
    void create05() {
        // given
        ArrayList<OrderTable> orderTables = Lists.newArrayList(주문테이블_1, 주문테이블_2);
        TableGroup tableGroup = TableGroup.from(orderTables);
        tableGroup.assignedOrderTables(orderTables);

        // when & then
        assertThrows(CreateTableGroupException.class, () -> TableGroup.from(orderTables));
    }
}