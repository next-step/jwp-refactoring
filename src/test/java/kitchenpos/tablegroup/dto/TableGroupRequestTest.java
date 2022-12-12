package kitchenpos.tablegroup.dto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.tablegroup.dto.TableGroupRequest.OrderTableDto;

public class TableGroupRequestTest {

    public static TableGroupRequest 단체_지정_생성_요청_객체_생성(List<OrderTableDto> orderTables) {
        return new TableGroupRequest.Builder()
                .orderTables(orderTables)
                .build();
    }

    public static TableGroupRequest 단체_지정_생성_요청_객체_생성(Long... orderTableIds) {
        List<OrderTableDto> orderTables = Arrays.stream(orderTableIds)
                .map(OrderTableDto::new)
                .collect(Collectors.toList());

        return new TableGroupRequest.Builder()
                .orderTables(orderTables)
                .build();
    }

}