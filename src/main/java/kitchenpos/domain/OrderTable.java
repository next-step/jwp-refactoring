package kitchenpos.domain;

import javax.persistence.*;
import java.util.Objects;

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

    protected OrderTable() {
    }

    public OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public void group(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void ungroup() {
        this.tableGroup = null;
    }

    public Long getId() {
        return id;
    }

    public void changeEmpty(boolean empty) {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException();
        }
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (empty) {
            throw new IllegalArgumentException();
        }
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public void checkNonEmptyInGroup() {
        if (!empty || Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException();
        }
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumber();
    }

    public boolean isEmpty() {
        return empty;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void checkEmpty() {
        if (empty) {
            throw new IllegalArgumentException();
        }
    }
}
