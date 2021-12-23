package kitchenpos.ordertable.domain;

import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.exception.CommonErrorCode;
import kitchenpos.common.exception.InvalidParameterException;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.order.domain.Orders;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Embedded
    private Empty empty;

    protected OrderTable() {
    }

    private OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
        this.empty = Empty.of(empty);
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public void changeNumberOfGuests(int changeNumberOfGuests) {
        empty.validNotEmpty();
        this.numberOfGuests = numberOfGuests.changeNumberOfGuests(changeNumberOfGuests);
    }

    public boolean isEmpty() {
        return empty.isEmpty();
    }

    public void changeEmpty(Orders orders, boolean empty) {
        validTableGroupNotInclude();

        if (!orders.isOrdersAllCompleted()) {
            throw new InvalidParameterException(
                CommonErrorCode.ORDER_TABLE_CHANGE_EMPTY_NOT_COMPLETE_EXCEPTION);
        }

        this.empty.changeEmpty(empty);
    }

    public void changeTableGroup(TableGroup tableGroup) {
        validTableGroupNotInclude();
        empty.validNotEmpty();
        this.tableGroup = tableGroup;
        this.empty.changeEmpty(false);
    }

    public void ungroup(Orders orders) {
        if (!orders.isOrdersAllCompleted()) {
            throw new InvalidParameterException(
                CommonErrorCode.ORDER_TABLE_UNGROUP_NOT_COMPLETE_EXCEPTION);
        }
        tableGroup = null;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        if (Objects.isNull(tableGroup)) {
            return null;
        }
        return tableGroup.getId();
    }

    public int getNumberOfGuests() {
        return numberOfGuests.value();
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    private void validTableGroupNotInclude() {
        if (Objects.nonNull(tableGroup)) {
            throw new InvalidParameterException(
                CommonErrorCode.ORDER_TABLE_EXISTS_TABLE_GROUP_EXCEPTION);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (Objects.isNull(id)) {
            return false;
        }

        OrderTable that = (OrderTable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
