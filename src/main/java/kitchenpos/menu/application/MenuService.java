package kitchenpos.menu.application;

import static kitchenpos.common.exception.ExceptionMessage.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.repository.ProductRepository;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
        MenuProductRepository menuProductRepository, ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup findMenuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_DATA));

        Menu menu = menuRequest.toEntity(findMenuGroup);
        for (final MenuProductRequest menuProduct : menuRequest.getMenuProducts()) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_DATA));
            menu.addMenuProduct(menuProduct.toEntity(menu, product));
        }
        menu.validateMenuPrice();

        return new MenuResponse(menuRepository.save(menu));
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream()
            .map(MenuResponse::new)
            .collect(Collectors.toList());
    }
}
