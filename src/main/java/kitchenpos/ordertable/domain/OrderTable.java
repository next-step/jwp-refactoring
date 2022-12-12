package kitchenpos.ordertable.domain;

import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.constant.ErrorCode;
import kitchenpos.common.domain.Empty;
import kitchenpos.common.domain.NumberOfGuests;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tableGroupId;
    @Embedded
    private NumberOfGuests numberOfGuests;
    @Embedded
    private Empty empty;

    protected OrderTable() {}

    private OrderTable(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
        this.empty = Empty.valueOf(empty);
    }

    public static OrderTable of(Long id, int numberOfGuests, boolean empty) {
        return new OrderTable(id, numberOfGuests, empty);
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(null, numberOfGuests, empty);
    }

    public void ungroup() {
        this.tableGroupId = null;
    }

    public boolean isEmpty() {
        return empty.isEmpty();
    }

    public boolean hasTableGroup() {
        return tableGroupId != null;
    }

    public void registerTableGroup(Long tableGroupId) {
        validateHasTableGroup();
        this.tableGroupId = tableGroupId;
        this.empty = Empty.IS_NOT_EMPTY;
    }

    public void changeEmpty(boolean isEmpty) {
        validateHasTableGroup();
        this.empty = Empty.valueOf(isEmpty);
    }

    private void validateHasTableGroup() {
        if(hasTableGroup()) {
            throw new IllegalArgumentException(ErrorCode.단체_그룹_지정되어_있음.getErrorMessage());
        }
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validateOrderTableIsNotEmpty();
        this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
    }

    private void validateOrderTableIsNotEmpty() {
        if(empty.isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.비어있는_주문_테이블.getErrorMessage());
        }
    }

    public Long findTableGroupId() {
        if(tableGroupId == null) {
            return null;
        }
        return tableGroupId;
    }

    public Long getId() {
        return id;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public Empty getEmpty() {
        return empty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderTable that = (OrderTable) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
