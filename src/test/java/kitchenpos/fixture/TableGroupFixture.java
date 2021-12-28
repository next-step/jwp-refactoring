package kitchenpos.fixture;

import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;

import static kitchenpos.fixture.OrderTablesFixture.orderTables;

public class TableGroupFixture {
    public static TableGroup tableGroup = TableGroup.of(1L, LocalDateTime.now(), orderTables);
}
