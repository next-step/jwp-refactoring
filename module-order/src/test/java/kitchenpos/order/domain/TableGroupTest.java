package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

	@DisplayName("비어있지 않은 테이블을 단체지정 하면 오류 발생")
	@Test
	void testAddOrderNotEmptyTable() {
		TableGroup tableGroup = new TableGroup(LocalDateTime.now());
		assertThatThrownBy(() -> {
			tableGroup.addOrderTable(new OrderTable(null, new NumberOfGuests(3), false));
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("주문테이블이 단체지정이 되어있거나, 비어있지 않은 테이블입니다.");
	}

	@DisplayName("이미 단체지정 되어있는 테이블을 단체지정 시도하면 오류 발생")
	@Test
	void testAddOrderAlreadyTableGroup() {
		TableGroup tableGroup = new TableGroup(LocalDateTime.now());
		assertThatThrownBy(() -> {
			tableGroup.addOrderTable(new OrderTable(tableGroup, new NumberOfGuests(3), true));
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("주문테이블이 단체지정이 되어있거나, 비어있지 않은 테이블입니다.");
	}

	@DisplayName("단체 지정 테스트")
	@Test
	void testAddOrderTable() {
		TableGroup tableGroup = new TableGroup(LocalDateTime.now());
		OrderTable orderTable = new OrderTable(null, new NumberOfGuests(3), true);
		tableGroup.addOrderTable(orderTable);

		Assertions.assertThat(orderTable.getTableGroup()).isEqualTo(tableGroup);
	}
}