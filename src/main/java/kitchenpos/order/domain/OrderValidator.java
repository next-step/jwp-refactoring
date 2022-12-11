package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.menu.domain.MenuRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {
    private static final String MENU_NOT_EXIST = "존재하지 않는 메뉴가 포함되어 있습니다.";
    private static final String NOT_EXIST_TABLE = "존재하지 않는 테이블입니다.";
    private static final String EMPTY_TABLE = "이용중이지 않은 테이블에서는 주문 할 수 없습니다.";

    private final MenuRepository menuRepository;
    private final OrderTableDao orderTableDao;

    public OrderValidator(MenuRepository menuRepository, OrderTableDao orderTableDao) {
        this.menuRepository = menuRepository;
        this.orderTableDao = orderTableDao;
    }

    public void validateMenu(Order order) {
        List<Long> assignedMenus = order.findMenus();
        if (menuRepository.countByIdIn(assignedMenus) != assignedMenus.size()) {
            throw new IllegalArgumentException(MENU_NOT_EXIST);
        }
    }

    public void validateTable(Order order) {
        OrderTable table = orderTableDao.findById(order.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_TABLE));

        if (table.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_TABLE);
        }
    }
}
