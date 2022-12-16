package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "order_table")
public class OrderTable {
    private static final String EXCEPTION_MESSAGE_NUMBER_OF_GUESTS_IS_NOT_NEGATIVE = "가격은 음수일 수 없습니다.";
    private static final String EXCEPTION_MESSAGE_IS_EMPTY_TABLE = "빈 테이블입니다. 요청하신 행위를 실행할 수 없습니다.";
    private static final String EXCEPTION_MESSAGE_IS_NOT_EMPTY_TABLE = "빈 테이블이 아닙니다. 요청하신 행위를 실행할 수 없습니다.";
    private static final String EXCEPTION_MESSAGE_ALREADY_IS_TABLE_GROUP = "속해있는 단체 테이블이 있으므로 빈 테이블이 될 수 없습니다.";
    private static final String EXCEPTION_MESSAGE_ALREADY_COOKING_OR_MEAL = "현재 속해있는 테이블의 주문이 요리중이거나, 식사중입니다.";
    private static final int ZERO = 0;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "bigint(20)")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "table_group_id", columnDefinition = "bigint(20)", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;
    @Column(nullable = false, columnDefinition = "int(11)")
    private int numberOfGuests;
    @Column(nullable = false, columnDefinition = "bit(1)")
    private boolean empty;
    @OneToOne(mappedBy = "orderTable", cascade = CascadeType.ALL)
    private Order order;

    public Order getOrder() {
        return order;
    }

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void ordered(Order order) {
        this.order = order;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isGrouping() {
        return Objects.nonNull(tableGroup);
    }

    public void enGroupBy(TableGroup tableGroup) {
        validateIsNotEmptyTable();

        changeEmpty(false);
        this.tableGroup = tableGroup;
    }

    public void unGroupBy() {
        if (isGrouping()) {
            this.tableGroup = null;
        }
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validateIsEmptyTable();
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public void changeEmpty(boolean empty) {
        validateOrderStatus();
        validateAlreadyGroup();
        this.empty = empty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderTable)) return false;
        OrderTable that = (OrderTable) o;
        return getNumberOfGuests() == that.getNumberOfGuests() && isEmpty() == that.isEmpty() && Objects.equals(getId(), that.getId()) && Objects.equals(getTableGroup(), that.getTableGroup());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTableGroup(), getNumberOfGuests(), isEmpty());
    }

    private void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < ZERO) {
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_NUMBER_OF_GUESTS_IS_NOT_NEGATIVE);
        }
    }

    private void validateIsNotEmptyTable() {
        if (!isEmpty()) {
            throw new IllegalStateException(EXCEPTION_MESSAGE_IS_NOT_EMPTY_TABLE);
        }
    }

    public void validateIsEmptyTable() {
        if (isEmpty()) {
            throw new IllegalStateException(EXCEPTION_MESSAGE_IS_EMPTY_TABLE);
        }
    }

    private void validateAlreadyGroup() {
        if (isGrouping()) {
            throw new IllegalStateException(EXCEPTION_MESSAGE_ALREADY_IS_TABLE_GROUP);
        }
    }

    public void validateOrderStatus() {
        if (Objects.nonNull(order) && !order.isCooking()) {
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_ALREADY_COOKING_OR_MEAL);
        }
    }
}
