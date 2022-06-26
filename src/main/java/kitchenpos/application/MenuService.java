package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menuGroup.MenuGroup;
import kitchenpos.domain.menuGroup.MenuGroupRepository;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
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
        MenuGroup menuGroup = findMenuGroup(menuRequest.getMenuGroupId());

        Menu menu = menuRequest.toMenu(menuGroup);
        menu.bindMenuProducts();

        List<Long> productIds = menu.getMenuProducts().findProductIds();
        extractedProducts(productIds);

        return MenuResponse.of(saveMenu(menu));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = findMenus();

        return menus.stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    private void extractedProducts(List<Long> productIds) {
        if (!productRepository.existsAllByIdIn(productIds)) {
            throw new IllegalArgumentException("잘못된 상품 정보 입니다.");
        }
    }

    private MenuGroup findMenuGroup(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(NoSuchElementException::new);
    }

    private Menu saveMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    private List<Menu> findMenus() {
        return menuRepository.findAll();
    }
}
