package kitchenpos.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;
    @Embedded
    private NumberOfGuests numberOfGuests = new NumberOfGuests();
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(Long id) {
        this.id = id;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public OrderTable(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public void validateEmpty() {
        if (empty) {
            throw new IllegalArgumentException("빈 테이블은 주문을 할 수 없습니다.");
        }
    }

    public void validateCanGroup() {
        if (!empty || Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("빈 테이블이 아니거나 이미 단체가 지정되었습니다.");
        }
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

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (empty) {
            throw new IllegalArgumentException("빈 테이블이면 방문한 손님 수를 변경할 수 없습니다.");
        }
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(final boolean empty) {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("단체 지정이 되어 있는 테이블은 이용 여부를 변경할 수 없습니다.");
        }
        this.empty = empty;
    }

    public void group(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void unGroup() {
        tableGroup = null;
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
}
