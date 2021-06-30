package kitchenpos.application;

import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuGroupRepository menuGroupRepository;
    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuGroupRepository menuGroupRepository,
            final MenuRepository menuRepository,
            final ProductRepository productRepository
    ) {
        this.menuGroupRepository = menuGroupRepository;
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        final MenuGroup menuGroup = findMenuGroup(menuRequest);
        final List<Product> products = findProducts(menuRequest);
        final Menu persistMenu = menuRepository.save(menuRequest.toEntityWith(menuGroup, products));
        return MenuResponse.of(persistMenu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return MenuResponse.listOf(menus);
    }

    private MenuGroup findMenuGroup(MenuRequest menuRequest) {
        return menuGroupRepository.findById(menuRequest.getMenuGroupId())
            .orElseThrow(() -> new IllegalArgumentException("메뉴 그룹이 존재하지 않는 메뉴는 등록할 수 없습니다."));
    }

    private List<Product> findProducts(final MenuRequest menuRequest) {
        final List<MenuProductRequest> menuProductRequests = menuRequest.getMenuProducts();

        final List<Long> productIds = menuProductRequests.stream()
            .map(MenuProductRequest::getProductId)
            .collect(Collectors.toList());

        final List<Product> products = productRepository.findAllById(productIds);

        if (products.size() != productIds.size()) {
            throw new IllegalArgumentException("상품으로 등록되지 않은 메뉴는 등록할 수 없습니다.");
        }

        return products;
    }
}
