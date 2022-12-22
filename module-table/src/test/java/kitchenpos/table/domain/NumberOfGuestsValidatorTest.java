package kitchenpos.table.domain;

import static kitchenpos.table.exception.CannotChangeNumberOfGuestsException.Type.INVALID_NUMBER_OF_GUESTS;
import static kitchenpos.table.exception.CannotChangeNumberOfGuestsException.Type.NOT_EMPTY;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.table.OrderTableFixtures;
import kitchenpos.table.exception.CannotChangeNumberOfGuestsException;

class NumberOfGuestsValidatorTest {

	NumberOfGuestsValidator validator;

	@BeforeEach
	void setUp() {
		validator = new NumberOfGuestsValidator();
	}

	@Test
	@DisplayName("주문 테이블의 손님 수를 0 미만으로 변경할 수 없음")
	void testChangeNumberOfGuestsBelowThanZero() {
		OrderTable expectedOrderTable = OrderTableFixtures.numberOfGuests(-1);

		Assertions.assertThatThrownBy(() -> validator.validate(expectedOrderTable))
			.isInstanceOf(CannotChangeNumberOfGuestsException.class)
			.hasMessage(INVALID_NUMBER_OF_GUESTS.message);
	}

	@Test
	@DisplayName("주문 테이블이 비어있지 않은 경우 실패함")
	void testChangeNumberOfGuestsNotEmptyTable() {
		OrderTable expectedOrderTable = OrderTableFixtures.notEmptyOrderTable(0);

		Assertions.assertThatThrownBy(() -> validator.validate(expectedOrderTable))
			.isInstanceOf(CannotChangeNumberOfGuestsException.class)
			.hasMessage(NOT_EMPTY.message);
	}

}
