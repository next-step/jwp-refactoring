package kitchenpos.menu.application;

import common.exception.NoSuchDataException;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private MenuRepository menuRepository;
    private MenuGroupRepository menuGroupRepository;
    private ProductRepository productRepository;

    public MenuService(
            MenuRepository menuRepository,
            MenuGroupRepository menuGroupRepository,
            ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {

        MenuGroup menuGroup = findMenuGroupById(menuRequest.getMenuGroupId());
        Menu menu = new Menu(menuRequest.getName(), new BigDecimal(menuRequest.getPrice()), menuGroup);
        List<MenuProduct> menuProducts = generateMenuProducts(menuRequest, menu);
        menu.addMenuProducts(menuProducts);
        final Menu savedMenu = menuRepository.save(menu);
        System.out.println(savedMenu == null);
        return MenuResponse.of(savedMenu);
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    private List<MenuProduct> generateMenuProducts(MenuRequest menuRequest, Menu menu) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuRequest.getMenuProducts().stream()
                .forEach(
                        menuProduct -> {
                            Product product = findProduct(menuProduct.getProductId());
                            menuProducts.add(new MenuProduct(menu, product, menuProduct.getQuantity()));
                        }
                );

        return menuProducts;
    }

    private MenuGroup findMenuGroupById(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId).orElseThrow(NoSuchDataException::new);
    }

    private Product findProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(NoSuchDataException::new);
    }
}
