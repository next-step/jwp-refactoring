package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Price;
import kitchenpos.dto.MenuRequest;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.ui.creator.MenuCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuCreator menuCreator;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository, MenuCreator menuCreator) {
        this.menuRepository = menuRepository;
        this.menuCreator = menuCreator;
    }

    @Transactional
    public Menu create(final MenuRequest menuRequest) {
//    public Menu create(final Menu menu) {
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
