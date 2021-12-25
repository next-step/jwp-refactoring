package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TableGuest {

    @Column(name = "number_of_guests", nullable = false)
    private int numberOfGuests;

    protected TableGuest() {

    }

    public TableGuest(int numberOfGuests) {
        if(numberOfGuests < 0) {
            throw new IllegalArgumentException("조정되는 인원수가 음수일 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumber() {
        return numberOfGuests;
    }
}
