package kitchenpos.table.domain.tablegroup;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("테이블 그룹 관리")
class TableGroupTest {

    @Test
    @DisplayName("테이블 그룹 생성")
    public void create() {
        // when
        final TableGroup tableGroup = new TableGroup();
        // then
        assertThat(tableGroup).isNotNull();
    }

}
