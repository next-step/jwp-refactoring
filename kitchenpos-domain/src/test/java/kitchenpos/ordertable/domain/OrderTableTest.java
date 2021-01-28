package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import kitchenpos.tablegroup.domain.TableGroup;

class OrderTableTest {

	@DisplayName("테이블 상태 변경")
	@Test
	void changeEmpty() {
		// given
		OrderTable orderTable = OrderTable.of(0, false);

		// when
		orderTable.changeEmptyState(true);

		// then
		assertThat(orderTable.isEmpty()).isTrue();
	}

	@DisplayName("단체 설정이 되어있는 경우 테이블 상태를 변경할 수 없다.")
	@Test
	void changeEmptyWithGroupTable() {
		// given
		TableGroup tableGroup = TableGroup.of();
		ReflectionTestUtils.setField(tableGroup, "id", 1L);
		OrderTable orderTable = OrderTable.of(1L, 5, false, tableGroup);

		// when, then
		assertThatIllegalArgumentException()
			.isThrownBy(() -> orderTable.changeEmptyState(true))
			.withMessage("단체 지정된 테이블은 상태를 변경할 수 없습니다.");
	}

	@DisplayName("테이블의 게스트수를 변경한다.")
	@Test
	void changeNumberOfGuests() {
		// given
		int changeGuestNumber = 5;
		TableGroup tableGroup = TableGroup.of();
		ReflectionTestUtils.setField(tableGroup, "id", 1L);
		OrderTable orderTable = OrderTable.of(null, 3, false, tableGroup);

		// when
		orderTable.changeNumberOfGuests(changeGuestNumber);

		// then
		assertThat(orderTable.numberOfGuests()).isEqualTo(changeGuestNumber);
	}

	@DisplayName("게스트수가 0 미만인 경우 게스트수를 변경할 수 없다.")
	@Test
	void changeGuestNumberError() {
		//given
		TableGroup tableGroup = TableGroup.of();
		ReflectionTestUtils.setField(tableGroup, "id", 1L);
		OrderTable orderTable = OrderTable.of(null, 3, false, tableGroup);

		//when, then
		assertThatIllegalArgumentException()
			.isThrownBy(() -> orderTable.changeNumberOfGuests(-1))
			.withMessage("손님은 0명 이상이여야 합니다.");
	}

	@DisplayName("테이블이 비어있 경우 게스트수를 변경할 수 없다.")
	@Test
	void changeNumberOfGuestsWithEmptyTable() {
		//given
		TableGroup tableGroup = TableGroup.of();
		ReflectionTestUtils.setField(tableGroup, "id", 1L);
		OrderTable orderTable = OrderTable.of(null, 0, true, tableGroup);

		//when, then
		assertThatIllegalArgumentException()
			.isThrownBy(() -> orderTable.changeNumberOfGuests(2))
			.withMessage("테이블이 비어있습니다.");
	}
}
