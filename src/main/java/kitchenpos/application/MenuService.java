package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.request.MenuProductRequest;
import kitchenpos.request.MenuRequest;
import kitchenpos.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupService menuGroupService,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        final MenuGroup menuGroup = menuGroupService.findMenuGroupById(menuRequest.getMenuGroupId());

        final List<MenuProductRequest> menuProductRequests = menuRequest.getMenuProducts();
        final List<MenuProduct> menuProducts = toMenuProducts(menuProductRequests);
        final Menu menu = Menu.of(menuRequest, menuGroup, menuProducts);

        return MenuResponse.of(menuRepository.save(menu));
    }

    private List<MenuProduct> toMenuProducts(final List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
                .map(menuProductRequest -> {
                    final Product product = findProductById(menuProductRequest);
                    return MenuProduct.of(product, menuProductRequest.getQuantity());
                })
                .collect(Collectors.toList());
    }

    private Product findProductById(final MenuProductRequest menuProductRequest) {
        return productRepository.findById(menuProductRequest.getProductId())
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<MenuResponse> list() {
        return MenuResponse.of(menuRepository.findAll());
    }
}
