package kitchenpos.table.domain;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
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
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_table_group_of_order_table"))
    private TableGroup tableGroup;

    private int numberOfGuests;

    private boolean empty;

    public OrderTable() {
    }

    private OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        this.tableGroup = TableGroup.of(tableGroupId);
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = TableGroup.of(tableGroupId);
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(int numberOfGuests) {
        return new OrderTable(null, numberOfGuests, false);
    }

    public static OrderTable ofWithEmpty(int numberOfGuests) {
        return new OrderTable(null, numberOfGuests, true);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getTableGroupId() {
        return tableGroup.getId();
    }

    public void setTableGroupId(final Long tableGroupId) {
        this.tableGroup = TableGroup.of(tableGroupId);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }
}
