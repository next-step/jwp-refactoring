package kitchenpos.common.fixture;

import kitchenpos.table.domain.NumberOfGuests;

public class NumberOfGuestsFixture {

    public static NumberOfGuests numberOfGuests() {
        return new NumberOfGuests(1);
    }

}
