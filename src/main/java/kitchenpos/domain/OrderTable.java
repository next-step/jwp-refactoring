package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private TableGroup tableGroup;

    private int numberOfGuests;
    private boolean empty;


    public OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void validateCanGroupTable() {
        if (!empty || Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException();
        }
    }

    public void changeEmpty(OrderTable orderTable){
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException();
        }

        setEmpty(orderTable.isEmpty());
    }
}
