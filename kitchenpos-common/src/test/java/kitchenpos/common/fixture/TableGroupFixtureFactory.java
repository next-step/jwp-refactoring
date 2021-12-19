package kitchenpos.common.fixture;

import java.util.List;

import kitchenpos.common.domain.tablegroup.TableGroup;

public class TableGroupFixtureFactory {

    private TableGroupFixtureFactory() {}

    public static TableGroup create(long id) {
        return TableGroup.from(id);
    }
}
