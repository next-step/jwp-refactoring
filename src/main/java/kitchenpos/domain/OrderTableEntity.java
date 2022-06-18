package kitchenpos.domain;

import javax.persistence.*;

@Entity(name = "order_table")
public class OrderTableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private TableGroupEntity tableGroup;
    @Embedded
    private NumberOfGuests numberOfGuests;
    @Embedded
    private Empty empty;

    public OrderTableEntity(int numberOfGuests, boolean empty) {
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = new Empty(empty);
    }

    protected OrderTableEntity() {
    }

    public void bindTo(TableGroupEntity tableGroup) {
        this.tableGroup = tableGroup;
        this.empty = new Empty(false);
    }

    public void unbind() {
        tableGroup = null;
    }

    public boolean isGrouped() {
        return tableGroup != null;
    }

    public boolean isEmpty() {
        return empty.isTrue();
    }

    public Long getId() {
        return id;
    }

    public TableGroupEntity getTableGroup() {
        return tableGroup;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public void changeEmpty(boolean empty) {
        if (isGrouped()) {
            throw new CannotChangeEmptyException();
        }
        this.empty = new Empty(empty);
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (isEmpty()) {
            throw new CannotChangeNumberOfGuestsException();
        }
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }
}
