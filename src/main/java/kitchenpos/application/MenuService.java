package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequestDto;
import kitchenpos.dto.MenuRequestDto;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(final MenuRequestDto request) {
        MenuGroup menuGroup = getMenuGroup(request.getMenuGroupId());
        Menu menu = new Menu(request.getName(), request.getPrice(), menuGroup);
        menu.addMenuProducts(createMenuProducts(request));
        return menuRepository.save(menu);
    }

    private MenuGroup getMenuGroup(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId).orElseThrow(IllegalArgumentException::new);
    }

    private List<MenuProduct> createMenuProducts(MenuRequestDto request) {
        List<MenuProduct> menuProducts = request.getMenuProducts().stream()
                .map(this::getMenuProduct)
                .collect(Collectors.toList());
        return menuProducts;
    }

    private MenuProduct getMenuProduct(MenuProductRequestDto menuProductRequest) {
        final Product product = productRepository.findById(menuProductRequest.getProductId())
                .orElseThrow(IllegalArgumentException::new);
        return new MenuProduct(product, menuProductRequest.getQuantity());
    }

    public List<Menu> list() {
        final List<Menu> menus = menuRepository.findAll();

//        for (final Menu menu : menus) {
//            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
//        }

        return menus;
    }
}
