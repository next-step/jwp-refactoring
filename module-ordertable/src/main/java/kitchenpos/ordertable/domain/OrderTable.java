package kitchenpos.ordertable.domain;

import javax.persistence.Column;
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

    @Column(name = "table_group_id")
    private Long tableGroupId;

    @Embedded
    @Column(precision = 11, nullable = false)
    private NumberOfGuests numberOfGuests;

    @Embedded
    @Column(nullable = false)
    private OrderTableEmpty empty;

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = new OrderTableEmpty(empty);
    }

    public void changeEmpty(final boolean empty) {
        this.empty.changeEmpty(empty);
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateEmpty();
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public void changeTableGroupId(Long tableGroupId) {
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
        return empty.isEmpty();
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

    private void validateEmpty() {
        if (isEmpty()) {
            throw new IllegalArgumentException("빈 주문 테이블 입니다[" + this + "]");
        }
    }
}
