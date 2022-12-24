package kitchenpos.domain;

import org.springframework.test.util.ReflectionTestUtils;

public class TableGroupFixture {

    public static TableGroup 테이블그룹(Long id) {
        TableGroup tableGroup = new TableGroup();
        ReflectionTestUtils.setField(tableGroup, "id", id);
        return tableGroup;
    }
}
