package kitchenpos.table.domain;

import kitchenpos.common.domain.BaseEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private Long tableGroupId;

    private int numberOfGuests;

    private boolean empty;

    protected OrderTable() {}

    private OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(null, null, numberOfGuests, empty);
    }

    public void addGroup(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void removeGroup() {
        this.tableGroupId = null;
    }

    public void changeEmpty(OrderTableValidator orderTableValidator, boolean empty) {
        orderTableValidator.validateChangeEmpty(this);
        this.empty = empty;
    }

    public void changeNumberOfGuests(OrderTableValidator orderTableValidator, int numberOfGuests) {
        orderTableValidator.validateChangeNumberOfGuests(this, numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isGrouping() {
        return Objects.nonNull(tableGroupId);
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTable that = (OrderTable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OrderTable{" +
                "id=" + id +
                ", tableGroupId=" + tableGroupId +
                ", numberOfGuests=" + numberOfGuests +
                ", empty=" + empty +
                '}';
    }
}
