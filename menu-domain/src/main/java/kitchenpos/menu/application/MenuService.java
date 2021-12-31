package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.exception.ExceptionMessage;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.repository.ProductRepository;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
        ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup findMenuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
            .orElseThrow(() -> new NotFoundException(ExceptionMessage.NOT_FOUND_DATA));

        List<MenuProduct> menuProducts = findMenuProducts(menuRequest.getMenuProducts());
        Menu menu = menuRequest.toEntity(findMenuGroup, menuProducts);
        return new MenuResponse(menuRepository.save(menu));
    }

    private List<MenuProduct> findMenuProducts(List<MenuProductRequest> menuProducts) {
        return menuProducts.stream().map(menuProduct ->
                menuProduct.toEntity(findProduct(menuProduct.getProductId())))
            .collect(Collectors.toList());
    }

    private Product findProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new NotFoundException(
            ExceptionMessage.NOT_FOUND_DATA));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findMenus();
        return menus.stream()
            .map(MenuResponse::new)
            .collect(Collectors.toList());
    }
}
