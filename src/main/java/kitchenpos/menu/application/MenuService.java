package kitchenpos.menu.application;

import kitchenpos.product.application.ProductService;
import kitchenpos.global.exception.EntityNotFoundException;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.mapper.MenuMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuService {

    private final MenuGroupService menuGroupService;
    private final ProductService productService;
    private final MenuRepository menuRepository;

    public MenuService(final MenuGroupService menuGroupService, final ProductService productService, final MenuRepository menuRepository) {
        this.menuGroupService = menuGroupService;
        this.productService = productService;
        this.menuRepository = menuRepository;
    }

    public MenuResponse create(final MenuCreateRequest request) {
        final MenuGroup menuGroup = menuGroupService.findMenuGroup(request.getMenuGroupId());
        final List<Product> products = getProducts(request);

        final List<MenuProduct> menuProduct = getMenuProduct(request, products);

        final Menu menu = new Menu(request.getName(), request.getPrice(), menuGroup, menuProduct);

        final Menu savedMenu = menuRepository.save(menu);
        return MenuMapper.toMenuResponse(savedMenu);
    }

    private List<Product> getProducts(final MenuCreateRequest request) {
        final List<Long> productIds = request.getMenuProducts()
                .stream()
                .map(MenuCreateRequest.MenuProductRequest::getProductId)
                .collect(Collectors.toList());

        return productService.findProducts(productIds);
    }

    private List<MenuProduct> getMenuProduct(final MenuCreateRequest request, final List<Product> products) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (final MenuCreateRequest.MenuProductRequest menuProductRequest : request.getMenuProducts()) {
            final Product product = getProduct(products, menuProductRequest);
            menuProducts.add(new MenuProduct(product, menuProductRequest.getQuantity()));
        }
        return menuProducts;
    }

    private Product getProduct(final List<Product> products, final MenuCreateRequest.MenuProductRequest menuProductRequest) {
        return products.stream()
                .filter(product -> product.getId().equals(menuProductRequest.getProductId()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(String.format("menu product not found. find menu product id is %d", menuProductRequest.getProductId())));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return MenuMapper.toMenuResponses(menus);
    }

    @Transactional(readOnly = true)
    public List<Menu> findMenus(List<Long> ids) {
        return menuRepository.findByIdIn(ids);
    }
}
