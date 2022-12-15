package kitchenpos.menu.application;

import java.util.stream.Collectors;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public MenuResponse create(MenuRequest menuRequest) {
        MenuGroup menuGroup = findMenuGroupById(menuRequest.getMenuGroupId());
        List<Product> products = findAllProductByIds(menuRequest.getMenuProductIds());
        return MenuResponse.from(menuRepository.save(menuRequest.toMenu(menuGroup, products)));
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
            .stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList());
    }

    private MenuGroup findMenuGroupById (Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
            .orElseThrow(() -> new NotFoundException());
    }

    private List<MenuProduct> findAllMenuProductsByProductId(List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
            .map(menuProductRequest -> menuProductRequest.toMenuProduct(findProductById(menuProductRequest.getProductId())))
            .collect(Collectors.toList());
    }

    private List<Product> findAllProductByIds(List<Long> ids) {
        return ids.stream()
            .map(this::findProductById)
            .collect(Collectors.toList());
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new NotFoundException());
    }
}
