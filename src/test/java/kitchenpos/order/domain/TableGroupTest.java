package kitchenpos.order.domain;

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
			.isThrownBy(() -> TableGroup.builder().orderTables(orderTables).build());
	}

	@DisplayName("주문 테이블일 경우 IllegalArgumentException 발생")
	@Test
	void createWhenOrderTable() {
		List<OrderTable> orderTables = Arrays.asList(
			OrderTable.builder().empty(true).build(),
			OrderTable.builder().empty(false).build()
		);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> TableGroup.builder().orderTables(orderTables).build());
	}

	@DisplayName("단체 지정")
	@Test
	void create() {
		List<OrderTable> orderTables = Arrays.asList(
			OrderTable.builder().empty(true).build(),
			OrderTable.builder().empty(true).build()
		);

		TableGroup tableGroup = TableGroup.builder().orderTables(orderTables).build();

		assertThat(tableGroup.getOrderTables()).hasSize(2);
	}

	@DisplayName("주문 테이블일 경우 IllegalArgumentException 발생")
	@Test
	void createWhenAlreadyGroup() {
		OrderTable orderTable1 = OrderTable.builder().empty(true).build();
		OrderTable orderTable2 = OrderTable.builder().empty(true).build();
		OrderTable orderTable3 = OrderTable.builder()
			.empty(true)
			.tableGroup(TableGroup.builder().orderTables(Arrays.asList(orderTable1, orderTable2)).build())
			.build();

		assertThatIllegalArgumentException()
			.isThrownBy(() -> TableGroup.builder().orderTables(Arrays.asList(orderTable2, orderTable3)).build());
	}
}
