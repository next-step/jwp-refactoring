package kitchenpos.menu.application;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
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

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {

        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다."));
        MenuProducts menuProducts = menuProductsFromRequest(menuRequest.getMenuProducts());

        final Menu savedMenu = menuRepository.save(menuRequest.toEntity(menuGroup, menuProducts));
        return MenuResponse.from(savedMenu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return MenuResponse.from(menus);
    }

    @Transactional
    public MenuResponse update(Long menuId, MenuRequest menuRequest) {
        Menu menu = menuRepository.findById(menuId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));

        menu.changeInfo(menuRequest.getName(), new Price(menuRequest.getPrice()));
        return MenuResponse.from(menu);
    }

    private MenuProducts menuProductsFromRequest(List<MenuProductRequest> requests) {
        final List<MenuProduct> menuProducts = new ArrayList<>();

        for (final MenuProductRequest menuProductRequest : requests) {
            final Product product = productRepository.findById(menuProductRequest.getProductId())
                .orElseThrow(IllegalArgumentException::new);
            menuProducts.add(menuProductRequest.toEntity(product));
        }
        return new MenuProducts(menuProducts);
    }
}
