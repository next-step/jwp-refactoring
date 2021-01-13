package kitchenpos.tablegroup.domain;

import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("단체 지정")
class TableGroupTest {

    @DisplayName("단체 지정을 생성한다.")
    @Test
    void create() {
        // given
        OrderTable orderTable1 = new OrderTable(5, true);
        OrderTable orderTable2 = new OrderTable(3, true);

        // when
        TableGroup tableGroup = new TableGroup(Arrays.asList(orderTable1, orderTable2));

        // then
        assertThat(tableGroup).isNotNull();
        assertThat(orderTable1.getTableGroup()).isNotNull();
        assertThat(orderTable2.getTableGroup()).isNotNull();
    }

    @DisplayName("주문 테이블이 2개 이상이어야 한다.")
    @Test
    void requiredOrderTable() {
        // given
        OrderTable orderTable = new OrderTable(5, false);

        // when / then
        assertThrows(IllegalArgumentException.class, () -> new TableGroup(Arrays.asList(orderTable)));
    }

    @DisplayName("주문 테이블이 등록 가능 상태면 지정할 수 없다.")
    @Test
    void isNotEmpty() {
        // given
        OrderTable orderTable1 = new OrderTable(5, true);
        OrderTable orderTable2 = new OrderTable(3, false);

        // when / then
        assertThrows(IllegalArgumentException.class, () -> new TableGroup(Arrays.asList(orderTable1, orderTable2)));

    }

    @DisplayName("이미 단체 지정이 되어 있으면 지정할 수 없다.")
    @Test
    void already() {
        // given
        OrderTable orderTable1 = new OrderTable(5, false);
        OrderTable orderTable2 = new OrderTable(3, false);
        orderTable1.assign(new TableGroup());

        // when / then
        assertThrows(IllegalArgumentException.class, () -> new TableGroup(Arrays.asList(orderTable1, orderTable2)));
    }

    @DisplayName("단체 지정을 삭제할 수 있다.")
    @Test
    void ungroup() {
        // given
        OrderTable orderTable1 = new OrderTable(5, true);
        OrderTable orderTable2 = new OrderTable(3, true);
        TableGroup tableGroup = new TableGroup(Arrays.asList(orderTable1, orderTable2));

        // when
        tableGroup.ungroup();

        // then
        assertThat(orderTable1.getTableGroup()).isNull();
        assertThat(orderTable2.getTableGroup()).isNull();
    }

    @DisplayName("해당 주문 테이블이 조리 중이거나 식사 중일때는 단체 지정을 삭제할 수 없다.")
    @Test
    void cantUngroup() {
        // given
        OrderTable orderTable1 = new OrderTable(5, false);
        OrderTable orderTable2 = new OrderTable(3, false);
        TableGroup tableGroup = new TableGroup(Arrays.asList(orderTable1, orderTable2));

        // when / then
        assertThrows(IllegalStateException.class, () -> tableGroup.ungroup());
    }

}
