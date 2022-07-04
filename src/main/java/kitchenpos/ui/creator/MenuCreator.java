package kitchenpos.ui.creator;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Price;
import kitchenpos.dto.MenuRequest;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
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
        if(menuRequest.getMenuGroupId() != null){
            menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId()).orElse(null);
        }
        return menuGroup;
    }
    
    public Menu toMenu(MenuRequest menuRequest) {
        MenuGroup menuGroup = setMenuGroup(menuRequest);

        List<MenuProduct> menuProducts = menuProductRepository.findAllByMenuId(menuRequest.getId());

        return Menu.builder().id(menuRequest.getId())
                .name(menuRequest.getName())
                .price(new Price(menuRequest.getPrice()))
                .menuGroup(menuGroup)
                .menuProducts(new MenuProducts(menuProducts))
                .build();
    }

}
