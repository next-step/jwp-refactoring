package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.Test;

class TableGroupTest {
    @Test
    void 생성() {
        TableGroup tableGroup = new TableGroup(Arrays.asList(new OrderTable(null, 1, false)));

        assertAll(
                () -> assertThat(tableGroup.getOrderTables().size()).isEqualTo(1)
        );
    }
}
