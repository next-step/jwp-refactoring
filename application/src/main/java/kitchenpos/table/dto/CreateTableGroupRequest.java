package kitchenpos.table.dto;

import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static java.util.stream.Collectors.toList;

public class CreateTableGroupRequest {

    @NotNull
    @Size(min = 1, message = "주문 테이블은 1개 이상 입력해야 합니다.")
    private List<OrderTableIdRequest> orderTables;

    public CreateTableGroupRequest() { }

    public CreateTableGroupRequest(List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public CreateTableGroupDto toDomainDto() {
        return new CreateTableGroupDto(orderTables.stream()
                                                  .map(OrderTableIdRequest::toDomainDto)
                                                  .collect(toList()));
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }
}
