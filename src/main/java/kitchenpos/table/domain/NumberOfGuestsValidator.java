package kitchenpos.table.domain;

import static kitchenpos.table.exception.CannotChangeNumberOfGuestsException.Type.INVALID_NUMBER_OF_GUESTS;
import static kitchenpos.table.exception.CannotChangeNumberOfGuestsException.Type.NOT_EMPTY;

import org.springframework.stereotype.Component;

import kitchenpos.table.exception.CannotChangeNumberOfGuestsException;

@Component
public class NumberOfGuestsValidator {

	public void validate(OrderTable orderTable) {
		validateNumberOfGuests(orderTable.getNumberOfGuests());
		validateNotEmptyTable(orderTable.isEmpty());
	}

	private void validateNumberOfGuests(Integer numberOfGuests) {
		if (numberOfGuests < 0) {
			throw new CannotChangeNumberOfGuestsException(INVALID_NUMBER_OF_GUESTS);
		}
	}

	private void validateNotEmptyTable(boolean empty) {
		if (!empty) {
			throw new CannotChangeNumberOfGuestsException(NOT_EMPTY);
		}
	}
}
