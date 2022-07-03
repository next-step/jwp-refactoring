package kitchenpos.table.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ExceptionType;
import kitchenpos.table.event.TableChangeEmptyEventPublisher;
import org.springframework.data.domain.AbstractAggregateRoot;

@Entity
@Table(name = "order_table")
public class OrderTable extends AbstractAggregateRoot<OrderTable> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    private OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    private OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(Long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTable(tableGroupId, numberOfGuests, empty);
    }

    public static OrderTable of(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }

    public void mapIntoGroup(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    public boolean isGrouped() {
        return Objects.nonNull(tableGroupId);
    }

    public void validateHasTableGroupId() {
        if (isGrouped()) {
            throw new BadRequestException(ExceptionType.TABLE_IS_GROUPED);
        }
    }

    public void validateIsEmpty() {
        if (isEmpty()) {
            throw new BadRequestException(ExceptionType.EMPTY_TABLE);
        }
    }

    public void emptyTheTable() {
        registerEvent(new TableChangeEmptyEventPublisher(this));
        this.empty = true;
    }

    public void fillTheTable() {
        this.empty = false;
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

    public void unGroup() {
        this.tableGroupId = null;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
}
