package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;
    private final MenuProductRepository menuProductRepository;

    public MenuService(
            MenuRepository menuRepository,
            MenuGroupRepository menuGroupRepository,
            ProductRepository productRepository,
            MenuProductRepository menuProductRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
        this.menuProductRepository = menuProductRepository;
    }

    public MenuResponse create(MenuRequest request) {
        MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException("등록하려는 메뉴 그룹: " + request.getMenuGroupId() + "이 존재하지 않습니다."));
        List<Product> products = productRepository.findByIdIn(request.getProductIds());
        request.validateSizeForProducts(products);
        List<MenuProduct> menuProducts = request.createMenuProducts(products);
        Menu savedMenu = menuRepository.save(Menu.create(request.getName(), request.getPrice(), menuGroup, menuProducts));
        List<MenuProduct> savedMenuProducts = menuProductRepository.saveAll(getSavingMenuProducts(menuProducts, savedMenu));
        return MenuResponse.of(savedMenu, savedMenuProducts);
    }

    private List<MenuProduct> getSavingMenuProducts(List<MenuProduct> menuProducts, Menu savedMenu) {
        return menuProducts.stream()
                .map(mp -> mp.updateMenu(savedMenu))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAll();
        List<MenuProduct> menuProducts = menuProductRepository.findAllByMenuIn(menus);
        return menus.stream()
                .map(menu -> MenuResponse.of(menu, extractMenuProducts(menuProducts, menu)))
                .collect(Collectors.toList());
    }

    private List<MenuProduct> extractMenuProducts(List<MenuProduct> menuProducts, Menu menu) {
        return menuProducts.stream()
                .filter(mp -> menu.equals(mp.getMenu()))
                .collect(Collectors.toList());
    }
}
