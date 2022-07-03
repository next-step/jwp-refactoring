package kitchenpos.__fixture__;

import java.time.LocalDateTime;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupTestFixture {
    public static TableGroup 테이블_그룹_생성(final Long id, final LocalDateTime createdDate,
                                       final OrderTable... orderTables) {
        return new TableGroup(id, createdDate, orderTables);
    }

    public static TableGroup 빈_테이블_그룹_생성(final Long id, final LocalDateTime createdDate) {
        return new TableGroup(id, createdDate);
    }
}
