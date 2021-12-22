package kitchenpos.domain;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;
    private int numberOfGuests;
    @Embedded
    private TableState tableState;

    public OrderTable() {
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, TableState tableState) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.tableState = tableState;
    }

    public OrderTable(Long id, int numberOfGuests, TableState tableState) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.tableState = tableState;
    }

    public OrderTable(int numberOfGuests, TableState tableState) {
        this.numberOfGuests = numberOfGuests;
        this.tableState = tableState;
    }

    public void setTableGroup(TableGroup tableGroup) {
        if (this.tableGroup != null) {
            this.tableGroup.getOrderTables().remove(this);
        }
        this.tableGroup = tableGroup;
        tableGroup.getOrderTables().add(this);
    }

    public void changeEmpty() {
        this.tableState = new TableState(true);
    }

    public void changeSit() {
        this.tableState = new TableState(false);
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
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

    public TableState getTableState() {
        return tableState;
    }

    public boolean isEmpty() {
        return tableState.isEmpty();
    }
}
