package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.domain.Products;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Transactional
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
    public Menu create(final MenuRequest menuRequest) {
        MenuProducts menuProducts = new MenuProducts(menuRequest.getMenuProducts());
        Products products = new Products(productRepository.findAllById(menuProducts.toMenuProductIds()));
        Long sum = products.calculateSumPrice(menuRequest.getMenuProducts());

        return menuRepository.save(new Menu(menuRequest.getName(), BigDecimal.valueOf(menuRequest.getPrice()),
                findMenuGroup(menuRequest), sum, menuRequest.getMenuProducts()));
    }

    private MenuGroup findMenuGroup(MenuRequest menuRequest) {
        return menuGroupRepository.findById(menuRequest.getMenuGroupId()).orElseThrow(() -> new IllegalArgumentException());
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
