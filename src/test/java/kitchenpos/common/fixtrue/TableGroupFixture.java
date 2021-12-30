package kitchenpos.common.fixtrue;

import kitchenpos.table.domain.TableGroup;

public class TableGroupFixture {

    private TableGroupFixture() {
    }

    public static TableGroup from() {
        return TableGroup.from();
    }
}
