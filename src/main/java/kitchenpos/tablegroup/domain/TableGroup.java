package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;

    public TableGroup() {
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final TableGroup that = (TableGroup)o;
        return Objects.equals(id, that.id) && Objects.equals(createdDate, that.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdDate);
    }
}
