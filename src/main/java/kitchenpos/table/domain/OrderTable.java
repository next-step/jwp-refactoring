package kitchenpos.table.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;
    @Embedded
    private NumberOfGuests numberOfGuests;
    @Embedded
    private Empty empty;

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, null, new NumberOfGuests(numberOfGuests), new Empty(empty));
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this(id, tableGroup, new NumberOfGuests(numberOfGuests), new Empty(empty));
    }

    public OrderTable(Long id, TableGroup tableGroup, NumberOfGuests numberOfGuests, Empty empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public void validate() {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException();
        }
    }

    public void validateOrderTableEmpty() {
        if (isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
    public void changeNumberOfGuests(int numberOfGuests) {
        changeNumberOfGuests(new NumberOfGuests(numberOfGuests));
    }

    public void changeNumberOfGuests(NumberOfGuests numberOfGuests) {
        if (numberOfGuests.lessThenZero()) {
            throw new IllegalArgumentException();
        }
        this.numberOfGuests = numberOfGuests;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty.value();
    }

    public Empty getEmpty() {
        return empty;
    }

    public Long getTableGroupId() {
        if (tableGroup == null) {
            return null;
        }
        return tableGroup.getId();
    }

    public void changeEmpty(boolean empty) {
        changeEmpty(new Empty(empty));
    }

    public void changeEmpty(Empty empty) {
        this.empty = empty;
    }

    public void validateEmptyAndTableGroup() {
        if (!empty.value() || Objects.nonNull(getTableGroup())) {
            throw new IllegalArgumentException();
        }
    }

    public void changeTableGroupIdAndEmpty(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }
}
