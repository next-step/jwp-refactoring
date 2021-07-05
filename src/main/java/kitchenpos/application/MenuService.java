package kitchenpos.application;

import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Name;
import kitchenpos.domain.Price;
import kitchenpos.domain.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {
    private final MenuGroupRepository menuGroupRepository;
    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuGroupRepository menuGroupRepository,
            final MenuRepository menuRepository,
            final ProductRepository productRepository
    ) {
        this.menuGroupRepository = menuGroupRepository;
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        final MenuGroup menuGroup = findMenuGroup(menuRequest);
        final List<Product> products = findProducts(menuRequest);
        final MenuProducts menuProducts = MenuProducts.of(menuRequest.toMenuProducts(products));
        final Name menuName = Name.valueOf(menuRequest.getName());
        final Price menuPrice = Price.wonOf(menuRequest.getPrice());
        final Menu persistMenu = menuRepository.save(
            Menu.create(menuName, menuPrice, menuGroup, menuProducts));
        return MenuResponse.of(persistMenu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> findAll() {
        final List<Menu> menus = menuRepository.findAll();
        return MenuResponse.listOf(menus);
    }

    private MenuGroup findMenuGroup(MenuRequest menuRequest) {
        return menuGroupRepository.findById(menuRequest.getMenuGroupId())
            .orElseThrow(() -> new IllegalArgumentException("메뉴 그룹이 존재하지 않는 메뉴는 등록할 수 없습니다."));
    }

    private List<Product> findProducts(final MenuRequest menuRequest) {
        final List<Long> productIds = menuRequest.getProductIds();
        final List<Product> products = productRepository.findAllById(productIds);
        if (products.size() != productIds.size()) {
            throw new IllegalArgumentException("상품으로 등록되지 않은 메뉴는 등록할 수 없습니다.");
        }
        return products;
    }
}
