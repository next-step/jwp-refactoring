package kitchenpos.dto;

import kitchenpos.domain.Order;
import kitchenpos.order.domain.Order;
import kitchenpost.dto.OrderTableMapper;
import org.mapstruct.Mapper;

@Mapper(uses = {
        OrderLineItemMapper.class,
        OrderTableMapper.class
})
public interface OrderMapper {

    OrderResponse toResponse(Order order);

}
