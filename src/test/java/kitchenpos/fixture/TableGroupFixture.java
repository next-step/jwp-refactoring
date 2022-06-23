package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupFixture {

    public static TableGroup 단체_지정_데이터_생성(List<OrderTable> orderTables) {
        return new TableGroup(null, null, orderTables);
    }

    public static TableGroup 단체_데이터_생성(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        return new TableGroup(id, createdDate, orderTables);
    }

}
