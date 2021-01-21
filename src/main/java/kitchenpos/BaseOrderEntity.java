package kitchenpos;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseOrderEntity {
    @CreatedDate
    private LocalDateTime orderedTime;

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }
}
