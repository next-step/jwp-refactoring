package kitchenpos.tobe.fixture;

import kitchenpos.tobe.orders.domain.ordertable.TableGroup;

public class TableGroupFixture {

    private TableGroupFixture() {
    }

    public static TableGroup of(final Long id) {
        return new TableGroup(id);
    }

    public static TableGroup of() {
        return of(null);
    }
}
