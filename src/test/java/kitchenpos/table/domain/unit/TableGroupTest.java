package kitchenpos.table.domain.unit;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;

import kitchenpos.table.domain.*;

@DisplayName("단체 지정 관련(단위테스트)")
class TableGroupTest {
    @DisplayName("단체 지정 생성하기")
    @Test
    void create() {
        assertThat(TableGroup.create()).isInstanceOf(TableGroup.class);
    }
}
