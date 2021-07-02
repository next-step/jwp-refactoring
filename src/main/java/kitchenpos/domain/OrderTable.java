package kitchenpos.domain;

import static java.util.Objects.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderTable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    private int numberOfGuests;

    private boolean empty;

    protected OrderTable() {}

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public void ungroup() {
        this.tableGroup = null;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isNotEmpty() {
        return !empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    TableGroup getTableGroup() {
        return tableGroup;
    }

    void setTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public boolean isGrouped() {
        return nonNull(tableGroup);
    }

    public Long getTableId() {
        return requireNonNull(tableGroup).getId();
    }
}
