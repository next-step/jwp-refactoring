package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.exception.MenuGroupNotFoundException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.exception.ProductNotFoundException;

@Service
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
    public MenuResponse create(final MenuRequest menuRequest) {
        final BigDecimal price = menuRequest.getPrice();
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId()).orElseThrow(MenuGroupNotFoundException::new);
        Menu menu = menuRepository.save(new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup));
        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProductRequest menuProductRequest : menuRequest.getMenuProductRequests()) {
            Product product = productRepository.findById(menuProductRequest.getProductId()).orElseThrow(ProductNotFoundException::new);
            MenuProduct menuProduct = new MenuProduct(menu, product, menuProductRequest.getQuantity());
            sum = sum.add(menuProduct.multiplyProductPriceByQuantity());
            menu.addMenuProduct(menuProduct);
        }
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
        return MenuResponse.of(menu);
    }

    public List<MenuResponse> findAllMenu() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }
}
