package kitchenpos.tobe.orders.domain.ordertable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderTable {

    private static final int MIN_NUMBER_OF_GUESTS = 0;
    private static final boolean TAKE = false;
    private static final boolean CLEAR = true;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Embedded
    private Empty empty;

    @Column(name = "table_group_id", nullable = false)
    private Long tableGroupId;

    protected OrderTable() {
    }

    public OrderTable(final Long id) {
        this.id = id;
        this.numberOfGuests = new NumberOfGuests(MIN_NUMBER_OF_GUESTS);
        this.empty = new Empty(CLEAR);
    }

    public void changeNumberOfGuests(final NumberOfGuests numberOfGuests) {
        if (isEmpty()) {
            throw new IllegalStateException("빈 테이블은 방문한 손님 수를 변경할 수 없습니다.");
        }
        if (numberOfGuests.asInt() < MIN_NUMBER_OF_GUESTS) {
            throw new IllegalArgumentException();
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void serve(final NumberOfGuests numberOfGuests) {
        if (!isEmpty()) {
            throw new IllegalStateException("방문한 손님이 없는 주문 테이블만 사용할 수 있습니다.");
        }
        this.empty = new Empty(TAKE);
        changeNumberOfGuests(numberOfGuests);
    }

    public void clear() {
        // TODO: 완료되지 않은 주문이 있는 주문 테이블은 빈 테이블로 설정할 수 없음
        if (isEmpty()) {
            throw new IllegalStateException();
        }
        this.numberOfGuests = new NumberOfGuests(MIN_NUMBER_OF_GUESTS);
        this.empty = new Empty(CLEAR);
    }

    public Long getId() {
        return id;
    }

    public boolean isEmpty() {
        return empty.isEmpty();
    }

    public int getNumberOfGuests() {
        return numberOfGuests.asInt();
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
