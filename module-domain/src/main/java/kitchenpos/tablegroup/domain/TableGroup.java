package kitchenpos.tablegroup.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    public TableGroup() {
        this.createdDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }


//    public void setTableGroupToOrderTables(List<OrderTable> orderTables) {
//        orderTables.forEach(orderTable -> orderTable.addTableGroup(this.id));
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableGroup that = (TableGroup) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdDate);
    }

    @Override
    public String toString() {
        return "TableGroup{" +
                "id=" + id +
                ", createdDate=" + createdDate +
                '}';
    }
}
