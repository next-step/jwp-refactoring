package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.exception.NotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final ProductRepository productRepository;
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuService(final ProductRepository productRepository,
        final MenuRepository menuRepository,
        final MenuGroupRepository menuGroupRepository) {
        this.productRepository = productRepository;
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {

        final MenuGroup menuGroup = findMenuGroup(menuRequest.getMenuGroupId());
        final Menu menu = menuRequest.toEntity(menuGroup);

        menu.addMenuProducts(createMenuProducts(menuRequest.getMenuProducts()));

        final Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.of(savedMenu);
    }

    public List<MenuResponse> list() {
        return MenuResponse.ofList(menuRepository.findAll());
    }

    private MenuGroup findMenuGroup(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
            .orElseThrow(() -> new NotFoundException("해당하는 메뉴그룹을 찾을 수 없습니다."));
    }

    private Product findProduct(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new NotFoundException("해당하는 상품이 없습니다."));
    }

    private List<MenuProduct> createMenuProducts(List<MenuProductRequest> requests) {
        return requests.stream()
            .map(it -> it.toEntity(findProduct(it.getProductId())))
            .collect(Collectors.toList());
    }
}
