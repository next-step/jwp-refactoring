package kitchenpos.table.__fixture__;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public class TableGroupTestFixture {
    public static TableGroup 테이블_그룹_생성(final Long id, final LocalDateTime createdDate,
                                       final List<OrderTable> orderTables) {
        return new TableGroup(id, createdDate, orderTables);
    }

    public static TableGroup 빈_테이블_그룹_생성(final Long id, final LocalDateTime createdDate) {
        return new TableGroup(id, createdDate, Collections.emptyList());
    }
}
