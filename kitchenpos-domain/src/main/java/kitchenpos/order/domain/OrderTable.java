package kitchenpos.order.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="table_group_id")
    private Long tableGroupId;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        validateLessThanZero(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    private void validateLessThanZero(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님의 수가 0보다 작을 수 없습니다.");
        }
    }


    public void changeEmpty(boolean empty) {
        validateIsTableGroup();
        this.empty = empty;
    }

    private void validateIsTableGroup() {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException("단체지정이 되어있는 경우 빈 테이블 상태 변경 불가합니다.");
        }
    }



    public void changeTableGroupId(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validateIsEmpty();
        validateLessThanZero(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validateIsEmpty() {
        if (empty) {
            throw new IllegalArgumentException("빈테이블은 손님의 수 변경할 수 없습니다.");
        }
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
