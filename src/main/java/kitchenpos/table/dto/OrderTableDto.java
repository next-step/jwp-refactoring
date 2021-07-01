package kitchenpos.table.dto;

import java.util.Optional;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public class OrderTableDto {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableDto() { }

    public OrderTableDto(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTableDto(int numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTableDto(int numberOfGuests) {
        this(null, null, numberOfGuests, false);
    }

    public OrderTableDto(boolean empty) {
        this(null, null, 0, empty);
    }

    public static OrderTableDto of(OrderTable orderTable) {
        return new OrderTableDto(orderTable.getId(),
                                 Optional.ofNullable(orderTable.getTableGroup())
                                         .map(TableGroup::getId)
                                         .orElse(null),
                                 orderTable.getNumberOfGuests(),
                                 orderTable.isEmpty());
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

    public boolean isEmpty() {
        return empty;
    }
}
