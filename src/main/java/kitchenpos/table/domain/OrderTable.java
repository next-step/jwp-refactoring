package kitchenpos.table.domain;

import kitchenpos.order.exception.NotChangeToEmptyThatGroupTable;
import kitchenpos.table.exception.NotChangeNumberOfGuestThatEmptyTable;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(Long id, int numberOfGuests) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = false;
    }

    public OrderTable(int numberOfGuests) {
        this(null, numberOfGuests);
    }

    public Long getId() {
        return id;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeToEmpty() {
        if(tableGroup != null){
            throw new NotChangeToEmptyThatGroupTable();
        }
        this.empty = true;
    }

    public void changeTableGroupId(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
        if (empty) {
            throw new NotChangeNumberOfGuestThatEmptyTable();
        }
        this.numberOfGuests = numberOfGuests;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmptyTableAndNotExistTableGroupId() {
        return this.empty || this.tableGroup == null;
    }

    public void registerTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void ungroup() {
        this.tableGroup = null;
        this.empty = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTable that = (OrderTable) o;
        return numberOfGuests == that.numberOfGuests && empty == that.empty && Objects.equals(id, that.id) && Objects.equals(tableGroup, that.tableGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableGroup, numberOfGuests, empty);
    }
}
