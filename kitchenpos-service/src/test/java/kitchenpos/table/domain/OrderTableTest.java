package kitchenpos.table.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {
	@Test
	@DisplayName("테이블 그룹 생성 시 테이블을 빈 테이블이 아니면 익셉션 발생")
	void fillTableTest() {
		OrderTable orderTable2 = new OrderTable(2L, null, 3, false);
		Assertions.assertThatThrownBy(orderTable2::fillTable)
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("테이블 그룹 생성 시 이미 테이블그룹이 존재하면 익셉션 발생")
	void addTableGroupIdFailTest() {
		OrderTable orderTable = new OrderTable(1L, 1L, 2, false);
		Assertions.assertThatThrownBy(() -> orderTable.addTableGroupId(1L))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("주문 테이블을 빈 테이블로 변경 시 테이블 그룹이 이미 존재할 시 익셉션 발생")
	void changeEmptyFailTest() {
		OrderTable orderTable = new OrderTable(1L, 1L, 2, false);

		Assertions.assertThatThrownBy(() -> orderTable.changeEmpty(true))
				.isInstanceOf(IllegalArgumentException.class);
	}
}
