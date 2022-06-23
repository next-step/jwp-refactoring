package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupTest {

    public static TableGroup 테이블_그룹_생성(List<OrderTable> orderTables) {
        TableGroup result = new TableGroup();

        result.setOrderTables(orderTables);
        result.setCreatedDate(LocalDateTime.now());

        return result;
    }
}
