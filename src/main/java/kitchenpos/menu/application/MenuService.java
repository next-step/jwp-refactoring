package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private ProductRepository productRepository;
    private MenuRepository menuRepository;
    private MenuGroupRepository menuGroupRepository;

    public MenuService(
            final ProductRepository productRepository,
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository
    ) {
        this.productRepository = productRepository;
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        List<MenuProduct> menuProducts = createMenuProducts(request.getMenuProducts());
        MenuGroup menuGroup = findMenuGroupById(request.getMenuGroupId());
        Menu createdMenu = menuRepository.save(new Menu(request.getName(), request.getPrice(), menuGroup, menuProducts));
        return MenuResponse.of(createdMenu);
    }

    public MenuGroup findMenuGroupById(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId).orElseThrow(IllegalArgumentException::new);

    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    public List<MenuProduct> createMenuProducts(List<MenuProductRequest> menuProducts) {
        List<MenuProduct> list = new ArrayList<>();
        for (final MenuProductRequest menuProduct : menuProducts) {
            final Product product = getProductById(menuProduct.getProductId());
            list.add(new MenuProduct(menuProduct.getSeq(), product, menuProduct.getQuantity()));
        }
        return list;
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }
}
