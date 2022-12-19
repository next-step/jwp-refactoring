package kitchenpos.table.domain.fixture;

import kitchenpos.table.domain.NumberOfGuests;

public class NumberOfGuestsFixture {

    public static NumberOfGuests initNumberOfGuests() {
        return new NumberOfGuests(0);
    }

    public static NumberOfGuests numberOfGuests() {
        return new NumberOfGuests(1);
    }
}
