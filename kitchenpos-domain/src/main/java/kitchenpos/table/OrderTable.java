package kitchenpos.table;

import kitchenpos.table.exception.InvalidChangeException;
import kitchenpos.table.exception.NegativeNumberOfGuestsException;
import kitchenpos.tablegroup.TableGroup;
import kitchenpos.tablegroup.exception.InvalidGroupException;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
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
        validateGroup();

        this.tableGroupId = tableGroup.getId();
        this.empty = false;
    }

    private void validateGroup() {
        if (!empty || Objects.nonNull(tableGroupId)) {
            throw new InvalidGroupException("빈 테이블이 아니거나 단체 지정일 경우 단체 지정을 할 수 없습니다.");
        }
    }

    public void unGroup() {
        this.tableGroupId = null;
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
