package kitchenpos.table.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

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
        this.tableGroup = TableGroup.of(tableGroupId);
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

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isGroupable() {
        return isEmpty() && Objects.isNull(tableGroup);
    }

    public void ungroup() {
        this.tableGroup = null;
    }

    public void groupBy(TableGroup tableGroup) {
        changeEmpty(false);
        this.tableGroup = tableGroup;
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
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("그룹이 있는 테이블은 빈 상태로 변경할 수 없습니다");
        }
    }

    private void validateOfChangeNumberOfGuests(NumberOfGuests numberOfGuests) {
        if (isEmpty()) {
            throw new IllegalArgumentException("빈 테이블에 손님 수를 변경 할수 없습니다");
        }
    }
}
