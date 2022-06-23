package kitchenpos.table.domain;

import kitchenpos.common.domain.NumberOfGuests;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private Long tableGroupId;

    @Embedded
    private NumberOfGuests numberOfGuests;

    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(Long id) {
        this.id = id;
    }

    private OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
        this.empty = empty;
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
        this.empty = empty;
    }

    public static OrderTable of(Long id) {
        return new OrderTable(id);
    }

    public static OrderTable of(int numberOfGuests) {
        return new OrderTable(null, numberOfGuests, false);
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(null, numberOfGuests, empty);
    }
}
