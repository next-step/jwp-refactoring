package kitchenpos.application;

import kitchenpos.dao.MenuRepository;
import kitchenpos.domain.*;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.exception.NoMenuException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;

    public MenuService(
            final MenuRepository menuRepository,
            MenuGroupService menuGroupService, ProductService productService) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupService.findById(menuRequest.getMenuGroupId());
        MenuProducts menuProducts = new MenuProducts();
        for (MenuProductRequest menuProductRequest : menuRequest.getMenuProducts()) {
            Product product = productService.findById(menuProductRequest.getProductId());
            menuProducts.add(new MenuProduct(product, menuProductRequest.getQuantity()));
        }
        Menu menu = new Menu(menuRequest.getName(), new Price(menuRequest.getPrice()), menuGroup, menuProducts);
        Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.from(savedMenu);
    }

    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAll();
        return MenuResponse.fromList(menus);
    }

    public Menu findById(Long menuId) {
        return menuRepository.findById(menuId).orElseThrow(NoMenuException::new);
    }
}
