package kitchenpos.menu.application;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuEntity;
import kitchenpos.menu.domain.MenuGroupEntity;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProductEntity;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.ProductEntity;
import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.menu.domain.request.MenuProductRequest;
import kitchenpos.menu.domain.request.MenuRequest;
import kitchenpos.menu.domain.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {
    private static final int FIRST_INDEX = 0;

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
        MenuGroupEntity menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
            .orElseThrow(IllegalArgumentException::new);

        MenuEntity menu = MenuEntity.of(menuRequest.getName(), menuRequest.getPrice(), menuGroup);
        List<MenuProductEntity> menuProducts = convertToEntityIncludeProduct(menuRequest.getMenuProductRequests());
        menu.registerMenuProducts(menuProducts);

        MenuEntity savedMenu = menuRepository.save(menu);
        return MenuResponse.of(savedMenu);
    }

    private List<MenuProductEntity> convertToEntityIncludeProduct(List<MenuProductRequest> menuProductRequests) {
        List<Long> productIds = menuProductRequests.stream()
            .map(MenuProductRequest::getProductId)
            .collect(toList());

        List<ProductEntity> products = productRepository.findByIdIn(productIds);
        Map<Long, List<ProductEntity>> productMap = products.stream()
            .collect(groupingBy(ProductEntity::getId));

        return menuProductRequests.stream()
            .map(it -> MenuProductEntity.of(productMap.get(it.getProductId()).get(FIRST_INDEX) , it.getQuantity()))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<MenuEntity> menus = menuRepository.findAllMenuAndProducts();

        return menus.stream()
            .map(MenuResponse::of)
            .collect(toList());
    }
}
