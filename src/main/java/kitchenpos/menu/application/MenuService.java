package kitchenpos.menu.application;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.core.exception.CannotCreateException;
import kitchenpos.core.exception.ExceptionType;
import kitchenpos.core.exception.NotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.menu.domain.request.MenuProductRequest;
import kitchenpos.menu.domain.request.MenuRequest;
import kitchenpos.menu.domain.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {

    private static final int FIRST_INDEX = 0;

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

        Menu menu = Menu.of(menuRequest.getName(), menuRequest.getPrice(), menuGroup, menuProducts);
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

        Map<Long, List<Product>> productMap = products.stream()
            .collect(groupingBy(Product::getId));

        return menuProductRequests.stream()
            .map(it -> MenuProduct.of(productMap.get(it.getProductId()).get(FIRST_INDEX),
                it.getQuantity()))
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
