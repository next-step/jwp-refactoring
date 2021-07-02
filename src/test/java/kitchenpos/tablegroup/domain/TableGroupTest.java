package kitchenpos.tablegroup.domain;

import kitchenpos.ordertable.domain.OrderTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("단체 지정 테스트")
class TableGroupTest {
    @Test
    @DisplayName("2 개 이상의 빈 테이블을 단체로 지정할 수 있다.")
    public void create() throws Exception {
        // given
        OrderTable orderTable1 = new OrderTable(0, true);
        OrderTable orderTable2 = new OrderTable(0, true);

        // when
        TableGroup tableGroup = new TableGroup(new OrderTables(Arrays.asList(orderTable1, orderTable2)));

        // then
        Assertions.assertThat(tableGroup).isNotNull();
    }

    @Test
    @DisplayName("단체 지정은 중복될 수 없다.")
    public void createFail() throws Exception {
        // given
        OrderTable orderTable1 = new OrderTable(0, true);
        OrderTable orderTable2 = new OrderTable(0, true);
        new TableGroup(new OrderTables(Arrays.asList(orderTable1, orderTable2)));

        // when then
        Assertions.assertThatIllegalArgumentException()
                .isThrownBy(() -> new TableGroup(new OrderTables(Arrays.asList(orderTable1, orderTable2))))
                .withMessageMatching("단체 지정은 중복될 수 없습니다.");

    }

    @Test
    @DisplayName("단체 지정을 해지할 수 있다.")
    public void ungroup() throws Exception {
        // given
        OrderTable orderTable1 = new OrderTable(0, true);
        OrderTable orderTable2 = new OrderTable(0, true);
        TableGroup tableGroup = new TableGroup(new OrderTables(Arrays.asList(orderTable1, orderTable2)));

        // when
        tableGroup.ungroup();

        // then
        Assertions.assertThat(orderTable1.getTableGroup()).isNull();
        Assertions.assertThat(orderTable2.getTableGroup()).isNull();
    }
}