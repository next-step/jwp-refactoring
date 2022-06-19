package kitchenpos.table.domain;

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
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

    @Column(name = "number_of_guests", nullable = false)
    private int numberOfGuests;

    @Column(name = "empty", nullable = false)
    private boolean empty;

    protected OrderTable() {}

    private OrderTable(boolean empty, int numberOfGuests) {
        this.empty = empty;
        this.numberOfGuests = numberOfGuests;
    }

    private OrderTable(boolean empty) {
        this.empty = empty;
    }

    private OrderTable(Long orderTableId) {
        this.id = orderTableId;
    }

    public static OrderTable of(boolean empty) {
        return new OrderTable(empty);
    }

    public static OrderTable of(boolean empty, int numberOfGuests) {
        return new OrderTable(empty, numberOfGuests);
    }

    public static OrderTable from(Long orderTableId) {
        return new OrderTable(orderTableId);
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return this.tableGroup;
    }
    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void mappedByTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void changeEmpty(boolean empty) {
        this.empty = empty;
    }

    public void removeTableGroupId() {
        this.tableGroup = null;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Long getTableGroupId() {
        if (this.tableGroup == null) {
            return null;
        }
        return this.tableGroup.getId();
    }
}
