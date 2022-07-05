package kitchenpos.tablegroup.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class TableGroupTest {

    @Test
    @DisplayName("테이블 그룹 생성")
    void tableGroup() {
        // given
        LocalDateTime localDateTime = LocalDateTime.now();

        // when
        TableGroup tableGroup = TableGroup.of(localDateTime);

        // then
        Assertions.assertThat(tableGroup.getCreatedDate()).isEqualTo(localDateTime);
    }
}
