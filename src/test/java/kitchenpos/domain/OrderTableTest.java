package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

	@DisplayName("빈 테이블의 방문 고객수를 변경하면 오류 발생")
	@Test
	void testChangeNumberOfGuestsError() {
		TableGroup tableGroup = new TableGroup(LocalDateTime.of(2021, 7, 4, 0, 0, 0), null);
		OrderTable orderTable = new OrderTable(tableGroup, new NumberOfGuests(3), true);
		assertThatThrownBy(() -> {
			orderTable.changeNumberOfGuests(new NumberOfGuests(4));
		}).isInstanceOf(
			IllegalArgumentException.class)
			.hasMessageContaining("비어있는 테이블입니다.");
	}

	@DisplayName("방문 고객수 변경 확인")
	@Test
	void testChangeNumberOfGuests() {
		OrderTable orderTable = new OrderTable(null, new NumberOfGuests(3), false);
		orderTable.changeNumberOfGuests(new NumberOfGuests(4));

		assertThat(orderTable.getNumberOfGuests()).isEqualTo(new NumberOfGuests(4));
	}

	@DisplayName("주문 테이블의 비어있는 상태 변경 테스트")
	@Test
	void testChangeEmpty() {

		OrderTable orderTable = new OrderTable(null, new NumberOfGuests(3), false);
		orderTable.changeEmpty(true);

		assertThat(orderTable.isEmpty()).isTrue();
	}

	@DisplayName("단체 지정 테이블의 경우 비어있는 상태 변경시 오류발생")
	@Test
	void testChangeEmptyError() {
		TableGroup tableGroup = new TableGroup(LocalDateTime.of(2021, 7, 4, 0, 0, 0), null);
		OrderTable orderTable = new OrderTable(tableGroup, new NumberOfGuests(3), false);

		Assertions.assertThatThrownBy(() -> {
			orderTable.changeEmpty(true);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("단체 지정되어있는 테이블은 변경할 수 없습니다.");
	}
}