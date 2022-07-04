package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository,
        final MenuGroupRepository menuGroupRepository,
        final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        if (!menuGroupRepository.existsById(menuRequest.getMenuGroupId())) {
            throw new IllegalArgumentException("그룹이 존재하지 않습니다.");
        }
        final Menu menu = Menu.of(menuRequest.getName(), menuRequest.getPrice(), menuRequest.getMenuGroupId(), convertToMenuProduct(menuRequest.getMenuProducts()));
        final Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.of(savedMenu);
    }

    private List<MenuProduct> convertToMenuProduct(List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
            .map(request -> new MenuProduct(productRepository.findById(request.getProductId()).orElseThrow(() -> new IllegalArgumentException()), request.getQuantity())).
            collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return MenuResponse.convertToMenuResponses(menus);
    }
}
