package kitchenpos.table.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private TableGroup tableGroup;

    private int numberOfGuests;
    private boolean empty;

    public OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
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

    public void changeEmpty(OrderTable orderTable) {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException();
        }

        setEmpty(orderTable.isEmpty());
    }

    public int checkNumberOfGuests() {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
        return numberOfGuests;
    }

    public void checkOrderTableSave(int numberOfGuestsInput) {
        if (empty) {
            throw new IllegalArgumentException();
        }
        setNumberOfGuests(numberOfGuestsInput);
    }
}
