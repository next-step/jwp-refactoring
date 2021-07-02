package kitchenpos.domain;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

	@DisplayName("2개 이상의 주문테이블만 그룹화 할 수 있다.")
	@Test
	void createTableGroupWithLessTwoOrderTables() {
		OrderTable orderTable = new OrderTable(1, true);
		OrderTables orderTables = OrderTables.of(orderTable);

		assertThatThrownBy(() -> new TableGroup(orderTables, LocalDateTime.now()))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("2개 미만의 주문테이블은 그룹화 할 수 없습니다.");
	}

	@DisplayName("그룹화할 주문테이블들은 모두 빈 테이블이어야 한다.")
	@Test
	void createTableGroupWithNotEmptyOrderTableTest() {
		OrderTable notEmptyTable = new OrderTable(1, false);
		OrderTable orderTable = new OrderTable(1, true);
		OrderTables orderTables = OrderTables.of(orderTable, notEmptyTable);

		assertThatThrownBy(() -> new TableGroup(orderTables, LocalDateTime.now()))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("비어있지 않거나, 이미 그룹화되어 있는 테이블은 그룹화 할 수 없습니다.");
	}

	@DisplayName("그룹화할 주문테이블들은 모두 그룹화되지 않은 테이블이어야 한다.")
	@Test
	void createTableGroupWithGroupedOrderTableTest() {
		OrderTable groupedTable1 = new OrderTable(1, true);
		OrderTable groupedTable2 = new OrderTable(1, true);
		OrderTables orderTables = OrderTables.of(groupedTable1, groupedTable2);
		TableGroup tableGroup = new TableGroup(orderTables, LocalDateTime.now());
		OrderTable orderTable = new OrderTable(1, true);
		OrderTables orderTables1 = OrderTables.of(groupedTable1, orderTable);

		assertThatThrownBy(() -> new TableGroup(orderTables1, LocalDateTime.now()))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("비어있지 않거나, 이미 그룹화되어 있는 테이블은 그룹화 할 수 없습니다.");
	}

	@DisplayName("테이블그룹은 주문테이블들과 생성시각으로 생성된다.")
	@Test
	void createTest() {
		OrderTable orderTable1 = new OrderTable(1, true);
		OrderTable orderTable2 = new OrderTable(1, true);
		OrderTables orderTables = OrderTables.of(orderTable1, orderTable2);
		LocalDateTime createdDate = LocalDateTime.now();

		TableGroup tableGroup = new TableGroup(orderTables, createdDate);

		assertThat(orderTable1.getTableGroup()).isEqualTo(tableGroup);
		assertThat(orderTable2.getTableGroup()).isEqualTo(tableGroup);
		assertThat(tableGroup.getOrderTables()).containsExactly(orderTable1, orderTable2);
		assertThat(tableGroup.getCreatedDate()).isEqualTo(createdDate);
	}

	@DisplayName("테이블그룹을 통해 주문테이블의 식별자들을 알 수 있다.")
	@Test
	void getOrderTableIdsTest() {
		OrderTable orderTable1 = mock(OrderTable.class);
		when(orderTable1.getId()).thenReturn(1L);
		OrderTable orderTable2 = mock(OrderTable.class);
		when(orderTable2.getId()).thenReturn(2L);
		TableGroup tableGroup = new TableGroup(OrderTables.of(orderTable1, orderTable2), LocalDateTime.now());

		List<Long> orderTableIds = tableGroup.getOrderTableIds();

		assertThat(orderTableIds).containsExactly(1L, 2L);
	}

	@DisplayName("테이블그룹은 그룹에 소속된 테이블들을 그룹해제 시킬 수 있다.")
	@Test
	void ungroupTest() {
		OrderTable orderTable1 = new OrderTable(1, true);
		OrderTable orderTable2 =  new OrderTable(1, true);
		TableGroup tableGroup = new TableGroup(OrderTables.of(orderTable1, orderTable2), LocalDateTime.now());

		tableGroup.ungroup();

		assertThat(orderTable1.isGrouped()).isFalse();
		assertThat(orderTable2.isGrouped()).isFalse();
	}


	@DisplayName("그룹화된 주문테이블들 중 조리상태이거나 식사상태이면 그룹해제를 할 수 없다.")
	@Test
	void ungroupWithNotCompleteOrderTest() {
		// given
		Order order = mock(Order.class);
		when(order.isComplete()).thenReturn(false);

		OrderTable notCompletedOrderTable = new OrderTable(1, true, asList(order));
		OrderTable orderTable =  new OrderTable(1, true);
		TableGroup tableGroup = new TableGroup(OrderTables.of(notCompletedOrderTable, orderTable), LocalDateTime.now());

		assertThatThrownBy(() -> tableGroup.ungroup())
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("조리상태이거나 식사상태인 주문이 있는 주문테이블은 그룹해제를 할 수 없습니다.");
	}
}