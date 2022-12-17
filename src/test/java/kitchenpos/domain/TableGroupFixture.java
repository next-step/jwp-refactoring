package kitchenpos.domain;

import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupFixture {
    public static TableGroup 테이블그룹(Long id, List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup(orderTables);
        ReflectionTestUtils.setField(tableGroup, "id", id);
        return tableGroup;
    }
}
