package kitchenpos.menu.application;

import kitchenpos.exception.MenuError;
import kitchenpos.exception.ProductError;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException(MenuError.REQUIRED_MENU_GROUP));
        MenuProducts menuProducts = MenuProducts.of(menuProductsByProductId(request.getMenuProductRequests()));
        Menu menu = request.toMenu(menuGroup, menuProducts);

        return MenuResponse.of(menuRepository.save(menu));
    }

    private List<MenuProduct> menuProductsByProductId(List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
                .map(menuProductRequest -> menuProductRequest.toMenuProduct(
                        productRepository.findById(menuProductRequest.getProductId()).orElseThrow(
                                () -> new EntityNotFoundException(ProductError.NOT_FOUND)
                        )))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return MenuResponse.list(menuRepository.findAll());
    }
}
