package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OrderTableNumberOfGuests {
    @Column(nullable = false)
    private int numberOfGuests;

    protected OrderTableNumberOfGuests() {
    }

    public OrderTableNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
