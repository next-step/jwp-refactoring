package kitchenpos.domain;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_table")
public class OrderTable2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "table_group_id")
    private TableGroup2 tableGroup;

    private int numberOfGuests;

    private boolean empty;

    protected OrderTable2() {
    }

    public OrderTable2(TableGroup2 tableGroup, int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        setTableGroup(tableGroup);
    }

    public void setTableGroup(TableGroup2 newTableGroup) {
        if (Objects.nonNull(tableGroup)) {
            tableGroup.getOrderTables().remove(this);
        }
        tableGroup = newTableGroup;
        if (!tableGroup.getOrderTables().contains(this)) {
            tableGroup.getOrderTables().add(this);
        }
    }
}
