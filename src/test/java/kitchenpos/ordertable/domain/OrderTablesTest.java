package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import kitchenpos.common.error.ErrorEnum;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.Test;

public class OrderTablesTest {
    @Test
    void 주문테이블은_비어있을_수_없습니다() {
        assertThatThrownBy(() -> new OrderTables(new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.NOT_EXISTS_ORDER_TABLE_LIST.message());
    }

    @Test
    void 주문_테이블이_2개_이상이어야_한다() {
        // given
        OrderTable orderTable = new OrderTable(new NumberOfGuests(4), false);

        // when & then
        assertThatThrownBy(() -> new OrderTables(Arrays.asList(orderTable)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.ORDER_TABLE_TWO_OVER.message());
    }

    @Test
    void 단체_지정할_수_있는지_확인할_때_빈_상태가_아니면_예외를_발생한다() {
        // given
        OrderTable firstOrderTable = new OrderTable(new NumberOfGuests(4), false);
        OrderTable secondOrderTable = new OrderTable(new NumberOfGuests(4), true);
        OrderTables orderTables = new OrderTables(Arrays.asList(firstOrderTable, secondOrderTable));

        // when & then
        assertThatThrownBy(() -> new TableGroup(LocalDateTime.now(), orderTables))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.EXISTS_NOT_EMPTY_ORDER_TABLE.message());
    }

    @Test
    void 단체_지정할_수_있는지_확인할_때_이미_단체지정_되어_있다면_예외가_발생한다() {
        // given
        OrderTable firstOrderTable = new OrderTable(new NumberOfGuests(4), true);
        OrderTable secondOrderTable = new OrderTable(new NumberOfGuests(4), true);
        OrderTables orderTables = new OrderTables(Arrays.asList(firstOrderTable, secondOrderTable));
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);
        firstOrderTable.setTableGroup(tableGroup);

        // when & then
        assertThatThrownBy(() -> new TableGroup(LocalDateTime.now(), orderTables))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.ALREADY_GROUP.message());
    }

    @Test
    void 주문_테이블_목록에_대해_단체_지정_해제를_할_수_있다() {
        // given
        OrderTable firstOrderTable = new OrderTable(new NumberOfGuests(4), true);
        OrderTable secondOrderTable = new OrderTable(new NumberOfGuests(4), true);
        OrderTables orderTables = new OrderTables(Arrays.asList(firstOrderTable, secondOrderTable));
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);
        firstOrderTable.setTableGroup(tableGroup);

        // when
        orderTables.ungroup();

        // then
        assertThat(firstOrderTable.getTableGroup()).isNull();
    }
}
