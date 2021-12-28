package kitchenpos.table.domain;

import kitchenpos.utils.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.*;

@Table(name = "table_group")
@Entity
public class TableGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    protected TableGroup() {
    }

    public static TableGroup setUp() {
        return new TableGroup();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return super.createdDate;
    }
}
