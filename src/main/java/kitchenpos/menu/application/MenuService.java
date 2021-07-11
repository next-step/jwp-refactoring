package kitchenpos.menu.application;

import kitchenpos.common.valueobject.Price;
import kitchenpos.common.valueobject.Quantity;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.presentation.dto.MenuProductRequest;
import kitchenpos.menu.presentation.dto.MenuRequest;
import kitchenpos.menu.presentation.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

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
        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId()).orElseThrow(IllegalArgumentException::new);
        Menu menu = Menu.of(menuRequest.getName(), menuRequest.getPrice(), menuGroup);

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductRequest menuProductRequest : menuRequest.getMenuProductRequests()) {
            final Product product = productRepository.findById(menuProductRequest.getProductId()).orElseThrow(IllegalArgumentException::new);
            Price price = product.getPrice().calculatePriceByQuantity(Quantity.of(menuProductRequest.getQuantity()));
            sum = sum.add(price.getValue());
            menu.addMenuProduct(MenuProduct.of(product, menuProductRequest.getQuantity()));
        }
        if (menu.getPrice().isBiggerThan(sum)) {
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
