package kitchenpos.domain;

public class NumberOfGuest {
    private Long numberOfGuests;

    public NumberOfGuest(Long numberOfGuests) {
        validate(numberOfGuests);

        this.numberOfGuests = numberOfGuests;
    }

    private void validate(Long numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    public Long toLong() {
        return numberOfGuests;
    }
}
