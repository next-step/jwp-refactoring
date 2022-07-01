package kitchenpos.tablegroup.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {
    @DisplayName("초기화 테스트")
    @Test
    void from() {
        TableGroup tableGroup = TableGroup.from(1L);
        assertThat(tableGroup).isEqualTo(tableGroup);
    }
}
