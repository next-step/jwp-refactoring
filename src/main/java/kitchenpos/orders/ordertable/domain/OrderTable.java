package kitchenpos.orders.ordertable.domain;

import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.domain.Validator;

@Entity
public class OrderTable {

    private static final long EMPTY_ORDER_TABLE_ID = 0L;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "table_group_id",
        foreignKey = @ForeignKey(name = "fk_order_table_to_table_group")
    )
    private TableGroup tableGroup;

    public OrderTable() {
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

    public void serve() {
        if (!isEmpty()) {
            throw new IllegalStateException("방문한 손님이 없는 주문 테이블만 사용할 수 있습니다.");
        }
        this.empty = new Empty(TAKE);
    }

    public void serve(final NumberOfGuests numberOfGuests) {
        serve();
        changeNumberOfGuests(numberOfGuests);
    }

    public void clear(final Validator<OrderTable> validator) {
        validator.validate(this);
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
        if (Objects.nonNull(tableGroup)) {
            return tableGroup.getId();
        }
        return EMPTY_ORDER_TABLE_ID;
    }

    public void group(final TableGroup tableGroup) {
        if (Objects.nonNull(this.tableGroup)) {
            throw new IllegalStateException("이미 단체 지정된 테이블을 단체 지정할 수 없습니다.");
        }
        this.tableGroup = tableGroup;
    }

    public void ungroup() {
        tableGroup = null;
    }
}
