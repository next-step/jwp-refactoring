package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @Test
    @DisplayName("테이블그룹 객체가 같은지 검증")
    void verifyEqualsTableGroup() {
        final TableGroup tableGroup = new TableGroup(1L, null, Collections.emptyList());

        assertThat(tableGroup).isEqualTo(new TableGroup(1L, null, Collections.emptyList()));
    }
}
