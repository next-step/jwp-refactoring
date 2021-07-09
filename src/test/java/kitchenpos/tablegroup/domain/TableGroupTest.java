package kitchenpos.tablegroup.domain;

import static java.util.Arrays.*;
import static kitchenpos.TextFixture.*;
import static kitchenpos.table.domain.OrderTableTest.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;

class TableGroupTest {

	@DisplayName("2개 이상의 주문테이블만 그룹화 할 수 있다.")
	@Test
	void createTableGroupWithLessTwoOrderTables() {
		// given
		OrderTable orderTable = new OrderTable(1, true);
		TableGroup tableGroup = new TableGroup(LocalDateTime.now());
		// when
		// than
		assertThatThrownBy(() -> tableGroup.group(orderTable))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("2개 미만의 주문테이블은 그룹화 할 수 없습니다.");
	}

	@DisplayName("그룹화할 주문테이블들은 모두 빈 테이블이어야 한다.")
	@Test
	void createTableGroupWithNotEmptyOrderTableTest() {
		// given
		OrderTable notEmptyTable = new OrderTable(1, false);
		OrderTable orderTable = new OrderTable(1, true);
		TableGroup tableGroup = new TableGroup(LocalDateTime.now());
		// when
		// than
		assertThatThrownBy(() -> tableGroup.group(orderTable, notEmptyTable))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("비어있지 않은 테이블은 그룹화 할 수 없습니다.");
	}

	@DisplayName("그룹화할 주문테이블들은 모두 그룹화되지 않은 테이블이어야 한다.")
	@Test
	void createTableGroupWithGroupedOrderTableTest() {
		// given
		OrderTable groupedTable = createOrderTable(1L, 1L, NumberOfGuests.valueOf(1), false);
		OrderTable ungroupedTable = createOrderTable(2L, null, NumberOfGuests.valueOf(1), false);

		TableGroup tableGroup = new TableGroup(LocalDateTime.now());

		// when
		// than
		assertThatThrownBy(() -> tableGroup.group(groupedTable, ungroupedTable))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("이미 그룹화되어 있는 테이블은 그룹화 할 수 없습니다.");
	}

	@DisplayName("그룹화된 주문테이블들은 모두 비어있지 않는 상태로 변한다.")
	@Test
	void createTableGroupTest() {
		// given
		OrderTable groupedTable1 = new OrderTable(1, true);
		OrderTable groupedTable2 = new OrderTable(1, true);
		TableGroup tableGroup = new TableGroup(LocalDateTime.now());

		// when
		tableGroup.group(groupedTable1, groupedTable2);

		// than
		assertThat(groupedTable1.isEmpty()).isFalse();
		assertThat(groupedTable2.isEmpty()).isFalse();
	}

	@DisplayName("테이블그룹은 생성시각으로 생성된다.")
	@Test
	void createTest() {
		// given
		LocalDateTime createdDate = LocalDateTime.now();

		// when
		TableGroup tableGroup = new TableGroup(createdDate);

		// than
		assertThat(tableGroup.getCreatedDate()).isEqualTo(createdDate);
	}

	@DisplayName("테이블그룹은 그룹에 소속된 테이블들을 그룹해제 시킬 수 있다.")
	@Test
	void ungroupTest() {
		// given
		OrderTable table1 = createOrderTable(1L, 1L, NumberOfGuests.valueOf(1), false);
		Order order1 = table1.createOrder(주문항목들_후라이드_1개_양념_1개, LocalDateTime.now());
		order1.complete();

		OrderTable table2 =  createOrderTable(2L, 1L, NumberOfGuests.valueOf(1), false);
		Order order2 = table2.createOrder(주문항목들_후라이드_1개_양념_1개, LocalDateTime.now());
		order2.complete();

		TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now());

		// when
		tableGroup.ungroup(asList(table1, table2), new UngroupValidator(asList(order1, order2)));

		// than
		assertThat(table1.isGrouped()).isFalse();
		assertThat(table2.isGrouped()).isFalse();
	}


	@DisplayName("그룹화된 주문테이블들의 주문이 조리상태이거나 식사상태이면 그룹해제를 할 수 없다.")
	@Test
	void ungroupWithNotCompleteOrderTest() {
		// given
		OrderTable notCompletedOrderTable = createOrderTable(1L, 1L, NumberOfGuests.valueOf(1), false);
		Order notCompletedOrder = notCompletedOrderTable.createOrder(주문항목들_후라이드_1개_양념_1개, LocalDateTime.now());

		OrderTable orderTable = createOrderTable(2L, 1L, NumberOfGuests.valueOf(1), false);
		Order order = orderTable.createOrder(주문항목들_후라이드_1개_양념_1개, LocalDateTime.now());
		order.complete();

		TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now());

		// than
		assertThatThrownBy(() -> tableGroup.ungroup(asList(notCompletedOrderTable, orderTable), new UngroupValidator(asList(notCompletedOrder, order))))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("조리상태이거나 식사상태인 주문이 있는 주문테이블은 그룹해제를 할 수 없습니다.");
	}
}