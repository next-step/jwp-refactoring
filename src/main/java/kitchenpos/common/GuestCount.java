package kitchenpos.common;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.ErrorMessage;

@Embeddable
public class GuestCount {
    private static String PROPERTY_NAME = "손님수";
    @Column(name = "number_of_guests", nullable = false)
    private int value;

    protected GuestCount(){}

    private GuestCount(int value) {
        validateGuestCountNegative(value);
        this.value = value;
    }

    private void validateGuestCountNegative(int value) {
        if(value<0){
            throw new IllegalArgumentException(ErrorMessage.cannotBeNegative(PROPERTY_NAME));
        }
    }

    public static GuestCount of(int numberOfGuests) {
        return new GuestCount(numberOfGuests);
    }

    public int value(){
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GuestCount)) {
            return false;
        }
        GuestCount that = (GuestCount) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
