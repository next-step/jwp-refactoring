package kitchenpos.menu.application;

import java.util.List;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuEventPublisher menuEventPublisher;

    public MenuService(MenuRepository menuRepository, MenuEventPublisher menuEventPublisher) {
        this.menuRepository = menuRepository;
        this.menuEventPublisher = menuEventPublisher;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        List<MenuProduct> menuProducts = menuRequest.toMenuProducts();
        Menu menu = new Menu(menuRequest.getName(), Price.wonOf(menuRequest.getPrice()), menuRequest.getMenuGroupId(), menuProducts);
        MenuResponse menuResponse = MenuResponse.of(menuRepository.save(menu));
        menuEventPublisher.publishCreateMenuEvent(menuResponse);
        return menuResponse;
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return MenuResponse.listOf(menuRepository.findAll());
    }
}
