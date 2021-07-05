package kitchenpos.domain;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

	@DisplayName("2개 이상의 주문테이블만 그룹화 할 수 있다.")
	@Test
	void createTableGroupWithLessTwoOrderTables() {
		// given
		OrderTable orderTable = new OrderTable(1, true);

		// when
		// than
		assertThatThrownBy(() -> TableGroup.create(asList(orderTable), LocalDateTime.now()))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("2개 미만의 주문테이블은 그룹화 할 수 없습니다.");
	}

	@DisplayName("그룹화할 주문테이블들은 모두 빈 테이블이어야 한다.")
	@Test
	void createTableGroupWithNotEmptyOrderTableTest() {
		// given
		OrderTable notEmptyTable = new OrderTable(1, false);
		OrderTable orderTable = new OrderTable(1, true);

		// when
		// than
		assertThatThrownBy(() -> TableGroup.create(asList(notEmptyTable, orderTable), LocalDateTime.now()))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("비어있지 않은 테이블은 그룹화 할 수 없습니다.");
	}

	@DisplayName("그룹화할 주문테이블들은 모두 그룹화되지 않은 테이블이어야 한다.")
	@Test
	void createTableGroupWithGroupedOrderTableTest() {
		// given
		OrderTable groupedTable1 = new OrderTable(1, true);
		OrderTable groupedTable2 = new OrderTable(1, true);
		TableGroup.create(asList(groupedTable1, groupedTable2), LocalDateTime.now());
		OrderTable orderTable = new OrderTable(1, true);

		// when
		// than
		assertThatThrownBy(() -> TableGroup.create(asList(groupedTable1, orderTable), LocalDateTime.now()))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("이미 그룹화되어 있는 테이블은 그룹화 할 수 없습니다.");
	}

	@DisplayName("그룹화된 주문테이블들은 모두 비어있지 않는 상태로 변한다.")
	@Test
	void createTableGroupTest() {
		// given
		OrderTable groupedTable1 = new OrderTable(1, true);
		OrderTable groupedTable2 = new OrderTable(1, true);

		// when
		TableGroup.create(asList(groupedTable1, groupedTable2), LocalDateTime.now());

		// than
		assertThat(groupedTable1.isEmpty()).isFalse();
		assertThat(groupedTable2.isEmpty()).isFalse();
	}

	@DisplayName("테이블그룹은 주문테이블들과 생성시각으로 생성된다.")
	@Test
	void createTest() {
		// given
		OrderTable orderTable1 = new OrderTable(1, true);
		OrderTable orderTable2 = new OrderTable(1, true);
		LocalDateTime createdDate = LocalDateTime.now();

		// when
		TableGroup tableGroup = TableGroup.create(asList(orderTable1, orderTable2), createdDate);

		// than
		assertThat(orderTable1.getTableGroup()).isEqualTo(tableGroup);
		assertThat(orderTable2.getTableGroup()).isEqualTo(tableGroup);
		assertThat(tableGroup.getOrderTables()).containsExactly(orderTable1, orderTable2);
		assertThat(tableGroup.getCreatedDate()).isEqualTo(createdDate);
	}

	@DisplayName("테이블그룹은 그룹에 소속된 테이블들을 그룹해제 시킬 수 있다.")
	@Test
	void ungroupTest() {
		// given
		OrderTable orderTable1 = new OrderTable(1, true);
		OrderTable orderTable2 =  new OrderTable(1, true);
		TableGroup tableGroup = TableGroup.create(asList(orderTable1, orderTable2), LocalDateTime.now());

		// when
		tableGroup.ungroup();

		// than
		assertThat(orderTable1.isGrouped()).isFalse();
		assertThat(orderTable2.isGrouped()).isFalse();
	}


	@DisplayName("그룹화된 주문테이블들 중 조리상태이거나 식사상태이면 그룹해제를 할 수 없다.")
	@Test
	void ungroupWithNotCompleteOrderTest() {
		// given
		Order order = mock(Order.class);
		when(order.isComplete()).thenReturn(false);

		// when
		OrderTable notCompletedOrderTable = new OrderTable(1, true, asList(order));
		OrderTable orderTable =  new OrderTable(1, true);
		TableGroup tableGroup = TableGroup.create(asList(notCompletedOrderTable, orderTable), LocalDateTime.now());

		// than
		assertThatThrownBy(() -> tableGroup.ungroup())
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("조리상태이거나 식사상태인 주문이 있는 주문테이블은 그룹해제를 할 수 없습니다.");
	}
}