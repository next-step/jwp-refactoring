package kitchenpos.tablegroup.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class TableGroup {
    @Transient
    public static final int MIN_NUMBER_TABLES = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private final LocalDateTime createdDate = LocalDateTime.now();

    public TableGroup() {
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
