package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.persistence.MenuGroupRepository;
import kitchenpos.menu.persistence.MenuProductRepository;
import kitchenpos.menu.persistence.MenuRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.exception.ProductException;
import kitchenpos.product.persistence.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.product.exception.ProductExceptionType.NONE_EXISTS_PRODUCT;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);
        List<Product> menuProducts = findAllProductByIds(menuRequest.getMenuProductIds());
        Menu savedMenu = menuRepository.save(menuRequest.toMenu(menuGroup, menuProducts));
        return MenuResponse.of(savedMenu);
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    private List<Product> findAllProductByIds(List<Long> productIds) {
        List<Product> products = productRepository.findAllById(productIds);
        if (productIds.size() != products.size()) {
            throw new ProductException(NONE_EXISTS_PRODUCT);
        }
        return products;
    }
}
