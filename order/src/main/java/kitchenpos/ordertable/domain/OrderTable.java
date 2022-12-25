package kitchenpos.ordertable.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.order.domain.NumberOfGuest;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tableGroupId;
    @Embedded
    private NumberOfGuest numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = new NumberOfGuest(numberOfGuests);
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void setTableGroupId(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void ungroup() {
        this.tableGroupId = null;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.numberOfGuest();
    }

    public boolean isEmpty() {
        return empty;
    }

    public void occupied() {
        this.empty = false;
    }

    public void changeEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void changeNumberOfGuest(int numberOfGuests) {
        if (empty) {
            throw new IllegalArgumentException("빈 테이블의 손님 수를 변경할 수 없습니다.");
        }
        this.numberOfGuests = new NumberOfGuest(numberOfGuests);
    }

    public boolean ableToGroup() {
        return empty && tableGroupId == null;
    }
}
