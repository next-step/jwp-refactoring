package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;

@Transactional
@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository, final MenuGroupRepository menuGroupRepository,
        final ProductRepository productRepository) {

        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public MenuResponse create(final MenuRequest menuRequest) {
        final String name = menuRequest.getName();
        final MenuGroup menuGroup = findMenuGroup(menuRequest);
        final List<MenuProduct> menuProducts = makeMenuProducts(menuRequest);
        final Menu menu = new Menu(name, menuRequest.getPrice(), menuGroup, menuProducts);
        final Menu saved = menuRepository.save(menu);

        return MenuResponse.of(saved);
    }

    private MenuGroup findMenuGroup(final MenuRequest menuRequest) {
        return menuGroupRepository.findById(menuRequest.getMenuGroupId())
            .orElseThrow(IllegalArgumentException::new);
    }

    private List<MenuProduct> makeMenuProducts(final MenuRequest menuRequest) {
        return menuRequest.getMenuProducts().stream()
            .map(menuProductRequest -> new MenuProduct(findProduct(menuProductRequest),
                menuProductRequest.getQuantity()))
            .collect(Collectors.toList());
    }

    private Product findProduct(final MenuProductRequest menuProductRequest) {
        return productRepository.findById(menuProductRequest.getProductId())
            .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll()
            .stream()
            .map(MenuResponse::of)
            .collect(Collectors.toList());
    }
}
