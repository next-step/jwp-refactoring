package kitchenpos.tobe.fixture;

import java.util.List;
import kitchenpos.tobe.orders.domain.ordertable.TableGroup;
import kitchenpos.tobe.orders.dto.ordertable.TableGroupRequest;

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
