package kitchenpos.table.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private Long tableGroupId;

    @Embedded
    private NumberOfGuests numberOfGuests;

    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(Long id) {
        this.id = id;
    }

    private OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
        this.empty = empty;
    }

    public OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
        this.empty = empty;
    }

    public static OrderTable of(Long id) {
        return new OrderTable(id);
    }

    public static OrderTable of(int numberOfGuests) {
        return new OrderTable(null, numberOfGuests, false);
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public static OrderTable of(long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTable(tableGroupId, numberOfGuests, empty);
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroup() {
        return tableGroupId;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isGroupable() {
        return isEmpty() && Objects.isNull(tableGroupId);
    }

    public void ungroup() {
        this.tableGroupId = null;
    }

    public void groupBy(Long tableGroupId) {
        changeEmpty(false);
        this.tableGroupId = tableGroupId;
    }

    public void changeNumberOfGuests(NumberOfGuests numberOfGuests) {
        validateOfChangeNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public void changeEmpty(final boolean empty) {
        validateOfChangeEmpty();
        this.empty = empty;
    }

    private void validateOfChangeEmpty() {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException("그룹이 있는 테이블은 빈 상태로 변경할 수 없습니다");
        }
    }

    private void validateOfChangeNumberOfGuests(NumberOfGuests numberOfGuests) {
        if (isEmpty()) {
            throw new IllegalArgumentException("빈 테이블에 손님 수를 변경 할수 없습니다");
        }
    }
}
