package kitchenpos.domain.table;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    protected TableGroup() {
    }

    private TableGroup(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static TableGroup of(List<OrderTable> orderTables) {
        return new TableGroup(orderTables);
    }


    public Long getId() {
        return this.id;
    }

    public LocalDateTime getCreatedDate() {
        return this.createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return this.orderTables;
    }

    public void changeCreatedDate(LocalDateTime localDateTime) {
        this.createdDate = localDateTime;
    }
}
