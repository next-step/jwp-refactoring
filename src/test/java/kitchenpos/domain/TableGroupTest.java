package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class TableGroupTest {
    @DisplayName("그룹")
    @Test
    public void 그룹_확인() throws Exception {
        //given
        OrderTable orderTable1 = new OrderTable(1L, null, 5, false);
        OrderTable orderTable2 = new OrderTable(2L, null, 5, false);
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), null);

        //when
        tableGroup.group(Arrays.asList(orderTable1, orderTable2));

        //then
        assertThat(orderTable1.getTableGroup()).isEqualTo(tableGroup);
        assertThat(orderTable2.getTableGroup()).isEqualTo(tableGroup);
    }

    @DisplayName("그룹해제")
    @Test
    public void 그룹해제_확인() throws Exception {
        //given
        OrderTable orderTable1 = new OrderTable(1L, null, 5, false);
        OrderTable orderTable2 = new OrderTable(2L, null, 5, false);
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), null);
        tableGroup.group(Arrays.asList(orderTable1, orderTable2));

        //when
        tableGroup.ungroup();

        //then
        assertThat(orderTable1.getTableGroup()).isNull();
        assertThat(orderTable2.getTableGroup()).isNull();
    }
}
