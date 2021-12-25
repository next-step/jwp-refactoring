package kitchenpos.table.fixture;

import kitchenpos.table.domain.TableGroup;

public class TableGroupFixture {
    private TableGroupFixture() {
        throw new UnsupportedOperationException();
    }

    public static TableGroup create(Long id) {
        return TableGroup.of(id);
    }

}
