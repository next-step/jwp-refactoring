package kitchenpos.ordertable.domain;

import static kitchenpos.ordertable.OrderTableFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테이블")
class OrderTableTest {

	@DisplayName("생성")
	@Test
	void of() {
		// given
		NumberOfGuests numberOfGuests = NumberOfGuests.from(4);
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
		OrderTable orderTable = 비어있지않은_주문_테이블_1번();

		// when
		orderTable.changeEmpty(true, new ValidOrderTableValidator());

		// then
		assertThat(orderTable.isEmpty()).isEqualTo(true);
	}

	@DisplayName("빈 상태 변경 실패 - 주문 테이블 그룹에 속해 있는 경우")
	@Test
	void changeEmptyFailOnBelongToOrderTableGroup() {
		// given
		OrderTable orderTable = 그룹핑된_주문_테이블_5번();

		// when
		ThrowingCallable throwingCallable = () -> orderTable.changeEmpty(true, new ValidOrderTableValidator());

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalStateException.class);
	}

	@DisplayName("빈 상태 변경 실패 - 완료되지 않은 주문이 남아 있는 경우")
	@Test
	void changeEmptyFailOnOrderNotCompleted() {
		// given
		OrderTable orderTable = 비어있지않은_주문_테이블_1번();

		// when
		ThrowingCallable throwingCallable = () -> orderTable.changeEmpty(
			true,
			new NotCompletedOrderExistOrderTableValidator());

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalStateException.class);
	}

	@DisplayName("손님 수 변경")
	@Test
	void changeNumberOfGuests() {
		// given
		OrderTable orderTable = 비어있지않은_주문_테이블_1번();
		NumberOfGuests numberOfGuests = NumberOfGuests.from(6);

		// when
		orderTable.changeNumberOfGuests(numberOfGuests);

		// then
		assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
	}

	@DisplayName("손님 수 변경 실패 - 비어 있는 경우")
	@Test
	void changeNumberOfGuestsFailOnEmpty() {
		// given
		OrderTable orderTable = 빈_주문_테이블_3번();
		NumberOfGuests numberOfGuests = NumberOfGuests.from(6);

		// when
		ThrowingCallable throwingCallable = () -> orderTable.changeNumberOfGuests(numberOfGuests);

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalStateException.class);
	}
}
