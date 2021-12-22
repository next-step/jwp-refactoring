package kitchenpos.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private LocalDateTime createdDate;
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    public TableGroup() {
    }

    public TableGroup(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        this.orderTables = new ArrayList<>();
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
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
}
