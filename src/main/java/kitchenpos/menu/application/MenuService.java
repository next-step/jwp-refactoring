package kitchenpos.menu.application;

import static kitchenpos.common.exception.ExceptionMessage.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.repository.ProductRepository;

@Service
public class MenuService {
    private final MenuValidator menuValidator;
    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuValidator menuValidator, MenuRepository menuRepository,
        ProductRepository productRepository) {
        this.menuValidator = menuValidator;
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        Menu menu = menuRequest.toEntity();
        menuValidator.validateCreate(menu);

        addMenuProduct(menuRequest.getMenuProducts(), menu);
        menu.validateMenuPrice();

        return new MenuResponse(menuRepository.save(menu));
    }

    private void addMenuProduct(List<MenuProductRequest> menuProducts, Menu menu) {
        for (final MenuProductRequest menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_DATA));
            menu.addMenuProduct(menuProduct.toEntity(menu, product));
        }
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findMenus();
        return menus.stream()
            .map(MenuResponse::new)
            .collect(Collectors.toList());
    }
}
