package kitchenpos.table.dto;

import kitchenpos.table.OrderTable;
import org.mapstruct.Mapper;

@Mapper
public interface OrderTableMapper {

    OrderTableResponse toResponse(OrderTable orderTable);

}
