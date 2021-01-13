package kitchenpos.order.domain;

import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tableGroupId;
    @Embedded
    private NumberOfGuests numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.value();
    }

    public boolean isEmpty() {
        return empty;
    }

    public void updateTableGroup(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void updateEmpty(boolean empty) {
        checkTableGroup();
        this.empty = empty;
    }

    public void updateNumberOfGuests(int numberOfGuests) {
        checkIsNotEmpty();
        this.numberOfGuests.update(numberOfGuests);
    }

    private void checkIsNotEmpty() {
        if (this.empty) {
            throw new IllegalArgumentException("빈 테이블의 손님 수는 변경할 수 없습니다.");
        }
    }

    private void checkTableGroup() {
        if (Objects.nonNull(this.tableGroupId)) {
            throw new IllegalArgumentException("단체지정된 테이블의 공석여부는 변경할 수 없습니다.");
        }
    }
}
