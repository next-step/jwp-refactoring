package kitchenpos.tablegroup.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.stream.Collectors;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tablegroup.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.Test;

class OrderTablesTest {
    @Test
    void 테이블_그룹과_주문_테이블들과의_연관관계를_설정할_수_있어야_한다() {
        // given
        final TableGroup tableGroup = new TableGroup();
        final OrderTable orderTable1 = new OrderTable(1L, null, new NumberOfGuests(0), true);
        final OrderTable orderTable2 = new OrderTable(2L, null, new NumberOfGuests(0), true);

        // when
        final OrderTables orderTables = new OrderTables();
        orderTables.makeRelations(tableGroup, Arrays.asList(orderTable1, orderTable2));

        // then
        assertThat(
                orderTables.getResponses().stream().map(OrderTableResponse::getId).collect(Collectors.toList()))
                .containsExactly(orderTable1.getId(), orderTable2.getId());
    }

    @Test
    void 테이블_개수가_2개_미만이면_에러가_발생해야_한다() {
        // given
        final TableGroup tableGroup = new TableGroup();
        final OrderTable orderTable = new OrderTable(1L, null, new NumberOfGuests(0), true);
        final OrderTables orderTables = new OrderTables();

        // when and then
        assertThatThrownBy(() -> orderTables.makeRelations(tableGroup, Arrays.asList(orderTable)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블이_하나라도_비어있지_않으면_에러가_발생해야_한다() {
        // given
        final TableGroup tableGroup = new TableGroup();
        final OrderTable empty = new OrderTable(1L, null, new NumberOfGuests(0), true);
        final OrderTable notEmpty = new OrderTable(2L, null, new NumberOfGuests(1), false);
        final OrderTables orderTables = new OrderTables();

        // when and then
        assertThatThrownBy(() -> orderTables.makeRelations(tableGroup, Arrays.asList(empty, notEmpty)))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 이미_그룹이_지정된_테이블이_있으면_에러가_발생해야_한다() {
        // given
        final TableGroup tableGroup = new TableGroup();
        final OrderTable empty = new OrderTable(2L, null, new NumberOfGuests(0), true);
        final OrderTable alreadyGrouped = new OrderTable(1L, 1L, new NumberOfGuests(0), true);
        final OrderTables orderTables = new OrderTables();

        // when and then
        assertThatThrownBy(() -> orderTables.makeRelations(tableGroup, Arrays.asList(empty, alreadyGrouped)))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 테이블_그룹과_주문_테이블들과의_연관관계를_제거할_수_있어야_한다() {
        // given
        final TableGroup tableGroup = new TableGroup(1L);
        final OrderTable orderTable1 = new OrderTable(1L, null, new NumberOfGuests(0), true);
        final OrderTable orderTable2 = new OrderTable(2L, null, new NumberOfGuests(0), true);
        final OrderTables orderTables = new OrderTables();
        orderTables.makeRelations(tableGroup, Arrays.asList(orderTable1, orderTable2));

        // when
        orderTables.removeRelations();

        // then
        assertThat(orderTables.getResponses().stream().map(OrderTableResponse::getTableGroupId))
                .containsExactly(null, null);
    }
}
