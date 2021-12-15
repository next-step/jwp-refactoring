package kitchenpos.domain.order.application;

import kitchenpos.domain.menu.domain.MenuRepository;
import kitchenpos.domain.order.domain.OrderTableVO;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class OrderValidator {

    private final MenuRepository menuRepository;
    private final TableTranslator tableTranslator;

    public OrderValidator(
            final MenuRepository menuRepository,
            final TableTranslator tableTranslator
    ) {
        this.menuRepository = menuRepository;
        this.tableTranslator = tableTranslator;
    }

    public void validateEmptyTable(Long orderTableId) {
        OrderTableVO orderTable = tableTranslator.getOrderTable(orderTableId);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public void validateMenus(List<Long> menuIds) {
        if (CollectionUtils.isEmpty(menuIds)) {
            throw new IllegalArgumentException();
        }

        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }
}
