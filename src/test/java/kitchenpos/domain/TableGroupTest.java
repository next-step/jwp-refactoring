package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class TableGroupTest {
    @Test
    void 생성() {
        TableGroup tableGroup = new TableGroup(Arrays.asList(new OrderTable()));

        assertAll(
                () -> assertThat(tableGroup.getOrderTables().size()).isEqualTo(1)
        );
    }
}
