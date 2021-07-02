package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

	@DisplayName("2개 이상의 주문테이블만 그룹화 할 수 있다.")
	@Test
	void createTableGroupWithLessTwoOrderTables() {
		OrderTable orderTable = new OrderTable(1L, 1, true);
		OrderTables orderTables = OrderTables.of(orderTable);

		assertThatThrownBy(() -> new TableGroup(orderTables, LocalDateTime.now()))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("2개 미만의 주문테이블은 그룹화 할 수 없습니다.");
	}

	@DisplayName("그룹화할 주문테이블들은 모두 빈 테이블이어야 한다.")
	@Test
	void createTableGroupWithNotEmptyOrderTableTest() {
		OrderTable notEmptyTable = new OrderTable(1L, 1, false);
		OrderTable orderTable = new OrderTable(2L, 1, true);
		OrderTables orderTables = OrderTables.of(orderTable, notEmptyTable);

		assertThatThrownBy(() -> new TableGroup(orderTables, LocalDateTime.now()))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("비어있지 않거나, 이미 그룹화되어 있는 테이블은 그룹화 할 수 없습니다.");
	}

	@DisplayName("그룹화할 주문테이블들은 모두 그룹화되지 않은 테이블이어야 한다.")
	@Test
	void createTableGroupWithGroupedOrderTableTest() {
		OrderTable groupedTable1 = new OrderTable(1L, 1, true);
		OrderTable groupedTable2 = new OrderTable(2L, 1, true);
		OrderTables orderTables = OrderTables.of(groupedTable1, groupedTable2);
		TableGroup tableGroup = new TableGroup(orderTables, LocalDateTime.now());
		OrderTable orderTable = new OrderTable(3L, 1, true);
		OrderTables orderTables1 = OrderTables.of(groupedTable1, orderTable);

		assertThatThrownBy(() -> new TableGroup(orderTables1, LocalDateTime.now()))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("비어있지 않거나, 이미 그룹화되어 있는 테이블은 그룹화 할 수 없습니다.");
	}

	@DisplayName("테이블그룹은 주문테이블들과 생성시각으로 생성된다.")
	@Test
	void createTest() {
		OrderTable orderTable1 = new OrderTable(1L, 1, true);
		OrderTable orderTable2 = new OrderTable(2L, 1, true);
		OrderTables orderTables = OrderTables.of(orderTable1, orderTable2);
		LocalDateTime createdDate = LocalDateTime.now();

		TableGroup tableGroup = new TableGroup(orderTables, createdDate);

		assertThat(orderTable1.getTableGroup()).isEqualTo(tableGroup);
		assertThat(orderTable2.getTableGroup()).isEqualTo(tableGroup);
		assertThat(tableGroup.getOrderTables()).containsExactly(orderTable1, orderTable2);
		assertThat(tableGroup.getCreatedDate()).isEqualTo(createdDate);
	}

}