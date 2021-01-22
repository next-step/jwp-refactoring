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
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;
    private final MenuProductRepository menuProductRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository,
            final MenuProductRepository menuProductRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
        this.menuProductRepository = menuProductRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(() -> {throw new IllegalArgumentException("메뉴그룹이 없습니다."); });

        List<Product> products = productRepository.findAllByIdIn(menuRequest.getProductIds());

        Menu menu = Menu.createMenu(
                menuRequest.getName(),
                menuRequest.getPrice(),
                menuGroup,
                menuRequest.getSumPrice(products)
        );

        Menu savedMenu = menuRepository.save(menu);
        List<MenuProduct> savedMenuProducts = menuProductRepository.saveAll(
                menuRequest.createMenuProducts(menu, products)
        );

        return MenuResponse.of(savedMenu, savedMenuProducts);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAll();
        List<MenuProduct> menuProducts = menuProductRepository.findAllById(
                menus.stream()
                        .map(Menu::getId)
                        .collect(Collectors.toList())
        );

        return menus.stream()
                .map(menu ->
                        MenuResponse.of(menu, menuProducts.stream()
                                .filter(menuProduct -> Objects.equals(menuProduct.getMenu(), menu))
                                .collect(Collectors.toList()))
                ).collect(Collectors.toList());
    }
}
