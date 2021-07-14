package kitchenpos.menu.application;

import static kitchenpos.exception.KitchenposExceptionMessage.NOT_FOUND_MENU_GROUP;
import static kitchenpos.exception.KitchenposExceptionMessage.NOT_FOUND_PRODUCT;

import java.util.stream.Collectors;
import kitchenpos.exception.KitchenposException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
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

    public MenuResponse create(final MenuRequest request) {
        final MenuGroup menuGroup = findMenuGroupById(request.getMenuGroupId());
        Menu savedMenu = menuRepository.save(request.toMenu(menuGroup, getMenuProducts(request)));
        return MenuResponse.of(savedMenu);
    }

    private List<MenuProduct> getMenuProducts(MenuRequest request) {
        return request.getMenuProducts()
                      .stream()
                      .map(this::createMenuProduct)
                      .collect(Collectors.toList());
    }

    private MenuProduct createMenuProduct(MenuProductRequest menuProductRequest) {
        return new MenuProduct(findProductById(menuProductRequest),
                               menuProductRequest.getQuantity());
    }

    private Product findProductById(MenuProductRequest menuProductRequest) {
        return productRepository.findById(menuProductRequest.getProductId())
                                .orElseThrow(() -> new KitchenposException(NOT_FOUND_PRODUCT));
    }

    private MenuGroup findMenuGroupById(final Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                                  .orElseThrow(() -> new KitchenposException(NOT_FOUND_MENU_GROUP));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                             .map(MenuResponse::of)
                             .collect(Collectors.toList());
    }
}
