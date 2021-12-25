package kitchenpos.tablegroup.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.exception.CanNotGroupByEmptyException;
import kitchenpos.tablegroup.exception.CanNotGroupByGroupingAlreadyException;
import kitchenpos.tablegroup.exception.InvalidOrderTablesException;
import kitchenpos.tablegroup.exception.NotFoundOrderTablesException;

class TableGroupTest {

	@DisplayName("그룹생성: 주문테이블 목록이 없으면 예외발생")
	@Test
	void of_null_order_tables() {
		assertThatExceptionOfType(NotFoundOrderTablesException.class)
			.isThrownBy(() -> TableGroup.of(null));
	}

	@DisplayName("그룹생성: 주문테이블 목록이 비었으면 예외발생")
	@Test
	void of_empty_order_tables() {
		assertThatExceptionOfType(NotFoundOrderTablesException.class)
			.isThrownBy(() -> TableGroup.of(Collections.emptyList()));
	}

	@DisplayName("그룹생성: 주문테이블 목록이 1개이면 예외발생")
	@Test
	void of_only_1_order_tables() {
		assertThatExceptionOfType(InvalidOrderTablesException.class)
			.isThrownBy(() -> TableGroup.of(
				Arrays.asList(OrderTable.of(3, true))
			));
	}

	@DisplayName("그룹생성")
	@Test
	void group() {
		final OrderTable 논그룹_빈테이블1 = OrderTable.of(1L, null, 2, true);
		final OrderTable 논그룹_빈테이블2 = OrderTable.of(2L, null, 3, true);

		final TableGroup 그룹 = TableGroup.of(1L, Arrays.asList(논그룹_빈테이블1, 논그룹_빈테이블2));

		assertAll(
			() -> assertThat(논그룹_빈테이블1.isEmpty()).isFalse(),
			() -> assertThat(논그룹_빈테이블1.getTableGroupId()).isEqualTo(그룹.getId()),
			() -> assertThat(논그룹_빈테이블2.isEmpty()).isFalse(),
			() -> assertThat(논그룹_빈테이블2.getTableGroupId()).isEqualTo(그룹.getId())
		);
	}

	@DisplayName("그룹생성: 테이블이 비어있지 않으면 예외발생")
	@Test
	void group_not_empty_order_table() {
		final OrderTable 논그룹_비어있지_않은_테이블 = OrderTable.of(1L, null, 2, false);
		final OrderTable 논그룹_빈테이블 = OrderTable.of(2L, null, 2, true);
		final List<OrderTable> 테이블목록 = Arrays.asList(논그룹_비어있지_않은_테이블, 논그룹_빈테이블);

		assertThatExceptionOfType(CanNotGroupByEmptyException.class)
			.isThrownBy(() -> TableGroup.of(테이블목록));
	}

	@DisplayName("그룹생성: 이미 되어있으면 예외발생")
	@Test
	void group_having_table_group_already() {
		final OrderTable 그룹_빈테이블1 = OrderTable.of(1L, null, 1, true);
		final OrderTable 그룹_빈테이블2 = OrderTable.of(2L, null, 2, true);
		TableGroup.of(1L, Arrays.asList(그룹_빈테이블1, 그룹_빈테이블2));
		final OrderTable 논그룹_빈테이블3 = OrderTable.of(3L, null, 3, true);

		assertThatExceptionOfType(CanNotGroupByGroupingAlreadyException.class)
			.isThrownBy(() -> TableGroup.of(Arrays.asList(그룹_빈테이블1, 논그룹_빈테이블3)));
	}

	@DisplayName("그룹해지")
	@Test
	void ungroup() {
		final OrderTable 그룹_빈테이블1 = OrderTable.of(1L, null, 1, true);
		final OrderTable 그룹_빈테이블2 = OrderTable.of(2L, null, 2, true);
		final TableGroup 그룹 = TableGroup.of(Arrays.asList(그룹_빈테이블1, 그룹_빈테이블2));

		그룹.ungroup();

		assertAll(
			() -> assertThat(그룹_빈테이블1.getTableGroupId()).isNull(),
			() -> assertThat(그룹_빈테이블2.getTableGroupId()).isNull()
		);
	}
}
