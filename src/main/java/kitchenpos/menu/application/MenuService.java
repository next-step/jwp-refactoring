package kitchenpos.menu.application;

import kitchenpos.menu.dao.MenuRepository;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.dao.MenuGroupRepository;
import kitchenpos.product.dao.ProductRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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
        Menu menu = menuRequest.toMenu();
        menu.updateMenuGroup(findMenuGroup(menuRequest.getMenuGroupId()));
        menu.updateMenuProducts(findMenuProducts(menuRequest.getMenuProducts()));

        return MenuResponse.of(menuRepository.save(menu));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
            .map(MenuResponse::of)
            .collect(Collectors.toList());
    }

    private MenuGroup findMenuGroup(final Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
            .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 메뉴그룹 입니다."));
    }

    private List<MenuProduct> findMenuProducts(final List<MenuProductRequest> menuProductRequests) {
        final List<MenuProduct> menuProducts = new ArrayList<>();
        for (final MenuProductRequest menuProduct : menuProductRequests) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 상품 입니다."));
            menuProducts.add(menuProduct.toMenuProduct(product));
        }
        return menuProducts;
    }
}
