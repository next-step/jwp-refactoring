package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupTest {

    public static TableGroup 단체_생성(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        return new TableGroup.Builder()
                .id(id)
                .createdDate(createdDate)
                .orderTables(orderTables)
                .build();
    }
}