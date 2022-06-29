package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = findMenuGroup(menuRequest.getMenuGroupId());

        MenuProducts menuProducts = findMenuProducts(menuRequest.getMenuProducts());
        Menu menu = menuRequest.toMenu(menuGroup, menuProducts);

        return MenuResponse.of(saveMenu(menu));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = findMenus();

        return menus.stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    private MenuGroup findMenuGroup(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(NoSuchElementException::new);
    }

    private Menu saveMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    private List<Menu> findMenus() {
        return menuRepository.findAll();
    }

    private MenuProducts findMenuProducts(List<MenuProductRequest> menuProducts) {
        List<Long> productIds = createProductIds(menuProducts);

        List<Product> products = findProductsByIdIn(productIds);

        if (isSameSize(productIds.size(), products.size())) {
            throw new IllegalArgumentException("잘못된 상품 정보 입니다.");
        }

        List<MenuProduct> list = new ArrayList<>();

        for (int i = 0; i < products.size(); i++) {
            list.add(new MenuProduct(products.get(i), menuProducts.get(i).getQuantity()));
        }

        return new MenuProducts(list);
    }

    private List<Product> findProductsByIdIn(List<Long> productIds) {
        return productRepository.findAllByIdIn(productIds);
    }

    private List<Long> createProductIds(List<MenuProductRequest> menuProducts) {
        return menuProducts.stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());
    }

    private boolean isSameSize(int size, int comparison) {
        return size != comparison;
    }
}
