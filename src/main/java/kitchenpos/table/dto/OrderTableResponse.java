package kitchenpos.table.dto;

import kitchenpos.tablegroup.domain.TableGroup;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class OrderTableResponse {

    private Long id;
    private int numberOfGuests;
    private boolean empty;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }
}
