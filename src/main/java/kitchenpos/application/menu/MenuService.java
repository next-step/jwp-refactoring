package kitchenpos.application.menu;

import kitchenpos.domain.Price;
import kitchenpos.exception.menu.NotFoundMenuException;
import kitchenpos.exception.menu.NotFoundMenuGroupException;
import kitchenpos.vo.ProductId;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.dto.menu.MenuDto;
import kitchenpos.dto.menu.MenuProductDto;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuValidator menuValidator;

    public MenuService(
        final MenuRepository menuRepository,
        final MenuGroupRepository menuGroupRepository,
        final MenuValidator menuValidator
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuDto create(final MenuDto menuDto) {
        MenuGroup menuGroup = menuGroupRepository.findById(menuDto.getMenuGroupId()).orElseThrow(NotFoundMenuGroupException::new);
        MenuProducts menuProducts = createMenuProducts(menuDto.getMenuProducts());

        Menu newMenu = Menu.of(menuDto.getName(), Price.of(menuDto.getPrice()), menuGroup, menuProducts);

        menuValidator.validateForCreate(newMenu);

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
