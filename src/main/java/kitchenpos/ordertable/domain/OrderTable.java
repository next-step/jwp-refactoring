package kitchenpos.ordertable.domain;

import java.util.Objects;

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
import javax.persistence.Table;

import kitchenpos.ordertable.exception.InvalidOrderTableEmptyException;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.exception.InvalidTableGroupException;
import kitchenpos.tablegroup.exception.NotFoundTableGroupException;

@Entity
@Table(name = "order_table")
public class OrderTable {

    public static final String HAS_BEEN_GROUPED = "이미 단체 지정이 된 주문 테이블입니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Column(name = "empty", nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    private OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
        this.empty = empty;
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(null, null, numberOfGuests, empty);
    }

    public static OrderTable of(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return new OrderTable(id, tableGroup, numberOfGuests, empty);
    }

    private boolean hasBeenGrouped() {
        return !empty || Objects.nonNull(tableGroup);
    }

    public void group(TableGroup tableGroup) {
        if (Objects.isNull(tableGroup)) {
            throw new NotFoundTableGroupException();
        }
        if (hasBeenGrouped()) {
            throw new InvalidTableGroupException(HAS_BEEN_GROUPED);
        }
        this.empty = false;
        this.tableGroup = tableGroup;
    }

    public void ungroup() {
        this.tableGroup = null;
    }

    public void changeEmptyIfNotTableGroup(boolean empty) {
        if (Objects.nonNull(tableGroup)) {
            throw new InvalidOrderTableEmptyException("주문 테이블이 단체 지정된 경우 수정할 수 없습니다.");
        }
        this.empty = empty;
    }

    public void changeNumberOfGuestsIfNotEmpty(int numberOfGuests) {
        if (empty) {
            throw new IllegalArgumentException("주문 테이블이 비어있으면 손님 수를 수정할 수 없습니다.");
        }
        this.numberOfGuests.changeNumberOfGuests(numberOfGuests);
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }

    public boolean isEmpty() {
        return empty;
    }
}
