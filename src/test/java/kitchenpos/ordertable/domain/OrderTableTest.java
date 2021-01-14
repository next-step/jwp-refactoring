package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

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
		TableGroup tableGroup = new TableGroup();
		ReflectionTestUtils.setField(tableGroup, "id", 1L);
		OrderTable orderTable = new OrderTable(1, false, tableGroup);

		//when, then
		assertThatIllegalArgumentException()
			  .isThrownBy(() -> orderTable.changeEmpty(true))
			  .withMessage("단체 지정된 테이블은 상태를 변경할 수 없습니다.");
	}

	@DisplayName("테이블의 게스트수를 변경한다.")
	@Test
	void changeNumberOfGuests() {
		//given
		TableGroup tableGroup = new TableGroup();
		ReflectionTestUtils.setField(tableGroup, "id", 1L);
		OrderTable orderTable = new OrderTable(0, false, tableGroup);

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
		TableGroup tableGroup = new TableGroup();
		ReflectionTestUtils.setField(tableGroup, "id", 1L);
		OrderTable orderTable = new OrderTable(0, false, tableGroup);

		//when, then
		assertThatIllegalArgumentException()
			  .isThrownBy(() -> orderTable.changeNumberOfGuests(-1))
			  .withMessage("게스트 수는 0명 이상이어야 합니다.");
	}

	@DisplayName("테이블이 비어있 경우 게스트수를 변경할 수 없다.")
	@Test
	void changeNumberOfGuestsWithEmptyTable() {
		//given
		TableGroup tableGroup = new TableGroup();
		ReflectionTestUtils.setField(tableGroup, "id", 1L);
		OrderTable orderTable = new OrderTable(0, true, tableGroup);

		//when, then
		assertThatIllegalArgumentException()
			  .isThrownBy(() -> orderTable.changeNumberOfGuests(2))
			  .withMessage("테이블이 비어있습니다.");
	}
}
