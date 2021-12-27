package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.exceptions.InputMenuDataErrorCode;
import kitchenpos.menu.exceptions.InputMenuDataException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuService(final MenuRepository menuRepository, final MenuGroupRepository menuGroupRepository, final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        final List<Long> productIds = menuRequest.getProductIds();
        validateInputValue(menuRequest);
        List<Product> products = findProducts(productIds);
        Menu createMenu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuRequest.getMenuGroupId(), menuRequest.toMenuProducts(products));
        Menu persistMenu = menuRepository.save(createMenu);

        return MenuResponse.of(persistMenu);
    }

    public List<MenuResponse> findAll() {
        final List<Menu> menus = menuRepository.findAll();
        return MenuResponse.listOf(menus);
    }

    private List<Product> findProducts(List<Long> productIds) {
        List<Product> products =  productRepository.findAllById(productIds);
        if (products.size() != productIds.size()) {
            throw new InputMenuDataException(InputMenuDataErrorCode.THE_PRODUCT_IS_NOT_REGISTERED);
        }
        return products;
    }

    private void validateInputValue(MenuRequest menuRequest) {
        if (!menuGroupRepository.existsById(menuRequest.getMenuGroupId())) {
            throw new InputMenuDataException(InputMenuDataErrorCode.THE_MENU_GROUP_CAN_NOT_SEARCH);
        }
    }
}
