package kitchenpos.domain.table;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name ="table_group_id")
    private TableGroup tableGroup;
    private int numberOfGuests;

    boolean empty;
 
    protected OrderTable() {
    }

    private OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(TableGroup tableGroup, int numberOfGuests) {
        if (numberOfGuests > 0) {
            return new OrderTable(null, tableGroup, numberOfGuests, false);
        }

        return new OrderTable(null, tableGroup, numberOfGuests, true);
    }

    public static OrderTable of(Long id, TableGroup tableGroup, int numberOfGuests) {
        if (numberOfGuests > 0) {
            return new OrderTable(id, tableGroup, numberOfGuests, false);
        }

        return new OrderTable(id, tableGroup, numberOfGuests, true);
    }

    public static OrderTable of(Long id, int numberOfGuests) {
        if (numberOfGuests > 0) {
            return new OrderTable(id, null, numberOfGuests, false);
        }

        return new OrderTable(id, null, numberOfGuests, true);
    }

    public static OrderTable of(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return new OrderTable(id, tableGroup, numberOfGuests, empty);
    }

    public static OrderTable of(Long id, int numberOfGuests, boolean empty) {
        return new OrderTable(id, null, numberOfGuests, empty);
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(null, null, numberOfGuests, empty);
    }

    public Long getId() {
        return this.id;
    }

    public TableGroup getTableGroup() {
        return this.tableGroup;
    }
    public int getNumberOfGuests() {
        return this.numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void unGroupTable() {
        this.tableGroup.getOrderTables().remove(this);
        this.tableGroup = null;
    }

    public void groupingTable(TableGroup tableGroup) {
        if (this.tableGroup != null) {
            this.tableGroup.getOrderTables().remove(this);
        }

        this.tableGroup = tableGroup;
        this.tableGroup.getOrderTables().add(this);
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean getEmpty() {
        return this.empty;
    }

    public void changeEmpty(boolean empty) {
        this.empty = empty;
    }

    public boolean hasTableGroup() {
        return tableGroup != null;
    }

}
