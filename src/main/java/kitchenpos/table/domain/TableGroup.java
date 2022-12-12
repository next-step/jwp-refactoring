package kitchenpos.table.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private LocalDateTime createdDate;
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    public TableGroup(){}

    private TableGroup(TableGroupBuilder builder){
        this.id = builder.id;
        this.createdDate = builder.createdDate;
        this.orderTables = builder.orderTables;
    }

    public static TableGroupBuilder builder(){
        return new TableGroupBuilder();
    }

    public static class TableGroupBuilder{
        private Long id;
        private LocalDateTime createdDate;
        private List<OrderTable> orderTables;
        public TableGroupBuilder id(Long id){
            this.id = id;
            return this;
        }
        public TableGroupBuilder createDate(LocalDateTime createdDate){
            this.createdDate = createdDate;
            return this;
        }
        public TableGroupBuilder orderTables(List<OrderTable> orderTables){
            this.orderTables = orderTables;
            return this;
        }
        public TableGroup build(){
            return new TableGroup(this);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }
}
