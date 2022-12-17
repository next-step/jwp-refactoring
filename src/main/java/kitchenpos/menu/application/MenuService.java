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

    private final MenuValidator menuValidator;

    public MenuService(final MenuRepository menuRepository, final MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(MenuRequest menuRequest) {
        menuValidator.validate(menuRequest);
        List<MenuProduct> menuProducts = menuRequest.getMenuProducts()
            .stream()
            .map(MenuProductRequest::toMenuProduct)
            .collect(Collectors.toList());
        Menu menu = menuRepository.save(menuRequest.toMenu(MenuProducts.from(menuProducts)));
        return MenuResponse.from(menu);
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
            .stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList());
    }
}
