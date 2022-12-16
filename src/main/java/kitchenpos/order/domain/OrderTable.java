package kitchenpos.order.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "order_table")
public class OrderTable {
    private static final String EXCEPTION_MESSAGE_NUMBER_OF_GUESTS_IS_NOT_NEGATIVE = "가격은 음수일 수 없습니다.";
    private static final String EXCEPTION_MESSAGE_DO_NOT_CHANGE_EMPTY_TABLE = "빈 테이블은 손님의 수를 변경할 수 없습니다.";
    private static final String EXCEPTION_MESSAGE_ALREADY_IS_TABLE_GROUP = "속해있는 단체 테이블이 있으므로 빈 테이블이 될 수 없습니다.";
    private static final int ZERO = 0;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "bigint(20)")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id", columnDefinition = "bigint(20)", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;
    @Column(nullable = false, columnDefinition = "int(11)")
    private int numberOfGuests;
    @Column(nullable = false, columnDefinition = "bit(1)")
    private boolean empty;

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

    public boolean isEmpty() {
        return empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        validateChangableTable();

        this.numberOfGuests = numberOfGuests;
    }

    public void changeEmpty(boolean empty) {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_ALREADY_IS_TABLE_GROUP);
        }

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

    private void validateChangableTable() {
        if (isEmpty()) {
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_DO_NOT_CHANGE_EMPTY_TABLE);
        }
    }
}
