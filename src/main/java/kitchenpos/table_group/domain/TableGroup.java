package kitchenpos.table_group.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {
    private static final String ORDER_TABLE_LEAST_2 = "주문 테이블은 최소 2개 이상이어야 합니다";
    private static final int MINIMUM_GROUP_SIZE = 2;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    public static TableGroup group(List<GroupTable> groupTables) {
        if (CollectionUtils.isEmpty(groupTables) || groupTables.size() < MINIMUM_GROUP_SIZE) {
            throw new IllegalArgumentException(ORDER_TABLE_LEAST_2);
        }
        groupTables.forEach(GroupTable::group);
        return new TableGroup();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
