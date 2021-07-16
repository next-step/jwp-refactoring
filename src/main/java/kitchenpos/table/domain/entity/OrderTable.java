package kitchenpos.table.domain.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.table.domain.value.NumberOfGuests;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @Embedded
    private NumberOfGuests numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(Long id, TableGroup tableGroup, NumberOfGuests numberOfGuests,
        boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(Long id, NumberOfGuests numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(NumberOfGuests numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
        this.empty = false;
    }

    public OrderTable(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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

    public void toTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void unGroup() {
        this.tableGroup = null;
    }

    public void changeEmpty(boolean empty) {
        this.empty = empty;
    }

    public void changeNumberOfGuests(NumberOfGuests numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    @Override
    public String toString() {
        return "OrderTable{" +
            "id=" + id +
            ", tableGroup=" + tableGroup +
            ", numberOfGuests=" + numberOfGuests +
            ", empty=" + empty +
            '}';
    }
}
