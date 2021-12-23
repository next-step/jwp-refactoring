package kitchenpos.ordertable.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.exception.CommonErrorCode;
import kitchenpos.common.exception.InvalidParameterException;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

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

    public void changeEmpty(TableValidator tableValidator, boolean empty) {
        validTableGroupNotInclude();
        tableValidator.completedOrderValid(this);

        this.empty.changeEmpty(empty);
    }

    public void changeTableGroup(Long tableGroupId) {
        validTableGroupNotInclude();
        empty.validNotEmpty();
        this.tableGroupId = tableGroupId;
        this.empty.changeEmpty(false);
    }

    public void ungroup(TableValidator tableValidator) {
        tableValidator.completedOrderValid(this);

        tableGroupId = null;
    }

    public boolean isEmpty() {
        return empty.isEmpty();
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        if (Objects.isNull(tableGroupId)) {
            return null;
        }
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.value();
    }


    private void validTableGroupNotInclude() {
        if (Objects.nonNull(tableGroupId)) {
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
