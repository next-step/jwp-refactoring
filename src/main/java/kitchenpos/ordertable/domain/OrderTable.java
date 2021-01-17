package kitchenpos.ordertable.domain;

import kitchenpos.order.domain.Order;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {

    private static final int MIN_NUMBER_OF_GUESTS = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    @Column(name = "number_of_guests")
    private int numberOfGuests;

    @Column(name = "empty")
    private boolean empty;

    @Embedded
    private Orders orders;

    protected OrderTable() {
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, numberOfGuests, empty);
    }

    public OrderTable(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.orders = new Orders();
    }

    public void changeEmpty(final boolean empty) {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalStateException("단체 지정이 되어 있다면 등록 상태를 변경할 수 없습니다.");
        }

        if (orders.hasNotComplete()) {
            throw new IllegalStateException("주문이 완료되지 않았다면 상태를 변경할 수 없습니다.");
        }
        this.empty = empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (empty) {
            throw new IllegalStateException("빈 테이블은 손님 수를 변경할 수 없습니다.");
        }

        if (numberOfGuests < MIN_NUMBER_OF_GUESTS) {
            throw new IllegalArgumentException("손님 수는 0명 이상이어야 합니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void assign(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void ungroup() {
        if (orders.hasNotComplete()) {
            throw new IllegalArgumentException("주문이 완료되지 않은 테이블은 단체 지정을 삭제할 수 없다.");
        }
        this.tableGroupId = null;
    }

    public void add(final Order order) {
        orders.add(order);
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
