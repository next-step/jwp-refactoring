package kitchenpos.menu.application;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuPrice;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Component
public class MenuCreator {

    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;

    public MenuCreator(MenuGroupRepository menuGroupRepository,
                       MenuProductRepository menuProductRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
    }

    private MenuGroup setMenuGroup(MenuRequest menuRequest) {
        MenuGroup menuGroup = null;
        if (menuRequest.getMenuGroupId() != null) {
            menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId()).orElse(null);
        }
        return menuGroup;
    }

    public Menu toMenu(MenuRequest menuRequest) {
        MenuGroup menuGroup = setMenuGroup(menuRequest);

        List<MenuProduct> menuProducts = menuProductRepository.findAllByMenuId(menuRequest.getId());

        return new Menu(menuRequest.getId(), menuRequest.getName(), new MenuPrice(menuRequest.getPrice()), menuGroup,
                new MenuProducts(menuProducts));
    }

}
