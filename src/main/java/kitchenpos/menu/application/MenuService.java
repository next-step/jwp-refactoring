package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
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
        List<MenuProduct> menuProducts = menuRequest.getMenuProducts()
                .stream()
                .map(menuProductRequest -> new MenuProduct(findByProduct(menuProductRequest.getProductId()), menuProductRequest.getQuantity()))
                .collect(Collectors.toList());

        return new MenuProducts(menuProducts);
    }

    private Product findByProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 상품입니다."));
    }

    public List<MenuResponse> list() {
        List<MenuResponse> menuResponses = menuRepository.findAll()
                .stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
        return menuResponses;
    }
}
