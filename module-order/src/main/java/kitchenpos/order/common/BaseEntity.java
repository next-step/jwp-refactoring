package kitchenpos.order.common;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity {
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
