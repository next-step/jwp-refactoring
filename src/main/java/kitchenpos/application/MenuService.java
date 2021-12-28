package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.product.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.dto.MenuResponses;
import kitchenpos.exception.KitchenposNotFoundException;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.product.domain.ProductRepository;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
        final MenuRepository menuRepository,
        final MenuGroupRepository menuGroupRepository,
        final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
            .orElseThrow(KitchenposNotFoundException::new);

        MenuProducts menuProducts = makeMenuProducts(menuRequest.getMenuProducts());
        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup, menuProducts);

        final Menu savedMenu = menuRepository.save(menu);

        return MenuResponse.from(savedMenu);
    }

    private MenuProducts makeMenuProducts(List<MenuProductRequest> menuProductRequests) {
        return new MenuProducts(menuProductRequests.stream()
            .map(menuProductRequest -> {
                Product product = productRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(KitchenposNotFoundException::new);
                return new MenuProduct(product, menuProductRequest.getQuantity());
            })
            .collect(Collectors.toList()));
    }

    public MenuResponses list() {
        final List<Menu> menus = menuRepository.findAll();
        return MenuResponses.from(menus);
    }
}
