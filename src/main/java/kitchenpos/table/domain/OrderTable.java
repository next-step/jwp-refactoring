package kitchenpos.table.domain;

import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"))
    private TableGroup tableGroup;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Column
    private Empty empty;

    protected OrderTable() {
    }

    private OrderTable(Long id, TableGroup tableGroup, NumberOfGuests numberOfGuests, Empty empty) {
        Assert.notNull(numberOfGuests, "고객 수는 항상 존재해야 합니다.");
        Assert.notNull(empty, "테이블 상태는 항상 존재해야 합니다.");

        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(Long id, int numberOfGuests, boolean empty) {
        return new OrderTable(id, null, NumberOfGuests.of(numberOfGuests), Empty.of(empty));
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(null, null, NumberOfGuests.of(numberOfGuests), Empty.of(empty));
    }

    public void checkAvailableGrouping() {
        if (!this.empty.isEmpty() || Objects.nonNull(this.tableGroup)) {
            throw new IllegalArgumentException("그룹으로 묶을 수 없는 테이블 입니다.");
        }
    }

    public void grouping(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        this.empty = Empty.of(false);
    }

    public void ungroup() {
        this.tableGroup = null;
    }

    public void updateEmpty(boolean empty) {
        checkTableGroupExist();

        this.empty = Empty.of(empty);
    }

    public void updateNumberOfGuest(int numberOfGuests) {
        checkTableIsEmpty();

        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
    }

    private void checkTableGroupExist() {
        if (tableGroup != null) {
            throw new IllegalArgumentException();
        }
    }

    private void checkTableIsEmpty() {
        if (this.empty.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        if (tableGroup == null) {
            return null;
        }

        return tableGroup.getId();
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }

    public boolean isEmpty() {
        return this.empty.isEmpty();
    }
}
