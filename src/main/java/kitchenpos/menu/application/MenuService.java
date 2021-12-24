package kitchenpos.menu.application;

import kitchenpos.menu.application.exception.MenuGroupNotFoundException;
import kitchenpos.menu.application.exception.ProductNotFoundException;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
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

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        MenuGroup menuGroup = getMenuGroup(request);
        List<MenuProduct> menuProducts = getMenuProducts(request);

        Menu menu = request.toEntity(menuGroup, menuProducts);
        Menu persistMenu = menuRepository.save(menu);
        return MenuResponse.of(persistMenu);
    }

    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAll();
        return MenuResponse.ofList(menus);
    }

    private MenuGroup getMenuGroup(MenuRequest request) {
        return menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(MenuGroupNotFoundException::new);
    }

    private List<MenuProduct> getMenuProducts(MenuRequest request) {
        return request.getMenuProducts().stream()
                .map(this::getMenuProduct)
                .collect(Collectors.toList());
    }

    private MenuProduct getMenuProduct(MenuProductRequest request) {
        return request.toEntity(getProduct(request.getProductId()));
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
    }
}