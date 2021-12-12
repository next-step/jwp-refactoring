package kitchenpos.application.fixture;

import kitchenpos.domain.TableGroup;

public class TableGroupFixtureFactory {

    private TableGroupFixtureFactory() {}

    public static TableGroup create(long id) {
        return TableGroup.from(id);
    }
}
