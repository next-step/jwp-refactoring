package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
        this.menuGroupRepository = menuGroupRepository;
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        Long menuGroupId = request.getMenuGroupId();
        MenuGroup menuGroup = menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("%d에 해당하는 메뉴그룹을 찾을 수 없습니다.", menuGroupId)));
        MenuProducts menuProducts = MenuProducts.from(findAllMenuProductsByProductId(request.getMenuProductsRequest()));
        Menu menu = Menu.of(request.getName(), request.getPrice(), menuGroup, menuProducts);

        return MenuResponse.from(menuRepository.save(menu));
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }

    private List<MenuProduct> findAllMenuProductsByProductId(List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
                .map(menuProductRequest -> menuProductRequest.toMenuProduct(findProductById(menuProductRequest.getProductId())))
                .collect(Collectors.toList());
    }

    private Product findProductById(final Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + "에 해당하는 상품을 찾을 수 없습니다."));
    }
}
