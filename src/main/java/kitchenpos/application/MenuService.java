package kitchenpos.application;

import kitchenpos.domain.MenuEntity;
import kitchenpos.domain.MenuProductEntity;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupService menuGroupService,
            final ProductService productService
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        List<MenuProductEntity> menuProducts = request.getMenuProducts().stream()
                .map(menuProduct ->
                        new MenuProductEntity(
                                productService.findProductById(menuProduct.getId()),
                                menuProduct.getQuantity()
                        )
                ).collect(Collectors.toList());
        MenuEntity persistMenu = menuRepository.save(
                MenuEntity.createMenu(
                        request.getName(),
                        request.getPrice(),
                        menuGroupService.findMenuGroupById(request.getMenuGroupId()),
                        menuProducts
                )
        );

        return MenuResponse.of(persistMenu);
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(menu -> MenuResponse.of(menu))
                .collect(Collectors.toList());
    }
}
