package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menuGroup.domain.MenuGroup;
import kitchenpos.menuGroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
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
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다."));

        List<MenuProduct> menuProducts = menuRequest.getMenuProductRequests()
                .stream()
                .map(menuProductRequest -> new MenuProduct(productRepository.findById(menuProductRequest.getProductId())
                        .orElseThrow(IllegalArgumentException::new), menuProductRequest.getQuantity()))
                .collect(Collectors.toList());

        final Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup.getId());
        menu.organizeMenu(menuProducts);

        final Menu savedMenu = menuRepository.save(menu);

        return MenuResponse.of(savedMenu);
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(menu -> MenuResponse.of(menu))
                .collect(Collectors.toList());
    }
}
