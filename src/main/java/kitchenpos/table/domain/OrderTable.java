package kitchenpos.table.domain;

import kitchenpos.common.domain.BaseEntity;
import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.IllegalArgumentException;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private Long tableGroupId;

    private int numberOfGuests;

    private boolean empty;

    protected OrderTable() {}

    private OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(null, null, numberOfGuests, empty);
    }

    public void addGroup(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void removeGroup() {
        this.tableGroupId = null;
    }

    public void changeEmpty(boolean empty) {
        checkIsAbleToEmpty();
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        checkIsNumberOfGuestsChangeable(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void checkIsAbleToEmpty() {
        if (isGrouping()) {
            throw new BadRequestException("테이블 그룹이 존재하므로 빈 테이블 설정을 할 수 없습니다.");
        }
    }

    private void checkIsNumberOfGuestsChangeable(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님 수는 최소 0명 이상 설정 가능합니다.");
        }

        if (isEmpty()) {
            throw new BadRequestException("빈 테이블의 손님 수를 설정할 수 없습니다.");
        }
    }

    private boolean isGrouping() {
        return Objects.nonNull(tableGroupId);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTable that = (OrderTable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
