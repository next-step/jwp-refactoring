package kitchenpos.tobe.menu.application;

import kitchenpos.tobe.menu.domain.Menu;
import kitchenpos.tobe.menu.domain.MenuProduct;
import kitchenpos.tobe.menu.domain.MenuProducts;
import kitchenpos.tobe.menu.domain.MenuRepository;
import kitchenpos.tobe.menu.dto.MenuRequest;
import kitchenpos.tobe.menu.dto.MenuResponse;
import kitchenpos.tobe.menugroup.application.MenuGroupNotFoundException;
import kitchenpos.tobe.menugroup.domain.MenuGroup;
import kitchenpos.tobe.menugroup.domain.MenuGroupRepository;
import kitchenpos.tobe.product.domain.Product;
import kitchenpos.tobe.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Transactional(readOnly = true)
@Service
public class MenuService {
    public static final String NOT_EXIST_MENU_GROUP = "존재하지 않는 메뉴그룹";
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            MenuRepository menuRepository,
            MenuGroupRepository menuGroupRepository,
            ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(MenuRequest menuRequest) {
        MenuGroup menuGroup = getMenuGroup(menuRequest);
        List<Product> products = productRepository.findAllById(menuRequest.getProductIds());
        List<MenuProduct> menuProducts = menuRequest.getMenuProductsBy(products);
        Menu menu = Menu.createWithMenuProduct(menuRequest.getName(), menuRequest.getPrice(), new MenuProducts(menuProducts), menuGroup);
        Menu savedMenu = menuRepository.save(menu);
        return new MenuResponse(savedMenu);
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::new)
                .collect(Collectors.toList());
    }

    private MenuGroup getMenuGroup(MenuRequest menuRequest) {
        return menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(() -> new MenuGroupNotFoundException(NOT_EXIST_MENU_GROUP));
    }
}
