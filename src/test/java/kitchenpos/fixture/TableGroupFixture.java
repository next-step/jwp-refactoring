package kitchenpos.fixture;

import kitchenpos.domain.table.OrderTables;
import kitchenpos.domain.table.TableGroup;

import java.time.LocalDateTime;

public class TableGroupFixture {
    public static TableGroup 주문이_없는_테이블_그룹;

    public static void cleanUp() {
        주문이_없는_테이블_그룹 = new TableGroup(1L, LocalDateTime.now(), new OrderTables());
    }
}
