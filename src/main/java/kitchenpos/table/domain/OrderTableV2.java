package kitchenpos.table.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.dto.OrderTableResponse;

@Entity
@Table(name = "order_table")
public class OrderTableV2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long tableGroupId;

    @Column
    private int numberOfGuests;

    @Column
    private boolean empty;

    protected OrderTableV2(){
    }

    public OrderTableV2(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public boolean existTableGroupId() {
        return tableGroupId != null;
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public void empty() {
        this.empty = true;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public OrderTableResponse toOrderTableResponse() {
        return new OrderTableResponse(this.id, this.tableGroupId, this.numberOfGuests, this.empty);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderTableV2 that = (OrderTableV2) o;
        return numberOfGuests == that.numberOfGuests && empty == that.empty && Objects.equals(id, that.id)
                && Objects.equals(tableGroupId, that.tableGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableGroupId, numberOfGuests, empty);
    }
}
