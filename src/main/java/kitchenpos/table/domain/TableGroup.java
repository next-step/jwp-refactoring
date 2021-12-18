package kitchenpos.table.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "table_group")
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "table_group")
    private List<OrderTable> orderTables;

    protected TableGroup() {
    }

    public TableGroup(long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void createdNow() {
        this.createdDate = LocalDateTime.now();
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void initOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }
}
