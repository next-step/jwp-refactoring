package kitchenpos.order.application;

import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;

@Component
public class OrderValidator {
    private MenuService menuService;
    
    public OrderValidator(MenuService menuService) {
        this.menuService = menuService;
    }
    
    public void checkMenu(List<Long> menuIds) {
        List<Menu> menus = menuService.findAllByIds(menuIds);
        
        if (menus.size() != menuIds.size()) {
            new IllegalArgumentException("등록된 메뉴만 주문할 수 있습니다");
        }
    }
}
