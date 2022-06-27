package kitchenpos.order.domain;

import static kitchenpos.utils.DomainFixtureFactory.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.orderTable.domain.OrderTable;
import kitchenpos.orderTable.domain.OrderTables;
import kitchenpos.orderTable.domain.TableGroup;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {
    private OrderTable 치킨주문테이블;
    private OrderTable 피자주문테이블;

    @BeforeEach
    void setUp() {
        치킨주문테이블 = createOrderTable(1L, 2, true);
        피자주문테이블 = createOrderTable(2L, 2, true);
    }

    @DisplayName("초기화 테스트")
    @Test
    void from() {
        TableGroup tableGroup = TableGroup.from(OrderTables.from(Lists.newArrayList(치킨주문테이블, 피자주문테이블))
                , Lists.newArrayList(치킨주문테이블.id(), 피자주문테이블.id()));
        assertThat(tableGroup).isEqualTo(tableGroup);
    }
}
