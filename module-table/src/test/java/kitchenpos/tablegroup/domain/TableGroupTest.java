package kitchenpos.tablegroup.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static fixture.TableGroupFixtureFactory.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("테이블그룹 도메인 테스트")
class TableGroupTest {
    @DisplayName("테이블 그룹을 생성한다")
    @Test
    void TableGroup_생성() {
        TableGroup tableGroup = createTableGroup(1L);

        assertThat(tableGroup.getId()).isEqualTo(1L);
    }
}