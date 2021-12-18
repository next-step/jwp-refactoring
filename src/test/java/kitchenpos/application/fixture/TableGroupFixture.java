package kitchenpos.application.fixture;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupFixture {

    private TableGroupFixture() {
    }

    public static TableGroup 단체지정생성(Long id, List<OrderTable> 주문테이블목록) {
        TableGroup 단체지정 = new TableGroup();
        단체지정.setId(1L);
        단체지정.setOrderTables(주문테이블목록);

        return 단체지정;
    }

}
