package kitchenpos.application.fixture;

import kitchenpos.domain.TableGroup;

public class TableGroupFixtureFactory {

    private TableGroupFixtureFactory() {
    }


    public static TableGroup create(final Long id) {
        return new TableGroup(id);
    }
}
