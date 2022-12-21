package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;
    private final MenuProductRepository menuProductRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository, ProductRepository productRepository,
                       MenuProductRepository menuProductRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
        this.menuProductRepository = menuProductRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        final List<Long> productsIds = menuRequest.getMenuProducts().stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());
        final List<MenuProduct> menuProducts = productRepository.findAllById(productsIds).stream()
                .map(product -> new MenuProduct(product, menuRequest.getQuantityByProductId(product.getId())))
                .collect(Collectors.toList());

        final MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId()).orElseThrow(NoResultException::new);
        final Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup);
        final Menu savedMenu = menuRepository.save(menu);

        savedMenu.addMenuProducts(menuProducts);
        menuProductRepository.saveAll(savedMenu.getMenuProducts());

        return MenuResponse.of(savedMenu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }
}
