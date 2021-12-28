package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class TableGroupTest {
    public static final TableGroup 테이블그룹 = new TableGroup(1L, LocalDateTime.now());

    @Test
    @DisplayName("테이블 그룹 생성")
    public void create() {
        // given
        // when
        TableGroup actual = new TableGroup(1L, LocalDateTime.now());
        // then
        assertThat(actual).isEqualTo(테이블그룹);
    }
}