package kitchenpos.domain.table;

import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import org.springframework.data.domain.AbstractAggregateRoot;

@Entity
public class OrderTable extends AbstractAggregateRoot<OrderTable> {

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

    private OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
        this.empty = empty;
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
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
        return new OrderTable(null, numberOfGuests, empty);
    }

    public boolean isGroupable() {
        return isEmpty() && Objects.isNull(getTableGroupId());
    }

    public void groupBy(Long tableGroupId) {
        changeEmpty(false);
        this.tableGroupId = tableGroupId;
    }

    public void ungroup() {
        this.tableGroupId = null;
    }

    private void validateForChangeNumberOfGuests() {
        if (isEmpty()) {
            throw new IllegalArgumentException("테이블이 비어있습니다");
        }
    }

    public void validateForChangeEmpty() {
        if (hasGroup()) {
            throw new IllegalArgumentException("그룹이 있는 테이블은 빈 상태로 변경할 수 없습니다");
        }
    }

    private boolean hasGroup() {
        return Objects.nonNull(tableGroupId);
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public void changeNumberOfGuests(final NumberOfGuests numberOfGuests) {
        validateForChangeNumberOfGuests();
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(final boolean empty) {
        this.empty = empty;
        registerEvent(this);
    }
}
