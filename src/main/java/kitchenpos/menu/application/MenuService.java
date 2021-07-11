package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuService {
    private static final String NOT_FOUND_PRODUCT = "찾으려는 상품이 존재하지 않습니다.";
    private static final String NOT_FOUND_MENU_GROUP = "찾으려는 메뉴 그룹이 존재하지 않습니다.";

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    public MenuResponse create(final MenuRequest menuRequest) {

        final Menu savedMenu = menuRepository.save(new Menu(menuRequest.getName(), menuRequest.getPrice(), findMenuGroup(menuRequest.getMenuGroupId())));

        List<MenuProduct> menuProducts = menuRequest.getMenuProducts().stream()
                .map(menuProduct -> new MenuProduct(savedMenu.id(), findProduct(menuProduct.getProductId()), menuProduct.getQuantity())).collect(Collectors.toList());

        savedMenu.mappingProducts(new MenuProducts(menuProductRepository.saveAll(menuProducts)));
        savedMenu.validateMenuProductsPrice();

        return MenuResponse.of(savedMenu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        for (final Menu menu : menus) {
            menu.mappingProducts(new MenuProducts(menuProductRepository.findAllByMenuId(menu.id())));
        }

        return MenuResponse.ofList(menus);
    }

    private MenuGroup findMenuGroup( Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_MENU_GROUP + " find menuGroupId : " + menuGroupId));
    }

    private Product findProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_PRODUCT + " find productId : " + productId));
    }

}
