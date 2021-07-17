package kitchenpos.table.domain;

import kitchenpos.order.domain.NumberOfGuests;
import kitchenpos.tablegroup.domain.TableGroup;

import javax.persistence.*;

@Entity
public class OrderTable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @JoinColumn(name = "table_group_id")
    @ManyToOne
    private TableGroup tableGroup;

    private NumberOfGuests numberOfGuests;

    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public void changeEmpty(boolean empty) {
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        verifyChangeable();
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public void group(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void ungroup() {
        this.tableGroup = null;
    }

    private void verifyChangeable() {
        if (isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
