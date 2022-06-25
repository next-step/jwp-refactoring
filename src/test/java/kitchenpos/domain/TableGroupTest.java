package kitchenpos.domain;

import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.OrderTable;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupTest {

    public static TableGroup 테이블_그룹_생성(List<OrderTable> orderTables) {
        return new TableGroup(LocalDateTime.now(), orderTables);
    }
}
