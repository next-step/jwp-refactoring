package kitchenpos.menu.application;

import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.exception.EntityNotFoundExceptionConstants;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
                       ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(MenuRequest request) {
        MenuGroup menuGroup = findMenuGroupById(request.getMenuGroupId());
        List<Product> products = findAllProductByIds(request.findAllProductIds());
        Menu menu = request.toMenu(menuGroup, products);

        return MenuResponse.of(menuRepository.save(menu));
    }

    private MenuGroup findMenuGroupById(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundExceptionConstants.NOT_FOUND_BY_ID));
    }

    private List<Product> findAllProductByIds(List<Long> productIds) {
        List<Product> products = productRepository.findAllById(productIds);
        if (productIds.size() != products.size()) {
            throw new EntityNotFoundException(EntityNotFoundExceptionConstants.NOT_FOUND_BY_ID);
        }

        return products;
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return MenuResponse.list(menuRepository.findAll());
    }
}
