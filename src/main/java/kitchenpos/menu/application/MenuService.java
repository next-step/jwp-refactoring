package kitchenpos.menu.application;

import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.product.domain.Product;
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
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        validate(request);

        Menu menu = menuRepository.save(request.toMenu());
        menuRepository.flush();
        return MenuResponse.of(menu);
    }

    private void validate(MenuRequest request) {
        validateMenuGroupExistsById(request);
        request.validate(findProductIds(request));
    }

    private void validateMenuGroupExistsById(MenuRequest request) {
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    private List<Product> findProductIds(MenuRequest request) {
        return productRepository.findByIdIn(request.getProductIds());
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }
}
