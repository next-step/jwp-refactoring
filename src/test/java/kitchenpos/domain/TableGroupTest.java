package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class TableGroupTest {
    @Test
    void 단체_지정_생성_시_테이블에_단체_연결() {
        // when
        OrderTable orderTable1 = new OrderTable(NumberOfGuests.from(5), true);
        OrderTable orderTable2 = new OrderTable(NumberOfGuests.from(10), true);

        TableGroup tableGroup = new TableGroup(1L, null, Arrays.asList(orderTable1, orderTable2));

        // then
        assertAll(
                () -> assertThat(orderTable1.getTableGroupId()).isEqualTo(tableGroup.getId()),
                () -> assertThat(orderTable2.getTableGroupId()).isEqualTo(tableGroup.getId())
        );
    }
}
