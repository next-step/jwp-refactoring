package kitchenpos.tablegroup.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단체 지정 테스트")
class TableGroupTest {

    @DisplayName("id가 같은 두 객체는 같다.")
    @Test
    void equalsTest() {
        TableGroup tableGroup1 = TableGroup.of(1L);
        TableGroup tableGroup2 = TableGroup.of(1L);

        Assertions.assertThat(tableGroup1).isEqualTo(tableGroup2);
    }

    @DisplayName("id가 다르면 두 객체는 다르다.")
    @Test
    void equalsTest2() {
        TableGroup tableGroup1 = TableGroup.of(1L);
        TableGroup tableGroup2 = TableGroup.of(2L);

        Assertions.assertThat(tableGroup1).isNotEqualTo(tableGroup2);
    }
}
