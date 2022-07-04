package kitchenpos.menu.application;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.product.domain.Price;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.creator.MenuCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuCreator menuCreator;

    public MenuService(
            final MenuRepository menuRepository, MenuCreator menuCreator) {
        this.menuRepository = menuRepository;
        this.menuCreator = menuCreator;
    }

    @Transactional
    public Menu create(final MenuRequest menuRequest) {
        Menu menu = menuCreator.toMenu(menuRequest);
        final Price price = menu.getPrice();

        validateMenuGroup(menu);

        final MenuProducts menuProducts = menu.getMenuProducts();
        menuProducts.validateTotalPriceNotExpensiveThanEach(price);

        return menuRepository.save(menu);
    }

    private void validateMenuGroup(Menu menu) {
        if (menu.getMenuGroup() != null && menu.getMenuGroup().getId() == null) {
            throw new IllegalArgumentException();
        }
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
