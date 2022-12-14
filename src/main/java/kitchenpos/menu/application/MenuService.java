package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.exception.NotFoundMenuGroupException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.exception.NotFoundProductException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    private static final Logger log = LoggerFactory.getLogger(MenuService.class);


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
        Menu menu = createMenu(request);
        return MenuResponse.of(menu);
    }

    private Menu createMenu(MenuRequest request) {
        Menu menu = menuRepository.save(request.toMenu());
        menu.addMenuProducts(toMenuProducts(request));
        log.info("call tx");
        boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
        log.info("tx active = {}", txActive);
        return menu;
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
                .orElseThrow(NotFoundProductException::new);
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

