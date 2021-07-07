package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    protected TableGroup() {
    }

    TableGroup(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    // TODO 이하 삭제
    
    public void setId(Long tableGroupId) {
        this.id = tableGroupId;
    }
}
