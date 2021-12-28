package kitchenpos.menu.application;

import kitchenpos.menu.exception.NotFoundMenuException;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroupId;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.domain.ProductId;
import kitchenpos.menu.dto.MenuDto;
import kitchenpos.menu.dto.MenuProductDto;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final MenuValidator menuValidator;

    public MenuService(
        final MenuRepository menuRepository,
        final MenuGroupService menuGroupService,
        final MenuValidator menuValidator
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuDto create(final MenuDto menuDto) {
        MenuGroup menuGroup = menuGroupService.findById(menuDto.getMenuGroupId());
        MenuProducts menuProducts = createMenuProducts(menuDto.getMenuProducts());

        Menu newMenu = Menu.of(menuDto.getName(), Price.of(menuDto.getPrice()), MenuGroupId.of(menuGroup.getId()), menuProducts, menuValidator);

        return MenuDto.of(menuRepository.save(newMenu));
    }

    private MenuProducts createMenuProducts(List<MenuProductDto> menuProductDtos) {
        return MenuProducts.of(menuProductDtos.stream()
                                                .map(menuProductDto -> MenuProduct.of(ProductId.of(menuProductDto.getProductId()), menuProductDto.getQuantity()))
                                                .collect(Collectors.toList())
                             );
    }

    public List<MenuDto> list() {
        return menuRepository.findAll().stream()
                            .map(MenuDto::of)
                            .collect(Collectors.toList());
    }

    public Menu findById(Long menuId) {
        return menuRepository.findById(menuId).orElseThrow(NotFoundMenuException::new);
    }

    public List<Menu> findAllByIdIn(List<Long> menuIds) {
        return menuRepository.findAllByIdIn(menuIds);
    }
}
