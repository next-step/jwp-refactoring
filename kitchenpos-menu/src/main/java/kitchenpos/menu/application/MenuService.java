package kitchenpos.menu.application;

import static java.util.stream.Collectors.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.exception.NotExistIdException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;

@Transactional(readOnly = true)
@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
        final MenuRepository menuRepository,
        final MenuGroupRepository menuGroupRepository,
        final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        MenuGroup menuGroup = findMenuGroup(request.getMenuGroupId());
        Menu menu = new Menu(request.getPrice(), request.getName(), menuGroup,
            toMenuProducts(request.getMenuProducts()));
        return MenuResponse.of(menuRepository.save(menu));
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
            .stream()
            .map(MenuResponse::of)
            .collect(toList());
    }

    private List<MenuProduct> toMenuProducts(List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
            .map(pair -> new MenuProduct(findProduct(pair.getProductId()), pair.getQuantity()))
            .collect(toList());
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id)
            .orElseThrow(NotExistIdException::new);
    }

    private MenuGroup findMenuGroup(Long id) {
        return menuGroupRepository.findById(id)
            .orElseThrow(NotExistIdException::new);
    }
}
