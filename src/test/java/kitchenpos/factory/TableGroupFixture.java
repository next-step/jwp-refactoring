package kitchenpos.factory;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;

public class TableGroupFixture {
    public static OrderTable 테이블그룹_생성(Long id) {
        return new OrderTable(id, null, 0, true);
    }
}
