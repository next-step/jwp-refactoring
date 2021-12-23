package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.domain.dto.MenuProductRequest;
import kitchenpos.domain.dto.MenuRequest;
import kitchenpos.domain.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository, final MenuGroupRepository menuGroupRepository,
                       final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        Optional<MenuGroup> menuGroup = menuGroupRepository.findById(request.getMenuGroupId());
        if (!menuGroup.isPresent()) {
            throw new IllegalArgumentException();
        }

        List<MenuProduct> menuProducts = getMenuProducts(request);
        Menu menu = request.toEntity(menuGroup.get(), menuProducts);

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            Product product = menuProduct.getProduct();
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (menu.getPrice().compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        Menu save = menuRepository.save(menu);

        return MenuResponse.of(save);
    }

    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAll();
        return MenuResponse.ofList(menus);
    }

    private List<MenuProduct> getMenuProducts(MenuRequest request) {
        return request.getMenuProducts().stream()
                .map(this::getMenuProduct)
                .collect(Collectors.toList());
}

    private MenuProduct getMenuProduct(MenuProductRequest request) {
        return request.toEntity(getProduct(request.getProductId()));
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(IllegalArgumentException::new);
    }
}