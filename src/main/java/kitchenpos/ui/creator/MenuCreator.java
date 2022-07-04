package kitchenpos.ui.creator;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Price;
import kitchenpos.dto.MenuProductRequest;
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
    private final MenuProductCreator menuProductCreator;

    public MenuCreator(MenuGroupRepository menuGroupRepository,
                       MenuProductRepository menuProductRepository,
                       MenuProductCreator menuProductCreator) {
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.menuProductCreator = menuProductCreator;
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

    public MenuRequest toMenuRequest(Menu menu) {
        List<MenuProductRequest> menuProductRequests = setMenuProductReqeusts(menu);
        Long menuGroupId = setMenuGroupId(menu);

        return MenuRequest.builder().id(menu.getId())
                .name(menu.getName())
                .price(menu.getPrice().getPrice())
                .menuGroupId(menuGroupId)
                .menuProducts(menuProductRequests)
                .build();
    }

    private MenuGroup setMenuGroup(MenuRequest menuRequest) {
        MenuGroup menuGroup = null;
        if(menuRequest.getMenuGroupId() != null){
            menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId()).orElse(null);
        }
        return menuGroup;
    }

    private Long setMenuGroupId(Menu menu) {
        Long menuGroupId = null;
        if(menu.getMenuGroup()!= null){
            menuGroupId = menu.getMenuGroup().getId();
        }
        return menuGroupId;
    }

    private List<MenuProductRequest> setMenuProductReqeusts(Menu menu) {
        List<MenuProductRequest> menuProductRequests = null;
        if (menu.getMenuProducts() != null && menu.getMenuProducts().getMenuProducts()!= null) {
            menuProductRequests = menu.getMenuProducts().getMenuProducts().stream()
                    .map(menuProductCreator::toMenuProductRequest)
                    .collect(Collectors.toList());
        }
        return menuProductRequests;
    }
}
