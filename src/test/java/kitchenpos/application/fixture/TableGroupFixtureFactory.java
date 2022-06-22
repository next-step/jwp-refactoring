package kitchenpos.application.fixture;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupFixtureFactory {

    private TableGroupFixtureFactory() {
    }


    public static TableGroup create(final Long id) {
        return new TableGroup(id);
    }
    public static TableGroup create(final Long id, final List<OrderTable> orderTables) {
        return new TableGroup(id, orderTables);
    }
}
