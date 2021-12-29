package kitchenpos.table.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import kitchenpos.table.domain.OrderTable;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * packageName : kitchenpos.dto
 * fileName : OrderTableResponse
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
public class OrderTableResponse {
    private Long id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long tableGroupId;

    private Integer numberOfGuests;

    private boolean empty;

    private OrderTableResponse() {
    }

    private OrderTableResponse(Long id, Long tableGroupId, Integer numberOfGuests, Boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse of(OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(), orderTable.getId(), orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    public static List<OrderTableResponse> ofList(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTableResponse::of)
                .collect(toList());
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
