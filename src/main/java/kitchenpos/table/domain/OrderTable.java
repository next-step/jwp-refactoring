package kitchenpos.table.domain;

import kitchenpos.table.exception.OrderTableException;

import javax.persistence.*;
import java.util.Objects;

import static kitchenpos.table.exception.OrderTableExceptionType.*;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    private OrderTable(OrderTableBuilder builder) {
        this.id = builder.id;
        this.tableGroupId = builder.tableGroupId;
        this.numberOfGuests = builder.numberOfGuests;
        this.empty = builder.empty;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validateGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new OrderTableException(NEGATIVE_NUMBER_OF_GEUEST);
        }
        if (isEmpty()) {
            throw new OrderTableException(EMPTY_TABLE);
        }
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(final boolean empty) {
        validateTableGroup();
        this.empty = empty;
    }

    private void validateTableGroup() {
        if (Objects.nonNull(tableGroupId)) {
            throw new OrderTableException(USING_TABLE_GROUP);
        }
    }

    public void ungroup() {
        this.tableGroupId = null;
    }

    public static OrderTableBuilder builder() {
        return new OrderTableBuilder();
    }

    public void group(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    public static class OrderTableBuilder {
        private Long id;
        private Long tableGroupId;
        private int numberOfGuests;
        private boolean empty;

        public OrderTableBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public OrderTableBuilder tableGroupId(Long tableGroupId) {
            this.tableGroupId = tableGroupId;
            return this;
        }

        public OrderTableBuilder numberOfGuests(int numberOfGuests) {
            this.numberOfGuests = numberOfGuests;
            return this;
        }

        public OrderTableBuilder empty(boolean empty) {
            this.empty = empty;
            return this;
        }

        public OrderTable build() {
            return new OrderTable(this);
        }
    }
}
