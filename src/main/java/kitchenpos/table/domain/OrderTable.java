package kitchenpos.table.domain;

import kitchenpos.tablegroup.domain.TableGroup;

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

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    private OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return new OrderTable(tableGroup, numberOfGuests, empty);
    }

    public void changeGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void changeEmpty() {
        this.empty = true;
    }

    public void changeNumberOfGuests(int changeNumber) {
        validEmpty();
        validCheckSize(changeNumber);
        this.numberOfGuests = changeNumber;
    }

    public void validEmpty() {
        if (isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 존재하지 않습니다.");
        }
    }

    private void validCheckSize(int changeNumber) {
        if (changeNumber < 0) {
            throw new IllegalArgumentException("0명보다 작을 수 없습니다.");
        }
    }

    public void changeUnGroup() {
        this.tableGroup = null;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        if (tableGroup != null) {
            return tableGroup.getId();
        }

        return null;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isGroup() {
        return Objects.nonNull(tableGroup);
    }
}
