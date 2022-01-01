package kitchenpos.fixture;

import java.util.List;
import kitchenpos.ordertable.domain.TableGroup;
import kitchenpos.ordertable.dto.TableGroupRequest;

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
