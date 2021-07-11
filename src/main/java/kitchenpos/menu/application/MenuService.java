package kitchenpos.menu.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.presentation.dto.MenuProductRequest;
import kitchenpos.menu.presentation.dto.MenuRequest;
import kitchenpos.menu.presentation.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
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
    public MenuResponse create(final MenuRequest menuRequest) {
        final BigDecimal price = menuRequest.getPrice();
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId()).orElseThrow(IllegalArgumentException::new);
        Menu menu = Menu.of(menuRequest.getName(), menuRequest.getPrice(), menuGroup);

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductRequest menuProductRequest : menuRequest.getMenuProductRequests()) {
            final Product product = productRepository.findById(menuProductRequest.getProductId()).orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity())));
            menu.addMenuProduct(MenuProduct.of(product, menuProductRequest.getQuantity()));
        }
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        menu.getMenuProducts()
                .forEach(menuProduct -> menuProduct.setMenu(menu));
        return MenuResponse.of(menuRepository.save(menu));
    }

    public List<MenuResponse> list() {
        return MenuResponse.ofList(menuRepository.findAll());
    }
}
