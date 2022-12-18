package kitchenpos.table.domain;

import static javax.persistence.GenerationType.*;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class TableGroup  {
    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;

    public TableGroup() {
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public Long getId() {
        return id;
    }

}
