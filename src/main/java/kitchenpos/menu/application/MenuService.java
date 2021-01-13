package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuRequest.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Service
public class MenuService {

    private final MenuRepository menuRepository;

    private final MenuGroupRepository menuGroupRepository;

    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository,
                       final MenuGroupRepository menuGroupRepository,
                       final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = toMenuGroup(menuRequest.getMenuGroupId());
        MenuProducts menuProducts = toMenuProducts(menuRequest.getMenuProducts());
        Menu savedMenu = menuRepository.save(menuRequest.toMenu(menuGroup, menuProducts));
        return MenuResponse.of(savedMenu);
    }

    private MenuGroup toMenuGroup(final Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("%d에 해당하는 메뉴 그룹이 없습니다.", menuGroupId)));
    }

    private MenuProducts toMenuProducts(final List<MenuProductRequest> menuProducts) {
        return menuProducts.stream()
                .map(this::toMenuProduct)
                .collect(collectingAndThen(toList(), MenuProducts::new));
    }

    private MenuProduct toMenuProduct(final MenuProductRequest menuProductRequest) {
        Long productId = menuProductRequest.getProductId();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("%d에 해당하는 상품이 없습니다.", productId)));
        return new MenuProduct(product, menuProductRequest.getQuantity());
    }

    public List<MenuResponse> findAll() {
        List<Menu> menus = menuRepository.findAll();
        return MenuResponse.ofList(menus);
    }
}
