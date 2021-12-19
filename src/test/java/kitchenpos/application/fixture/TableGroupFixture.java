package kitchenpos.application.fixture;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupFixture {

    private TableGroupFixture() {
    }

    public static TableGroup 단체지정생성(Long id, List<OrderTable> orderTables) {
        TableGroup 단체지정 = new TableGroup();
        단체지정.setId(id);
        단체지정.setOrderTables(orderTables);

        return 단체지정;
    }

    public static TableGroup 단체지정생성(Long id) {
        TableGroup 단체지정 = new TableGroup();
        단체지정.setId(id);

        return 단체지정;
    }
}
