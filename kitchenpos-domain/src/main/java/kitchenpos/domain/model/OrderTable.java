package kitchenpos.domain.model;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-15
 */
@Entity
@Table(name = "order_table")
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tableGroupId;

    private Integer numberOfGuests;

    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(Long tableGroupId, Integer numberOfGuests, boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("주문 테이블에 변경 인원이 0명 이상이여야 합니다.");
        }

        if (this.empty) {
            throw new IllegalArgumentException("주문 테이블이 비어있는 경우 인원 수 변경이 불가 합니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void changeEmpty(boolean empty) {
        if (!Objects.nonNull(this.tableGroupId)) {
            throw new IllegalArgumentException("변경시에는 테이블 그룹이 존재해야 합니다.");
        }
        this.empty = empty;
    }

    public void groupBy(Long tableGroupId) {
        if (!empty) {
            throw new IllegalArgumentException("단체 지정의 모든 주문 테이블은 공석이여야 합니다.");
        }
        if (!Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException("단체 지정에 등록되지 않은 주문 테이블이 있어선 안됩니다.");
        }
        this.tableGroupId = tableGroupId;
    }

    public void ungroup() {
        this.tableGroupId = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderTable that = (OrderTable) o;
        return empty == that.empty && Objects.equals(tableGroupId, that.tableGroupId)
                && Objects.equals(numberOfGuests, that.numberOfGuests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableGroupId, numberOfGuests, empty);
    }



}
