package kitchenpos.tablegroup.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TableGroupTest {

    @Test
    @DisplayName("테이블 그룹 생성에 성공한다")
    void createTableGroupTest() {
        // when
        TableGroup tableGroup = TableGroup.empty();

        // then
        assertThat(tableGroup).isNotNull();
    }
}
