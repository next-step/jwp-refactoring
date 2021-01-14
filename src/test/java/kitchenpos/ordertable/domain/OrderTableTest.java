package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

	@DisplayName("테이블 상태 변경")
	@Test
	void changeEmpty() {
		//given
		OrderTable orderTable = new OrderTable(false);

		//when
		orderTable.changeEmpty(true);

		//then
		assertThat(orderTable.isEmpty()).isTrue();
	}

	@DisplayName("단체 설정이 되어있는 경우 테이블 상태를 변경할 수 없다.")
	@Test
	void changeEmptyWithGroupTable() {
		//given
		OrderTable orderTable = new OrderTable(1L, 1, false);

		//when, then
		assertThatIllegalArgumentException()
			  .isThrownBy(() -> orderTable.changeEmpty(true))
			  .withMessage("단체 지정된 테이블은 상태를 변경할 수 없습니다.");
	}

	@DisplayName("테이블의 게스트수를 변경한다.")
	@Test
	void changeNumberOfGuests() {
		//given
		OrderTable orderTable = new OrderTable(1L, 0, false);

		//when
		int newNumberOfGuest = 2;
		orderTable.changeNumberOfGuests(newNumberOfGuest);

		//then
		assertThat(orderTable.getNumberOfGuests()).isEqualTo(newNumberOfGuest);
	}

	@DisplayName("게스트수가 0 미만인 경우 게스트수를 변경할 수 없다.")
	@Test
	void changeNumberOfGuestsWithWrongGuestNumber() {
		//given
		OrderTable orderTable = new OrderTable(1L, 0, false);

		//when, then
		assertThatIllegalArgumentException()
			  .isThrownBy(() -> orderTable.changeNumberOfGuests(-1))
			  .withMessage("게스트 수는 0명 이상이어야 합니다.");
	}

	@DisplayName("게스트수가 0 미만인 경우 게스트수를 변경할 수 없다.")
	@Test
	void changeNumberOfGuestsWithEmptyTable() {
		//given
		OrderTable orderTable = new OrderTable(1L, 0, true);

		//when, then
		assertThatIllegalArgumentException()
			  .isThrownBy(() -> orderTable.changeNumberOfGuests(2))
			  .withMessage("테이블이 비어있습니다.");
	}

	@DisplayName("테이블 그룹 지정")
	@Test
	void setGroup() {
		//given
		OrderTable orderTable = new OrderTable();

		//when
		orderTable.setTableGroup(1L);

		//then
		assertThat(orderTable.getTableGroupId()).isEqualTo(1L);
	}

	@DisplayName("단체테이블로 등록된 테이블은 단체설정을 할 수 없다.")
	@Test
	void setGroupOtherTableGroup() {
		//given
		OrderTable orderTable = new OrderTable();
		orderTable.setTableGroup(1L);

		//when, then
		assertThatIllegalArgumentException()
			  .isThrownBy(() -> orderTable.setTableGroup(2L))
			  .withMessage("단체 지정이 불가능한 테이블입니다.");
	}

	@DisplayName("단체 테이블 지정 해제")
	@Test
	void unTableGroup() {
		//given
		OrderTable orderTable = new OrderTable();
		orderTable.setTableGroup(1L);

		//when
		orderTable.unTableGroup();

		//then
		assertThat(orderTable.getTableGroupId()).isNull();
	}
}
