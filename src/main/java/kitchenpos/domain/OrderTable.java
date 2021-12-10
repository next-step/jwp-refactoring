package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name ="table_group_id")
    private TableGroup tableGroup;
    private int numberOfGuests;

    protected OrderTable() {
    }

    private OrderTable(TableGroup tableGroup, int numberOfGuests) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
    }

    public static OrderTable of(TableGroup tableGroup, int numberOfGuests) {
        return new OrderTable(tableGroup, numberOfGuests);
    }

    public long getId() {
        return this.id;
    }

    public TableGroup getTableGroup() {
        return this.tableGroup;
    }
    public int getNumberOfGuests() {
        return this.numberOfGuests;
    }
}
