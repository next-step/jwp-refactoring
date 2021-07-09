package kitchenpos.domain;

import javax.persistence.*;
import java.util.Objects;

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

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this(numberOfGuests, empty);
        this.id = id;
        this.tableGroup = tableGroup;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public void changeEmpty(boolean empty) {
        verifyChangeableEmpty();
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        verifyChangeable();
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public void changeTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    private void verifyChangeable() {
        if (isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void verifyChangeableEmpty() {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("단체지정이 되어있으면 안됩니다.");
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

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }
}
