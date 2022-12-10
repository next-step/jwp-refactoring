package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class TableGroupTest {
    @Test
    void 생성() {
        LocalDateTime NOW = LocalDateTime.now();
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setCreatedDate(NOW);
        tableGroup.setOrderTables(Arrays.asList(new OrderTable()));

        assertAll(
                () -> assertThat(tableGroup.getId()).isEqualTo(1L),
                () -> assertThat(tableGroup.getCreatedDate()).isEqualTo(NOW),
                () -> assertThat(tableGroup.getOrderTables().size()).isEqualTo(1)
        );
    }
}
