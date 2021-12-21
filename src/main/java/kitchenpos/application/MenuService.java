package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.exception.MenuGroupNotFoundException;
import kitchenpos.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
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
        MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId()).orElseThrow(MenuGroupNotFoundException::new);

        final List<MenuProduct> menuProducts = request.getMenuProducts()
                .stream()
                .map(it -> {
                    final Product product = productRepository.findById(it.getProductId())
                            .orElseThrow(ProductNotFoundException::new);
                    return new MenuProduct(product, it.getQuantity());
                })
                .collect(Collectors.toList());

        final Menu menu = menuRepository.save(request.toEntity(menuGroup, menuProducts));
        return MenuResponse.of(menu);
    }

    public List<MenuResponse> list() {
        return MenuResponse.ofList(menuRepository.findAllJoinFetch());
    }
}
