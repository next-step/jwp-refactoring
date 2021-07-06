package kitchenpos.tablegroup.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("테이블 그룹 도메인 테스트")
class TableGroupTest {

    @DisplayName("테이블 그룹 생성 테스트")
    @Test
    void create() {
        // given
        TableGroup tableGroup = TableGroup.of(new OrderTables());
        // when
        // then
        assertThat(tableGroup).isNotNull();
    }
}