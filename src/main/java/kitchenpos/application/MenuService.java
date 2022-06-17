package kitchenpos.application;

import kitchenpos.domain.MenuEntity;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProductEntity;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.NotFoundMenuGroupException;
import kitchenpos.domain.NotFoundProductException;
import kitchenpos.domain.ProductEntity;
import kitchenpos.domain.ProductRepository;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
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
    public MenuResponse create(final MenuRequest request) {
        validateExistMenuGroup(request);
        MenuEntity menu = createMenu(request);
        return MenuResponse.of(menu);
    }

    private MenuEntity createMenu(MenuRequest request) {
        MenuEntity menu = menuRepository.save(request.toMenu());
        menu.addMenuProducts(toMenuProducts(request));
        return menu;
    }

    private List<MenuProductEntity> toMenuProducts(MenuRequest request) {
        List<MenuProductEntity> menuProducts = new ArrayList<>(request.getMenuProducts().size());
        for (MenuProductRequest menuProductRequest : request.getMenuProducts()) {
            ProductEntity product = findProductById(menuProductRequest);
            menuProducts.add(menuProductRequest.toMenuProduct(product));
        }
        return menuProducts;
    }

    private ProductEntity findProductById(MenuProductRequest menuProductRequest) {
        return productRepository.findById(menuProductRequest.getProductId()).orElseThrow(NotFoundProductException::new);
    }

    private void validateExistMenuGroup(MenuRequest request) {
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new NotFoundMenuGroupException();
        }
    }

    public List<MenuResponse> list() {
        return MenuResponse.of(menuRepository.findAll());
    }
}
