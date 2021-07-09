package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static java.util.stream.Collectors.toList;

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
    public MenuResponse create(final MenuRequest menuRequest) {
        List<MenuProduct> menuProducts = menuRequest.getMenuProducts().stream()
                .map(menuProduct -> new MenuProduct(productRepository.findById(menuProduct.getProductId())
                        .orElseThrow(EntityNotFoundException::new), menuProduct.getQuantity()))
                .collect(toList());

        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(),
                menuGroupRepository.findById(menuRequest.getMenuGroupId()).orElseThrow(EntityNotFoundException::new));
        menu.addMenuProducts(menuProducts);
        menuProducts.forEach(menuProductRepository::save);
        return MenuResponse.from(menuRepository.save(menu));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::from)
                .collect(toList());
    }
}
