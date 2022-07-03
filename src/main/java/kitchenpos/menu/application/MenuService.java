package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupService menuGroupService, ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.productRepository = productRepository;
    }

    public MenuResponse create(final MenuRequest request) {
        MenuGroup menuGroup = menuGroupService.getMenuGroupById(request.getMenuGroupId());
        Menu menu = new Menu(request.getName(), request.getPrice(), menuGroup.getId(), getMenuProducts(request));
        return MenuResponse.of(menuRepository.save(menu));
    }

    private List<MenuProduct> getMenuProducts(MenuRequest request) {
        return request.getMenuProducts().stream()
                .map(this::mapMenuProducts)
                .collect(Collectors.toList());
    }

    private MenuProduct mapMenuProducts(MenuProductRequest menuProductRequest) {
        final Product product = productRepository.findById(menuProductRequest.getProductId())
                .orElseThrow(NoSuchElementException::new);
        return new MenuProduct(product, menuProductRequest.getQuantity());
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    public Menu getMenuById(Long menuId) {
        return menuRepository.findById(menuId).orElseThrow(IllegalArgumentException::new);
    }
}
