package kitchenpos.menu.application;

import static java.util.stream.Collectors.toList;

import java.util.stream.Collectors;
import kitchenpos.core.exception.CannotCreateException;
import kitchenpos.core.exception.ExceptionType;
import kitchenpos.core.exception.NotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menuGroup.domain.MenuGroup;
import kitchenpos.menuGroup.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.menu.domain.request.MenuProductRequest;
import kitchenpos.menu.domain.request.MenuRequest;
import kitchenpos.menu.domain.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
            .orElseThrow(() -> new NotFoundException(ExceptionType.NOT_EXIST_MENU.getMessage(
                menuRequest.getMenuGroupId())));

        List<MenuProduct> menuProducts = convertToEntityIncludeProduct(
            menuRequest.getMenuProductRequests());

        Menu menu = Menu.of(menuRequest.getName(), menuRequest.getPrice(), menuGroup.getId(), menuProducts);
        Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.of(savedMenu);
    }

    private List<MenuProduct> convertToEntityIncludeProduct(List<MenuProductRequest> menuProductRequests) {
        List<Long> productIds = menuProductRequests.stream()
            .map(MenuProductRequest::getProductId)
            .collect(toList());

        List<Product> products = productRepository.findByIdIn(productIds);
        if (products.size() != menuProductRequests.size()) {
            throw new CannotCreateException(ExceptionType.CONTAINS_NOT_EXIST_PRODUCT);
        }

        return menuProductRequests.stream()
            .map(it -> MenuProduct.of(it.getProductId(), it.getQuantity()))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAllMenuAndProducts();

        return menus.stream()
            .map(MenuResponse::of)
            .collect(toList());
    }
}
