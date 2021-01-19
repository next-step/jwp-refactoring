package kitchenpos.menu.application;

import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class MenuService {
    private final MenuGroupRepository menuGroupRepository;
    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuGroupRepository menuGroupRepository, final MenuRepository menuRepository, final ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        final Price requestMenuPrice = Price.of(new BigDecimal(menuRequest.getPrice()));
        final MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
            .orElseThrow(NotFoundException::new);
        final MenuProducts menuProducts = generateMenuProducts(menuRequest);

        requestMenuPrice.isMustBeLessThanAllMenuProductsPrice(Price.of(menuProducts.getAllMenuProductsPrice()));

        return new MenuResponse(menuRepository.save(Menu.of(menuRequest.getName(), requestMenuPrice, menuGroup, menuProducts)));
    }

    private MenuProducts generateMenuProducts(final MenuRequest menuRequest) {
        final List<MenuProduct> menuProductList = menuRequest.getProducts().stream()
            .map(product -> {
                final Product foundProduct = productRepository.findById(product.getId())
                    .orElseThrow(NotFoundException::new);
                return MenuProduct.of(foundProduct, product.getQuantity());
            })
            .collect(toList());

        return MenuProducts.of(menuProductList);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> findAllMenus() {
        return menuRepository.findAll().stream()
            .map(MenuResponse::new)
            .collect(toList());
    }
}
