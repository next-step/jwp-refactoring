package kitchenpos.menus.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menus.menu.domain.Menu;
import kitchenpos.menus.menu.domain.MenuRepository;
import kitchenpos.menus.menu.dto.MenuRequest;
import kitchenpos.menus.menu.dto.MenuResponse;
import kitchenpos.menus.menu.dto.OrderMenuRequest;
import kitchenpos.menus.menu.dto.OrderMenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;
    private final OrderMenuValidator orderMenuValidator;

    public MenuService(MenuRepository menuRepository, MenuValidator menuValidator,
                       OrderMenuValidator orderMenuValidator) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
        this.orderMenuValidator = orderMenuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        menuValidator.validate(request);
        return MenuResponse.from(menuRepository.save(request.toMenu()));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return MenuResponse.from(menuRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<OrderMenuResponse> findMenuOrdersByIds(OrderMenuRequest requests) {
        orderMenuValidator.validate(requests);
        List<Menu> findMenus = menuRepository.findMenusByIdIn(requests.getMenuIds());
        return findMenus.stream()
                .map(OrderMenuResponse::toOrderMenu)
                .collect(Collectors.toList());
    }
}
