package kitchenpos.table.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "order_table")
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
    private EmptyStatus empty;

    protected OrderTable() {
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this(tableGroup, numberOfGuests, empty);
        this.id = id;
    }

    public OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this(numberOfGuests, empty);
        this.tableGroup = tableGroup;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = new EmptyStatus(empty);
    }

    public static OrderTable ofEmptyTable() {
        return new OrderTable(0, true);
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void assignTableGroup(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumber();
    }

    public void changeNumberOfGuests(final NumberOfGuests numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty.getStatus();
    }

    public void changeEmpty(final boolean empty) {
        this.empty = new EmptyStatus(empty);
    }

    public boolean isNotNullTableGroup() {
        return !Objects.isNull(tableGroup);
    }

    public void initTableGroup(TableGroup tableGroup) {
        assignTableGroup(tableGroup);
        changeEmpty(false);
    }

    public void unTableGroup() {
        tableGroup = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTable that = (OrderTable) o;
        return Objects.equals(id, that.id)
                && Objects.equals(tableGroup, that.tableGroup)
                && Objects.equals(numberOfGuests, that.numberOfGuests)
                && Objects.equals(empty, that.empty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableGroup, numberOfGuests, empty);
    }
}
