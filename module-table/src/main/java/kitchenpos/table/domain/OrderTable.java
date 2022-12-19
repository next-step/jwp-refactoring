package kitchenpos.table.domain;

import kitchenpos.table.exception.CannotChangeNumberOfGuestsException;
import org.springframework.data.domain.AbstractAggregateRoot;
import javax.persistence.*;

@Entity
public class OrderTable extends AbstractAggregateRoot<OrderTable> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tableGroupId;

    @Embedded
    private NumberOfGuests numberOfGuests;

    private boolean empty;

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    protected OrderTable() {
    }

    public void bindTo(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    public void unbind() {
        tableGroupId = null;
    }

    public boolean isGrouped() {
        return tableGroupId != null;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(boolean empty) {
        this.empty = empty;
        registerEvent(new TableEmptyChangedEvent(id));
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (isEmpty()) {
            throw new CannotChangeNumberOfGuestsException();
        }
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }
}
