package kitchenpos.menu.application;

import java.util.List;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(MenuRepository menuRepository, MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    public MenuResponse create(final MenuRequest menuRequest) {
        List<MenuProduct> menuProducts = makeMenuProducts(menuRequest);
        Menu menu = new Menu(menuRequest.getName(), Price.wonOf(menuRequest.getPrice()), menuRequest.getMenuGroupId(), menuProducts);
        return MenuResponse.of(menuRepository.save(menu));
    }

    private List<MenuProduct> makeMenuProducts(MenuRequest menuRequest) {
        List<MenuProductRequest> menuProductRequests = menuRequest.getMenuProducts();
        menuValidator.validationMenuProductPrices(Price.wonOf(menuRequest.getPrice()), menuProductRequests);
        return menuRequest.toMenuProducts();
    }

    public List<MenuResponse> list() {
        return MenuResponse.listOf(menuRepository.findAll());
    }
}
