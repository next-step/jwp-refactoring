package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.ordertablegroup.domain.OrderTableGroup;

@DisplayName("주문 테이블")
class OrderTableTest {

	@DisplayName("생성")
	@Test
	void of() {
		// given
		NumberOfGuests numberOfGuests = NumberOfGuests.of(4);
		boolean empty = false;

		// when
		OrderTable orderTable = OrderTable.of(numberOfGuests, empty);

		// then
		assertAll(
			() -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests),
			() -> assertThat(orderTable.isEmpty()).isEqualTo(empty));
	}

	@DisplayName("빈 상태 변경")
	@Test
	void changeEmpty() {
		// given
		OrderTable orderTable = OrderTable.of(NumberOfGuests.of(4), false);

		// when
		orderTable.changeEmpty(true);

		// then
		assertThat(orderTable.isEmpty()).isEqualTo(true);
	}

	@DisplayName("빈 상태 변경 - 주문 테이블 그룹에 속해 있는 경우")
	@Test
	void changeEmptyFailOnBelongToOrderTableGroup() {
		// given
		OrderTable orderTable1 = OrderTable.of(NumberOfGuests.of(4), false);
		OrderTable orderTable2 = OrderTable.of(NumberOfGuests.of(4), false);
		OrderTableGroup orderTableGroup = OrderTableGroup.of(Arrays.asList(orderTable1, orderTable2));

		// when
		ThrowingCallable throwingCallable = () -> orderTable1.changeEmpty(true);

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalStateException.class);
	}

	@DisplayName("손님 수 변경")
	@Test
	void changeNumberOfGuests() {
		// given
		OrderTable orderTable = OrderTable.of(NumberOfGuests.of(4), false);
		NumberOfGuests numberOfGuests = NumberOfGuests.of(6);

		// when
		orderTable.changeNumberOfGuests(numberOfGuests);

		// then
		assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
	}

	@DisplayName("손님 수 변경 실패 - 비어 있는 경우")
	@Test
	void changeNumberOfGuestsFailOnEmpty() {
		// given
		OrderTable orderTable = OrderTable.of(NumberOfGuests.of(4), true);
		NumberOfGuests numberOfGuests = NumberOfGuests.of(6);

		// when
		ThrowingCallable throwingCallable = () -> orderTable.changeNumberOfGuests(numberOfGuests);

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalStateException.class);
	}
}
