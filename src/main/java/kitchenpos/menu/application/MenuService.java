package kitchenpos.menu.application;

import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.menu.domain.*;
import kitchenpos.domain.Price;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
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
        List<MenuProduct> menuProductList = new ArrayList<>();

        Price sumPrice = Price.create(BigDecimal.ZERO);
        for (MenuProductRequest menuProductRequest : menuRequest.getMenuProducts()) {
            Product product = getProduct(menuProductRequest.getProductId());

            MenuProduct menuProduct = MenuProduct.create(null, product, menuProductRequest.getQuantity());
            MenuProduct createdMenuProduct = menuProductRepository.save(menuProduct);

            menuProductList.add(createdMenuProduct);
            sumPrice.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity())));
        }

        MenuGroup menuGroup = getMenuGroup(menuRequest.getMenuGroupId());
        Menu menu = Menu.create(
                menuRequest.getName(),
                menuRequest.getPrice(),
                menuGroup,
                new MenuProducts(menuProductList),
                new MenuValidator(sumPrice));
        return MenuResponse.from(menuRepository.save(menu));
    }

    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAllJoinFetch();
        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }

    private MenuGroup getMenuGroup(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId).orElseThrow(() -> new EntityNotFoundException("MenuGroup", menuGroupId));
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("Product", productId));
    }

}
