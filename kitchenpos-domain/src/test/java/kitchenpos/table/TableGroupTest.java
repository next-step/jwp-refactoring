package kitchenpos.table;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {
	@DisplayName("테이블 수가 2개 미만일때 IllegalArgumentException 발생")
	@Test
	void createWhenLessThanTwo() {
		List<OrderTable> orderTables = Arrays.asList(
			OrderTable.builder().build()
		);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> new TableGroup().group(orderTables));
	}

	@DisplayName("주문 테이블일 경우 IllegalArgumentException 발생")
	@Test
	void createWhenOrderTable() {
		List<OrderTable> orderTables = Arrays.asList(
			OrderTable.builder().empty(true).build(),
			OrderTable.builder().empty(false).build()
		);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> new TableGroup().group(orderTables));
	}

	@DisplayName("주문 테이블일 경우 IllegalArgumentException 발생")
	@Test
	void createWhenAlreadyGroup() {
		List<OrderTable> orderTables = Arrays.asList(
			OrderTable.builder().empty(true).build(),
			OrderTable.builder().empty(true).tableGroupId(1L).build()
		);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> new TableGroup().group(orderTables));
	}

	@DisplayName("단체 지정")
	@Test
	void create() {
		List<OrderTable> orderTables = Arrays.asList(
			OrderTable.builder().empty(true).build(),
			OrderTable.builder().empty(true).build()
		);

		TableGroup tableGroup = new TableGroup();
		tableGroup.group(orderTables);

		assertThat(tableGroup.getOrderTables()).hasSize(2);
	}
}
