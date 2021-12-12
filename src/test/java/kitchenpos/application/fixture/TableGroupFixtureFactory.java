package kitchenpos.application.fixture;

import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupFixtureFactory {

    private TableGroupFixtureFactory() {}

    public static TableGroup create(long id) {
        return TableGroup.from(id);
    }
}
