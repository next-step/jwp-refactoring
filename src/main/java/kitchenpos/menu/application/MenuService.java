package kitchenpos.menu.application;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.appliaction.ProductService;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static kitchenpos.common.Messages.PRODUCT_FIND_IN_NO_SUCH;

@Service
@Transactional(readOnly = true)
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
        final Name name = Name.of(menuRequest.getName());
        final Price price = Price.of(menuRequest.getPrice());
        final MenuGroup menuGroup = menuGroupService.findById(menuRequest.getMenuGroupId());
        final MenuProducts menuProducts = MenuProducts.of(convertToMenuProducts(menuRequest.getMenuProducts()));

        Menu menu = Menu.of(name, price, menuGroup, menuProducts);
        return MenuResponse.of(menuRepository.save(menu));
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream().map(MenuResponse::of).collect(Collectors.toList());
    }

    private List<MenuProduct> convertToMenuProducts(List<MenuProductRequest> menuProducts) {
        Map<Long, Long> menuProductRequests = menuProducts.stream()
                .collect(Collectors.toMap(
                        MenuProductRequest::getProductId,
                        MenuProductRequest::getQuantity
                ));

        List<Long> productsIds = new ArrayList<>(menuProductRequests.keySet());
        List<Product> products = productService.findByIdIn(productsIds);

        if (products.size() != menuProducts.size()) {
            throw new NoSuchElementException(PRODUCT_FIND_IN_NO_SUCH);
        }

        return products.stream()
                .map(product -> MenuProduct.of(product, Quantity.of(menuProductRequests.get(product.getId()))))
                .collect(Collectors.toList());
    }

    public Menu findById(long menuId) {
        return menuRepository.findById(menuId).orElseThrow(NoSuchElementException::new);
    }
}
