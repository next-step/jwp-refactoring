package kitchenpos.table.domain.tablegroup;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kitchenpos.table.domain.table.OrderTable;
import kitchenpos.table.domain.table.OrderTables;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate = LocalDateTime.now();

    @JsonIgnore
    @Embedded
    private OrderTables orderTables = new OrderTables();

    public TableGroup() {}

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void addOrderTable(OrderTable orderTable) {
        orderTables.addOrderTable(this, orderTable);
    }

    public void ungroup() {
        orderTables.ungroup();
    }
}
