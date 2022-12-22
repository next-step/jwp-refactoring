package kitchenpos.table.domain;

public class OrderTableValidator {

    public static void validate(int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
    }

    public static void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("게스트의 수는 음수여서는 안됩니다.");
        }
    }

}
