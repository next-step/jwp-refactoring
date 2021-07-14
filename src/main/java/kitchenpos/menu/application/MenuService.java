package kitchenpos.menu.application;

import kitchenpos.menu.application.exception.BadProductIdException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.presentation.dto.MenuProductRequest;
import kitchenpos.menu.presentation.dto.MenuRequest;
import kitchenpos.menu.presentation.dto.MenuResponse;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;

    public MenuService(MenuRepository menuRepository, MenuGroupService menuGroupService, ProductService productService) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        List<Product> products = productService.findByIdIn(menuRequest.getProductsIds());
        List<MenuProduct> menuProducts = getMenuProductsBy(menuRequest.getMenuProducts(), products);
        MenuGroup menuGroup = menuGroupService.findById(menuRequest.getMenuGroupId());
        Menu menu = Menu.create(menuRequest.getName(), menuRequest.getPrice(), menuGroup, menuProducts);
        return MenuResponse.of(menuRepository.save(menu));
    }

    private List<MenuProduct> getMenuProductsBy(List<MenuProductRequest> menuProductRequests, List<Product> products) {
        return products.stream()
                .map(product -> createMenuProductWith(menuProductRequests, product))
                .collect(Collectors.toList());
    }

    private MenuProduct createMenuProductWith(List<MenuProductRequest> menuProductRequests, Product product) {
        return menuProductRequests.stream()
                .filter(menuProductRequest -> isProductIdMatch(menuProductRequest, product))
                .map(menuProductRequest -> MenuProduct.of(product, menuProductRequest.getQuantity()))
                .findFirst()
                .orElseThrow(BadProductIdException::new);
    }

    private boolean isProductIdMatch(MenuProductRequest menuProductRequest, Product product) {
        return Objects.equals(menuProductRequest.getProductId(), product.getId());
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return MenuResponse.ofList(menuRepository.findAll());
    }
}
