package kitchenpos.menu.application;

import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.exception.MenuGroupNotFoundException;
import kitchenpos.menu.exception.ProductNotFoundException;
import kitchenpos.menu.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuService(MenuRepository menuRepository, ProductRepository productRepository, MenuGroupRepository menuGroupRepository) {
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(MenuGroupNotFoundException::new);
        final List<MenuProduct> menuProducts = toMenuProducts(request);
        final Menu menu = menuRepository.save(request.toEntity(menuGroup, menuProducts));
        return MenuResponse.of(menu);
    }

    private List<MenuProduct> toMenuProducts(MenuRequest request) {
        return request.getMenuProducts()
                .stream()
                .map(it -> {
                    final Product product = productRepository.findById(it.getProductId())
                            .orElseThrow(ProductNotFoundException::new);
                    return new MenuProduct(product, it.getQuantity());
                })
                .collect(Collectors.toList());
    }

    public List<MenuResponse> list() {
        return MenuResponse.ofList(menuRepository.findAllJoinFetch());
    }
}
