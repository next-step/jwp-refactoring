package kitchenpos.ui.dto.ordertable;

import java.util.Objects;

public class ChangeNumberOfGuestsRequest {
    private int numberOfGuests;

    public ChangeNumberOfGuestsRequest() {
    }

    public ChangeNumberOfGuestsRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ChangeNumberOfGuestsRequest that = (ChangeNumberOfGuestsRequest) o;
        return numberOfGuests == that.numberOfGuests;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfGuests);
    }

    @Override
    public String toString() {
        return "ChangeNumberOfGuestsRequest{" +
                "numberOfGuests=" + numberOfGuests +
                '}';
    }
}
