package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menuGroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        validateExistMenuGroup(request.getMenuGroupId());

        List<MenuRequest.ProductInfo> productInfos = request.getProductInfos();
        Map<Long, Product> products = findProductAllByIdIn(request.getProductIds());

        validateProductSize(productInfos, products);

        List<MenuProduct> menuProducts = createMenuProducts(productInfos, products);

        Menu menu = Menu.createMenu(request.getName(), request.getMenuGroupId(), request.getPrice(), menuProducts);

        return new MenuResponse(menuRepository.save(menu));
    }

    private Map<Long, Product> findProductAllByIdIn(List<Long> productIds) {
        if (CollectionUtils.isEmpty(productIds)) {
            throw new IllegalArgumentException();
        }
        return productRepository.findAllByIdIn(productIds).stream()
                .collect(Collectors.toMap(Product::getId, product -> product));
    }

    private void validateProductSize(List<MenuRequest.ProductInfo> productInfos, Map<Long, Product> products) {
        if (productInfos.size() != products.size()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateExistMenuGroup(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    private List<MenuProduct> createMenuProducts(List<MenuRequest.ProductInfo> productInfos, Map<Long, Product> products) {
        return productInfos.stream()
                .map(productInfo -> createMenuProduct(productInfo, products))
                .collect(Collectors.toList());
    }

    private MenuProduct createMenuProduct(final MenuRequest.ProductInfo productInfo, final Map<Long, Product> products) {
        Product product = products.get(productInfo.getProductId());
        return new MenuProduct(product, productInfo.getQuantity());
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::new)
                .collect(Collectors.toList());
    }
}
