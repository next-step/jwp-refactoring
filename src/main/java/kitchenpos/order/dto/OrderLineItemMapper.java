package kitchenpos.order.dto;

import kitchenpos.menu.dto.MenuMapper;
import kitchenpos.order.domain.OrderLineItem;
import org.mapstruct.Mapper;

@Mapper(uses = {
        MenuMapper.class
})
public interface OrderLineItemMapper {

    OrderLineItemResponse toResponse(OrderLineItem orderLineItem);

}
