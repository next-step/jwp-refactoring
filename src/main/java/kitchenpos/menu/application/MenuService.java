package kitchenpos.menu.application;

import java.util.stream.Collectors;
import kitchenpos.common.constant.ErrorCode;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.validator.MenuValidator;
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

    private final ProductRepository productRepository;

    private final MenuValidator menuValidator;

    public MenuService(
            final MenuRepository menuRepository,
            final ProductRepository productRepository,
            final MenuValidator menuValidator
    ) {
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(MenuRequest menuRequest) {
        menuValidator.validate(menuRequest);
        List<Product> products = findAllProductByIds(menuRequest.getMenuProductIds());
        Menu menu = menuRepository.save(menuRequest.toMenu(products));
        return MenuResponse.from(menu);
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
            .stream()
            .map(MenuResponse::from)
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
