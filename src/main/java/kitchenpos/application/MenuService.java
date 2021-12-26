package kitchenpos.application;

import kitchenpos.repository.MenuRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.global.exception.EntityNotFoundException;
import kitchenpos.mapper.MenuMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
        final List<Product> products = getMenuProduct(request);
        BigDecimal menuProductPriceSum = getMenuProductPriceSum(request, products);

        final Menu menu = new Menu(request.getName(), request.getPrice(), menuGroup, menuProductPriceSum);
        saveMenuProduct(request, products, menu);

        final Menu savedMenu = menuRepository.save(menu);
        return MenuMapper.toMenuResponse(savedMenu);
    }

    private List<Product> getMenuProduct(final MenuCreateRequest request) {
        return productService.findProducts(request.getMenuProducts()
                .stream()
                .map(MenuCreateRequest.MenuProduct::getProductId)
                .collect(Collectors.toList()));
    }

    private void saveMenuProduct(final MenuCreateRequest request, final List<Product> products, final Menu savedMenu) {
        for (final MenuCreateRequest.MenuProduct menuProductRequest : request.getMenuProducts()) {
            final Product product = getProduct(products, menuProductRequest);
            final MenuProduct menuProduct = new MenuProduct(product, menuProductRequest.getQuantity());

            savedMenu.addMenuProduct(menuProduct);
        }
    }

    private BigDecimal getMenuProductPriceSum(final MenuCreateRequest request, final List<Product> products) {
        BigDecimal menuProductPriceSum = BigDecimal.ZERO;

        for (final MenuCreateRequest.MenuProduct menuProductRequest : request.getMenuProducts()) {
            final Product menuProduct = getProduct(products, menuProductRequest);
            final BigDecimal menuProductTotalPrice = menuProduct.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity()));
            menuProductPriceSum = menuProductPriceSum.add(menuProductTotalPrice);
        }

        return menuProductPriceSum;
    }

    private Product getProduct(final List<Product> products, final MenuCreateRequest.MenuProduct menuProductRequest) {
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
    public List<Menu> findMenus(List<Long> id) {
        return menuRepository.findByIdIn(id);
    }
}
