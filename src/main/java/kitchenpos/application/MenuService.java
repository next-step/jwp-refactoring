package kitchenpos.application;

import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public MenuResponse create(MenuRequest request) {
        validateMenuGroup(request.getMenuGroupId());
        final Menu menu = menuRepository.save(request.toMenu());
        List<MenuProduct> menuProducts = toMenuProducts(request);
        menu.addMenuProducts(menuProducts);
        return MenuResponse.of(menu);
    }

    private void validateMenuGroup(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    private List<MenuProduct> toMenuProducts(MenuRequest request) {
        List<MenuProduct> menuProducts = new ArrayList<>(request.getMenuProducts().size());
        for (MenuProductRequest menuProductRequest : request.getMenuProducts()) {
            Product product = findProductById(menuProductRequest);
            menuProducts.add(menuProductRequest.toMenuProduct(product));
        }
        return menuProducts;
    }

    private Product findProductById(MenuProductRequest menuProductRequest) {
        return productRepository.findById(menuProductRequest.getProductId())
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<MenuResponse> list() {
        return MenuResponse.of(menuRepository.findAll());
    }
}
