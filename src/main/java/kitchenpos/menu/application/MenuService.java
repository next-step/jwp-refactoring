package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
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
        if (!menuGroupRepository.existsById(menuRequest.getMenuGroupId())) {
            throw new IllegalArgumentException("존재하지 않는 메뉴그룹입니다.");
        }

        final MenuProducts menuProducts = getMenuProducts(menuRequest);

        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuRequest.getMenuGroupId(), menuProducts);
        final Menu savedMenu = menuRepository.save(menu);

        return MenuResponse.of(savedMenu);
    }

    private MenuProducts getMenuProducts(MenuRequest menuRequest) {
        List<MenuProductRequest> menuProductRequests = menuRequest.getMenuProducts();

        List<Long> productIds = getProductIds(menuProductRequests);
        List<Product> findProducts = productRepository.findByIdIn(productIds);

        return new MenuProducts(menuProductRequests, findProducts);
    }

    private List<Long> getProductIds(List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream().map(menuProductRequest -> menuProductRequest.getProductId()).collect(Collectors.toList());
    }

    public List<MenuResponse> list() {
        List<MenuResponse> menuResponses = menuRepository.findAll()
                .stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
        return menuResponses;
    }
}
