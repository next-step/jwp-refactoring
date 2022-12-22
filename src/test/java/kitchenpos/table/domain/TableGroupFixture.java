package kitchenpos.table.domain;

import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

public class TableGroupFixture {

    public static TableGroup 테이블그룹(Long id) {
        TableGroup tableGroup = new TableGroup();
        ReflectionTestUtils.setField(tableGroup, "id", id);
        return tableGroup;
    }

    public static TableGroup 테이블그룹(Long id, List<OrderTable> orderTableList) {
        TableGroup tableGroup = new TableGroup(orderTableList);
        ReflectionTestUtils.setField(tableGroup, "id", id);
        return tableGroup;
    }
}
