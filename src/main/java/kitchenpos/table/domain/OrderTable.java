package kitchenpos.table.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, numberOfGuests, empty);
    }

    public OrderTable(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
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

    public void changeNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(final boolean empty) {
        this.empty = empty;
    }

    public OrderTable group(Long tableGroupId) {
        if (!isEmpty() || this.tableGroupId != null) {
            throw new IllegalArgumentException("단체 지정하려는 테이블 중 비어있지 않거나 이미 단체 지정된 테이블이 있습니다.");
        }
        this.tableGroupId = tableGroupId;
        this.empty = false;
        return this;
    }

    public void ungroup() {
        this.tableGroupId = null;
    }

    public OrderTable isNotEmpty() {
        if (this.empty) {
            throw new IllegalArgumentException("주문 테이블이 비어있습니다.");
        }
        return this;
    }

    public OrderTable isNotGroup() {
        if (Objects.nonNull(this.tableGroupId)) {
            throw new IllegalArgumentException("단체로 지정된 테이블입니다.");
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTable that = (OrderTable) o;
        return numberOfGuests == that.numberOfGuests && empty == that.empty && Objects.equals(id, that.id) && Objects.equals(tableGroupId, that.tableGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableGroupId, numberOfGuests, empty);
    }
}
