package kitchenpos.domain.domainService;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dto.dto.OrderLineItemDTO;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.repository.MenuRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class MenuOrderLineDomainService {

    private final MenuRepository menuRepository;

    public MenuOrderLineDomainService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public void validateComponentForCreateOrder(OrderRequest orderRequest){
        List<OrderLineItemDTO> orderLineItems = orderRequest.getOrderLineItems();

        orderLineItemsIsEmpty(orderLineItems);
        menuMappedByOrderLineItemsIsExist(orderLineItems);
    }

    private void orderLineItemsIsEmpty(List<OrderLineItemDTO> orderLineItems){
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    private void menuMappedByOrderLineItemsIsExist(List<OrderLineItemDTO> orderLineItems){
        final List<Long> menuIds = orderLineItems.stream()
            .map(OrderLineItemDTO::getMenuId)
            .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }
}
