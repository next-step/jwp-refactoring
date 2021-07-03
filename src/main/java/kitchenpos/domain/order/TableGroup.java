package kitchenpos.domain.order;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    private LocalDateTime createdDate;

    public static TableGroup of(List<OrderTable> orderTables) {
        return new TableGroup(null, orderTables, LocalDateTime.now());
    }
    // for jpa
    public TableGroup() {
    }

    private TableGroup(Long id, List<OrderTable> orderTables, LocalDateTime createdDate) {
        this.id = id;
        this.orderTables = orderTables;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void changeOrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }
}
