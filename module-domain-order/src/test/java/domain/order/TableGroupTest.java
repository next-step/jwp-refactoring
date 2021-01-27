package domain.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;

class TableGroupTest {

	private OrderTable orderTable1;
	private OrderTable orderTable2;
	private OrderTable orderTable3;
	private TableGroup tableGroup;

	@BeforeEach
	void setUp() {
		tableGroup = spy(new TableGroup());
		given(tableGroup.getId()).willReturn(5L);
		orderTable1 = new OrderTable(20, true);
		orderTable2 = new OrderTable(20, true);
		orderTable3 = new OrderTable(20, true);
	}

	@DisplayName("여러 주문테이블을 묶어 단체지정한다.")
	@Test
	void fromGroupingTables() {
		// when
		tableGroup.groupTables(Arrays.asList(orderTable1, orderTable2, orderTable3));

		// then
		assertThat(tableGroup.getOrderTables())
				.containsExactly(orderTable1, orderTable2, orderTable3);
	}

	@DisplayName("단체지정하려는 주문테이블 수가 적으면 예외 발생.")
	@Test
	void fromGroupingTables_Exception() {
		assertThatThrownBy(() -> tableGroup.groupTables(Collections.singletonList(orderTable1)))
				.isInstanceOf(TableGroupValidationException.class)
				.hasMessageMatching(TableGroup.MSG_TABLE_COUNT_LEAST);
	}
}
