package kitchenpos.ordertable.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.domain.NumberOfGuests;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long tableGroupId;
    @Embedded
    private NumberOfGuests numberOfGuests;
    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    private OrderTable(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
        this.empty = empty;
    }

    private OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
        this.empty = empty;
    }

    public static OrderTable from(Long id, int numberOfGuests, boolean empty) {
        return new OrderTable(id, numberOfGuests, empty);
    }

    public static OrderTable from(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public void reserve(Long tableGroupId) {
        validateTableGroupNull();
        validateEmpty();
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    private void validateEmpty() {
        if (!empty) {
            throw new IllegalArgumentException("주문테이블이 비어있어야 합니다.");
        }
    }

    public void changeEmpty(final boolean empty) {
        validateTableGroupNull();
        this.empty = empty;
    }


    private void validateTableGroupNull() {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException("단체지정이 없어야 합니다.");
        }
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateNotEmpty();
        this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
    }

    private void validateNotEmpty() {
        if (empty) {
            throw new IllegalArgumentException("주문테이블이 비어있으면 안됩니다.");
        }
    }

    public void unGroup() {
        this.tableGroupId = null;
    }

    public Long id() {
        return id;
    }

    public NumberOfGuests numberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
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
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
