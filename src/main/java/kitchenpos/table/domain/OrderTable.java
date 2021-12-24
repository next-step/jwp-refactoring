package kitchenpos.table.domain;

import kitchenpos.common.exception.OrderTableEmptyException;
import kitchenpos.tablegroup.domain.TableGroup;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private TableGroup tableGroup;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Embedded
    private Empty empty;

    protected OrderTable() {
    }

    private OrderTable(Long id, Empty empty) {
        this.id = id;
        this.empty = empty;
    }

    private OrderTable(TableGroup tableGroup, NumberOfGuests numberOfGuests, Empty empty) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(Long id, TableGroup tableGroup, NumberOfGuests numberOfGuests, Empty empty) {
        this(tableGroup, numberOfGuests, empty);
        this.id = id;
    }

    public static OrderTable of(Long id, Empty empty) {
        return new OrderTable(id, empty);
    }

    public static OrderTable of(TableGroup tableGroup, NumberOfGuests numberOfGuests, Empty empty) {
        return new OrderTable(tableGroup, numberOfGuests, empty);
    }

    public static OrderTable of(Long id, TableGroup tableGroup, NumberOfGuests numberOfGuests, Empty empty) {
        return new OrderTable(id, tableGroup, numberOfGuests, empty);
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void setTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public Long getTableGroupId() {
        if (tableGroup == null) {
            return null;
        }
        return tableGroup.getId();
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public Empty getEmpty() {
        return empty;
    }

    public void setEmpty(Empty empty) {
        this.empty = empty;
    }

    public void changeEmpty(Empty empty) {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("이미 속해있는 테이블 그룹이 있습니다.");
        }
        this.empty = empty;
    }

    public void changeNumberOfGuests(NumberOfGuests numberOfGuests) {
        if (empty.isEmpty()) {
            throw new OrderTableEmptyException();
        }
        this.numberOfGuests = numberOfGuests;
    }
}
