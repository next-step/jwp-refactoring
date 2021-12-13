package kitchenpos.table.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kitchenpos.tablegroup.domain.TableGroup;

@Entity
@Table(name = "order_table")
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

    @Column(name = "number_of_guests", nullable = false)
    private int numberOfGuests;

    @Column(name = "empty", nullable = false)
    private boolean empty;

    protected OrderTable() {}

    private OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this(tableGroup, numberOfGuests, empty);
        this.id = id;
    }

    private OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this(numberOfGuests, empty);
        this.tableGroup = tableGroup;
    }

    private OrderTable(Long id) {
        this.id = id;
    }

    private OrderTable(Long id, int numberOfGuests, boolean empty) {
        this(numberOfGuests, empty);
        this.id = id;
    }

    private OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable from(Long id) {
        return new OrderTable(id);
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public static OrderTable of (Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return new OrderTable(id, tableGroup, numberOfGuests, empty);
    }

    public static OrderTable of (TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return new OrderTable(tableGroup, numberOfGuests, empty);
    }

    public static OrderTable of(Long id, int numberOfGuests, boolean empty) {
        return new OrderTable(id, numberOfGuests, empty);
    }

    public void ungroup() {
        this.tableGroup = null;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void setTableGroupId(final Long tableGroupId) {
        this.tableGroup = TableGroup.from(tableGroupId);
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
