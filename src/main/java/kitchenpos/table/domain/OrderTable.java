package kitchenpos.table.domain;

import kitchenpos.table.exception.InvalidChangeException;
import kitchenpos.table.exception.NegativeNumberOfGuestsException;
import kitchenpos.tablegroup.domain.TableGroup;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tableGroupId;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    private OrderTable(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        validate();
    }

    private void validate() {
        if (numberOfGuests < 0) {
            throw new NegativeNumberOfGuestsException("방문한 손님 수는 0명 이상이어야 합니다.");
        }
    }

    public static OrderTable of(Long id, int numberOfGuests, boolean empty) {
        return new OrderTable(id, numberOfGuests, empty);
    }

    public void group(final TableGroup tableGroup) {
        checkUnGroup();

//        this.tableGroup = tableGroup;
//        this.empty = false;
    }

    private void checkUnGroup() {
//        if (!empty || Objects.nonNull(tableGroupId)) {
//            throw new InvalidGroupException("빈 테이블이거나 이미 단체 지정인 경우 단체 지정할 수 없다.");
//        }
        if (Objects.nonNull(tableGroupId)) {
            throw new InvalidChangeException("빈 테이블이거나 이미 단체 지정인 경우 단체 지정할 수 없다.");
        }
    }

    public void checkIsGroup() {
        if (Objects.nonNull(tableGroupId)) {
            throw new InvalidChangeException("단체 지정 테이블입니다.");
        }
    }

    public void checkIsEmpty() {
        if (empty) {
            throw new InvalidChangeException("빈 테이블입니다.");
        }
    }

    public void changeEmpty(boolean empty) {
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
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

    public boolean isEmpty() {
        return empty;
    }
}
