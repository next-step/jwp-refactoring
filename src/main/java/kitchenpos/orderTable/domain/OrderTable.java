package kitchenpos.orderTable.domain;

import kitchenpos.common.domain.BaseEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tableGroupId;
    @Embedded
    private NumberOfGuests numberOfGuests;
    private boolean empty;

    protected OrderTable() {
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

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public static OrderTable createOrderTable(final int numberOfGuests, final boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public void changeEmpty() {
        if (Objects.nonNull(this.tableGroupId)) {
            throw new IllegalArgumentException("단체 지정되어 빈 상태로 변경할 수 없습니다.");
        }
        this.numberOfGuests = new NumberOfGuests(0);
        this.empty = true;
    }

    public void changeNumberOfGuests(final NumberOfGuests numberOfGuests) {
        if (isEmpty()) {
            throw new IllegalArgumentException("빈 테이블은 손님의 수를 변경할 수 없습니다.");
        }

        if (numberOfGuests.isZero()) {
            changeEmpty();
        }

        this.numberOfGuests = numberOfGuests;
    }

    public void ungroup() {
        this.tableGroupId = null;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getValue();
    }

    public boolean isEmpty() {
        return empty;
    }
}
