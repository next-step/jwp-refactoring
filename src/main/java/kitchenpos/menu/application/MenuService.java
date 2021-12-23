package kitchenpos.menu.application;

import java.util.stream.Collectors;
import kitchenpos.common.exception.CommonErrorCode;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
        final MenuRepository menuDao,
        final MenuGroupRepository menuGroupDao,
        final ProductRepository productDao
    ) {
        this.menuRepository = menuDao;
        this.menuGroupRepository = menuGroupDao;
        this.productRepository = productDao;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = findMenuGroup(menuRequest.getMenuGroupId());
        List<MenuProduct> menuProducts = getMenuProducts(menuRequest);

        Menu menu = menuRequest.toMenu(menuGroup, menuProducts);

        return MenuResponse.of(menuRepository.save(menu));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return MenuResponse.toList(menuRepository.findAll());
    }

    @Transactional(readOnly = true)
    public MenuGroup findMenuGroup(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
            .orElseThrow(
                () -> new NotFoundException(CommonErrorCode.MENU_GROUP_NOT_FOUND_EXCEPTION));
    }

    @Transactional(readOnly = true)
    public List<MenuProduct> getMenuProducts(final MenuRequest menuRequest) {
        List<Long> productIds = menuRequest.getProductIds();
        List<Product> products = productRepository.findAllById(productIds);

        if (products.size() != productIds.size()) {
            throw new NotFoundException(CommonErrorCode.MENU_PRODUCT_NOT_FOUND);
        }

        return products.stream()
            .map(
                product -> MenuProduct.of(product, menuRequest.getProductQuantity(product.getId())))
            .collect(Collectors.toList());
    }
}
