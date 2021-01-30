package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = findMenuGroup(menuRequest.getMenuGroupId());
        List<MenuProduct> menuProducts = findMenuProducts(menuRequest.getMenuProducts());
        Menu menu = menuRequest.toEntity(menuGroup, menuProducts);
        return MenuResponse.of(menuRepository.save(menu));
    }

    private MenuGroup findMenuGroup(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 메뉴 그룹 입니다."));
    }

    private List<MenuProduct> findMenuProducts(List<MenuProductRequest> menuProductRequests) {
        List<MenuProduct> menuProducts = new ArrayList<>();

        List<Long> menuProductIds = menuProductRequests.stream()
                .map(menuProduct -> menuProduct.getProductId())
                .collect(Collectors.toList());

        List<Product> products = productRepository.findAllById(menuProductIds);

        for (MenuProductRequest menuProduct : menuProductRequests) {
            menuProducts.add(products.stream()
                    .filter(menuProduct::isSameProductId)
                    .map(menuProduct::toMenuProduct).findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 상품 입니다.")));
        }

        return menuProducts;
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return MenuResponse.ofList(menuRepository.findAll());
    }
}
