package kitchenpos.table.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.order.dto.OrderTableResponse;

@Entity
@Table(name = "order_table")
public class OrderTableV2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroupV2 tableGroup;

    @Column
    private int numberOfGuests;

    @Column
    private boolean empty;

    protected OrderTableV2(){
    }

    public OrderTableV2(Long id, TableGroupV2 tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public boolean existTableGroupId() {
        return tableGroup != null;
    }

    public void mappingTableGroupId(TableGroupV2 tableGroup) {
        this.tableGroup = tableGroup;
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public void empty() {
        this.empty = true;
    }

    public void notEmpty() {
        this.empty = false;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void ungroupTable() {
        this.tableGroup = null;
    }

    public OrderTableResponse toOrderTableResponse() {
        if (this.tableGroup == null) {
            return new OrderTableResponse(this.id, null, this.numberOfGuests, this.empty);
        }
        return new OrderTableResponse(this.id, this.tableGroup.getId(), this.numberOfGuests, this.empty);
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
                && Objects.equals(tableGroup, that.tableGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableGroup, numberOfGuests, empty);
    }
}
