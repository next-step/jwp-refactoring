package kitchenpos.domain.table;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class OrderTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

    private int numberOfGuests;

    private boolean empty;

    // for jpa
    public OrderTable() {
    }

    private OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(null, null, numberOfGuests, empty);
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmptyTable() {
        this.empty = true;
    }

    public void changeNonEmptyTable() {
        this.empty = false;
    }

    public void changeNumberOfGuest(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("numberOfGuests is under zero");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void changeTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
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

    @Override
    public String toString() {
        return "OrderTable{" +
                "id=" + id +
                ", tableGroup=" + tableGroup +
                ", numberOfGuests=" + numberOfGuests +
                ", empty=" + empty +
                '}';
    }

    public boolean isTableGroupEmpty() {
        return this.tableGroup == null;
    }
}
