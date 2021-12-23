package kitchenpos.table.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import kitchenpos.table.domain.OrderTable;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

/**
 * packageName : kitchenpos.dto
 * fileName : OrderTableResponse
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
//FIXME 생성자 제한하기
public class OrderTableResponse {
    private Long id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long tableGroupId;

    private Integer numberOfGuests;

    private boolean empty;

    public OrderTableResponse() {
    }

    public OrderTableResponse(Long id, Long tableGroupId, Integer numberOfGuests, Boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTableResponse(Long id, Integer numberOfGuests, Boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse of(OrderTable orderTable) {
        if (Objects.isNull(orderTable.getTableGroup())) {
            return new OrderTableResponse(orderTable.getId(), orderTable.getNumberOfGuests(), orderTable.isEmpty());
        }

        return new OrderTableResponse(orderTable.getId(), orderTable.getTableGroup().getId(), orderTable.getNumberOfGuests(), orderTable.isEmpty());
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
