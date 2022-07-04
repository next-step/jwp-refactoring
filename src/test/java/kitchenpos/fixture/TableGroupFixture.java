package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupFixture {

    public static TableGroup 테이블_그룹_데이터_생성(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        return new TableGroup(id, createdDate, orderTables);
    }

}
