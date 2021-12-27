package kitchenpos.tablegroup.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @DisplayName("테이블 그룹 생성")
    @Test
    void groupTables() {
        //when
        TableGroup tableGroup = new TableGroup(1L);

        //then
        assertThat(tableGroup).isEqualTo(new TableGroup(1L));
    }
}
