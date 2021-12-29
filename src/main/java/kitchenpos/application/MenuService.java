package kitchenpos.application;

import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuProductRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        List<Product> products = getValidProducts(menuRequest);

        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);

        Menu menu = Menu.of(menuRequest.getName(), menuRequest.getPrice(), menuGroup, createMenuProduct(menuRequest, products));
        return MenuResponse.of(menuRepository.save(menu));
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(menu -> MenuResponse.of(menu))
                .collect(Collectors.toList());
    }

    private List<MenuProduct> createMenuProduct(MenuRequest menuRequest, List<Product> products) {
        return products.stream()
                .map(product -> menuRequest.createMenuProduct(product))
                .collect(Collectors.toList());
    }

    private List<Product> getValidProducts(MenuRequest menuRequest) {
        List<Long> productIds = menuRequest.getProductIds();
        List<Product> products = productRepository.findAllByIdIn(productIds);
        if (productIds.size() != products.size()) {
            throw new IllegalArgumentException();
        }

        BigDecimal sum = products.stream()
                .map(product -> product.getTotalPrice(menuRequest.getQuantity(product.getId())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (menuRequest.getPrice().compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
        return products;
    }

}
