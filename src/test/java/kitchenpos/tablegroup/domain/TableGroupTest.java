package kitchenpos.tablegroup.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.tablegroup.exception.TableException;

@DisplayName("테이블 그룹 : 단위 테스트")
class TableGroupTest {

	@DisplayName("테이블 그룹 생성시 주문 테이블이 없는 경우 예외처리 테스트")
	@Test
	void createNullOrderTables() {
		// given
		List<OrderTable> orderTables = Collections.emptyList();

		// when // then
		assertThatThrownBy(() -> {
			TableGroup.from(orderTables);
		}).isInstanceOf(TableException.class);
	}

	@DisplayName("테이블 그룹 생성시 주문 테이블이 하나인 경우 예외처리 테스트")
	@Test
	void createOneOrderTable() {
		// given
		OrderTable orderTable = OrderTable.of(10, true);
		List<OrderTable> orderTables = Collections.singletonList(orderTable);

		// when // then우
		assertThatThrownBy(() -> {
			TableGroup.from(orderTables);
		}).isInstanceOf(TableException.class);
	}

	@DisplayName("테이블 그룹 생성시 주문 테이블이 비어있지 않은 경 예외처리 테스트")
	@Test
	void createEmptyOrderTable() {
		// given
		OrderTable orderTable1 = OrderTable.of(10, false);
		OrderTable orderTable2 = OrderTable.of(10, true);

		// when // then
		assertThatThrownBy(() -> {
			TableGroup.from(Arrays.asList(orderTable1, orderTable2));
		}).isInstanceOf(TableException.class);
	}
}
