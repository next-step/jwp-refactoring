package kitchenpos.tablegroup.domain;

import static kitchenpos.common.DomainFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.exception.InvalidTableGroupException;

class TableGroupTest {

	@DisplayName("주문 테이블 목록이 유효하지 않으면 예외발생")
	@Test
	void of_invalid_order_tables() {
		assertThatExceptionOfType(InvalidTableGroupException.class)
			.isThrownBy(() -> TableGroup.of(null));

		assertThatExceptionOfType(InvalidTableGroupException.class)
			.isThrownBy(() -> TableGroup.of(Collections.emptyList()));

		assertThatExceptionOfType(InvalidTableGroupException.class)
			.isThrownBy(() -> TableGroup.of(Arrays.asList(
				OrderTable.of(3, true)
			)));
	}

	@DisplayName("그룹지정")
	@Test
	void group() {
		final OrderTable 논그룹_빈테이블1 = orderTable(1L, null, 2, true);
		final OrderTable 논그룹_빈테이블2 = orderTable(2L, null, 3, true);
		final List<OrderTable> 테이블목록 = Arrays.asList(논그룹_빈테이블1, 논그룹_빈테이블2);

		final TableGroup 그룹 = TableGroup.of(테이블목록);

		테이블목록.forEach(테이블 -> {
			assertThat(테이블.isEmpty()).isFalse();
			assertThat(테이블.getTableGroup()).isEqualTo(그룹);
		});
	}

	@DisplayName("그룹지정: 테이블이 비어있지 않으면 예외발생")
	@Test
	void group_not_empty_order_table() {
		final OrderTable 논그룹_비어있지_않은_테이블 = orderTable(1L, null, 2, false);
		final OrderTable 논그룹_빈테이블 = orderTable(2L, null, 2, true);
		final List<OrderTable> 테이블목록 = Arrays.asList(논그룹_비어있지_않은_테이블, 논그룹_빈테이블);

		assertThatExceptionOfType(InvalidTableGroupException.class)
			.isThrownBy(() -> TableGroup.of(테이블목록));
	}

	@DisplayName("그룹지정: 이미 되어있으면 예외발생")
	@Test
	void group_having_table_group_already() {
		final OrderTable 그룹_빈테이블1 = orderTable(1L, null, 1, true);
		final OrderTable 그룹_빈테이블2 = orderTable(2L, null, 2, true);
		TableGroup.of(Arrays.asList(그룹_빈테이블1, 그룹_빈테이블2));
		final OrderTable 논그룹_빈테이블3 = orderTable(3L, null, 3, true);

		assertThatExceptionOfType(InvalidTableGroupException.class)
			.isThrownBy(() -> TableGroup.of(Arrays.asList(그룹_빈테이블1, 논그룹_빈테이블3)));
	}

	@DisplayName("그룹해지")
	@Test
	void ungroup() {
		final OrderTable 그룹_빈테이블1 = orderTable(1L, null, 1, true);
		final OrderTable 그룹_빈테이블2 = orderTable(2L, null, 2, true);
		final List<OrderTable> 테이블목록 = Arrays.asList(그룹_빈테이블1, 그룹_빈테이블2);
		final TableGroup 그룹 = TableGroup.of(테이블목록);

		그룹.ungroup();

		테이블목록.forEach(테이블 -> assertThat(테이블.getTableGroup()).isNull());
	}
}
