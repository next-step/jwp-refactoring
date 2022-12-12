package kitchenpos.order.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "table_group_id")
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    private TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroup of(Long id, LocalDateTime createdDate, OrderTable... orderTables) {
        return new TableGroup(id, createdDate, Arrays.asList(orderTables));
    }

    public static TableGroup of(Long id, LocalDateTime createdDate) {
        return new TableGroup(id, createdDate, null);
    }

    public Long getId() {
        return id;
    }

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }
}
