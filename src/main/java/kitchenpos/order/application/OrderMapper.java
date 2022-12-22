package kitchenpos.order.application;

import kitchenpos.ExceptionMessage;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.domain.MenuProduct;
import kitchenpos.product.domain.MenuProductRepository;
import kitchenpos.product.domain.MenuProducts;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderMapper {

    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;
    private final MenuProductRepository menuProductRepository;

    public OrderMapper(OrderTableRepository orderTableRepository, MenuRepository menuRepository,
                       MenuProductRepository menuProductRepository) {
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
        this.menuProductRepository = menuProductRepository;
    }

    public OrderTable getOrderTableById(Long id) {
        OrderTable orderTable = orderTableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.NOT_EXIST_ORDER_TABLE.getMessage()));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException(ExceptionMessage.EMPTY_TABLE.getMessage());
        }

        return orderTable;
    }

    public Map<Long, Menu> getMenus(List<Long> menuIds) {
        List<Menu> menus = menuRepository.findAllById(menuIds);
        if (menus.size() != menuIds.size()) {
            throw new IllegalArgumentException(ExceptionMessage.NOT_EXIST_MENU.getMessage());
        }
        return menus.stream().collect(Collectors.toMap(Menu::getId, menu -> menu, (id1, id2) -> id1));
    }

    public MenuProducts getMenuProductsByMenuId(Long id) {
        List<MenuProduct> menuProductList = menuProductRepository.findAllByMenuId(id);
        return new MenuProducts(menuProductList);
    }
}
