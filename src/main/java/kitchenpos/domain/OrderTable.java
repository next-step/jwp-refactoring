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
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTableGroup(TableGroup tableGroup) {
        if (this.tableGroup != null) {
            this.tableGroup.getOrderTables().remove(this);
        }
        this.tableGroup = tableGroup;
        tableGroup.getOrderTables().add(this);
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
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
}
