package kitchenpos.table.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private TableGroup tableGroup;

    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {}

    public OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable create(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return new OrderTable(tableGroup, numberOfGuests, empty);
    }

    public void update(TableGroup tableGroup, boolean empty) {
        this.tableGroup = tableGroup;
        this.empty = empty;
    }

    public void validateGrouped() {
        if (Objects.nonNull(tableGroup.getId())) {
            throw new IllegalArgumentException("테이블 그룹은 항상 존재해야 합니다.");
        }
    }

    public void validateEmpty() {
        if (empty) {
            throw new IllegalArgumentException("주문 테이블은 비어있으면 안됩니다.");
        }
    }

    public void removeTableGroup() {
        this.tableGroup = null;
    }

    public void updateNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void updateEmpty(final boolean empty) {
        validateGrouped();
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroup.getId();
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

}
