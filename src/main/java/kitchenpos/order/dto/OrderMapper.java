package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.table.dto.OrderTableMapper;
import org.mapstruct.Mapper;

@Mapper(uses = {
        OrderLineItemMapper.class,
        OrderTableMapper.class
})
public interface OrderMapper {

    OrderResponse toResponse(Order order);

}
