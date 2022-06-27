package kitchenpos.table.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.domain.NumberOfGuests;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "table_group_id")
    private Long tableGroupId;
    @Column(name = "number_of_guests", nullable = false)
    private NumberOfGuests numberOfGuests;
    @Column(name = "empty", nullable = false)
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public boolean hasTableGroup() {
        return Objects.nonNull(tableGroupId);
    }

    public void ungroup() {
        this.tableGroupId = null;
    }

    public void changeEmpty(final boolean empty) {
        if (hasTableGroup()) {
            throw new IllegalArgumentException("그룹에 속해있는 테이블은 빈 상태로 변경할 수 없습니다.");
        }

        this.empty = empty;
    }

    public void updateNumberOfGuests(int numberOfGuests) {
        if (empty) {
            throw new IllegalArgumentException("비어있는 테이블은 손님이 존재할 수 없습니다.");
        }

        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public void changeTableGroup(long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
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
