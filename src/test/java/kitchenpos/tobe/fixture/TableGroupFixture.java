package kitchenpos.tobe.fixture;

import java.util.List;
import kitchenpos.tobe.orders.ordertable.domain.TableGroup;
import kitchenpos.tobe.orders.ordertable.dto.TableGroupRequest;

public class TableGroupFixture {

    private TableGroupFixture() {
    }

    public static TableGroup of(final Long id) {
        return new TableGroup(id);
    }

    public static TableGroup of() {
        return of(null);
    }

    public static TableGroupRequest ofRequest(final List<Long> orderTableIds) {
        return new TableGroupRequest(orderTableIds);
    }
}
