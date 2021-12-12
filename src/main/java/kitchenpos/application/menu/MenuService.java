package kitchenpos.application.menu;

import kitchenpos.application.product.ProductService;
import kitchenpos.domain.Price;
import kitchenpos.dto.MenuDto;
import kitchenpos.dto.MenuProductDto;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProductRepository;
import kitchenpos.domain.menu.MenuRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductService productService;

    public MenuService(
        final MenuRepository menuRepository,
        final MenuGroupRepository menuGroupRepository,
        final MenuProductRepository menuProductRepository,
        final ProductService productService
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productService = productService;
    }

    @Transactional
    public MenuDto create(final MenuDto menu) {
        validationOfCreate(menu);

        MenuGroup menuGroup = menuGroupRepository.findById(menu.getMenuGroupId()).orElseThrow(IllegalArgumentException::new);
        
        Menu newMenu = Menu.of(menu.getName(), Price.of(menu.getPrice()), menuGroup);

        mappingMenuProduct(newMenu, menu.getMenuProducts());

        Menu savedMenu = menuRepository.save(newMenu);

        return MenuDto.of(savedMenu);
    }

    private void mappingMenuProduct(Menu newMenu, List<MenuProductDto> menuProductDtos) {
        for (MenuProductDto menuProductDto : menuProductDtos) {
            MenuProduct menuProduct = MenuProduct.of(null, productService.findById(menuProductDto.getProductId()), menuProductDto.getQuantity());
            menuProduct.acceptMenu(newMenu);
        }
    }

    private void validationOfCreate(final MenuDto menu) {
        checkMenuPrice(Price.of(menu.getPrice()), menu.getMenuProducts());
    }

    private void checkMenuPrice(final Price menuPrice, final List<MenuProductDto> menuProducts) {
        Price sumOfProductsPrice = productService.sumOfPrices(menuProducts);

        if (menuPrice.compareTo(sumOfProductsPrice) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public List<MenuDto> list() {
        return menuRepository.findAll().stream()
                            .map(MenuDto::of)
                            .collect(Collectors.toList());
    }

    public Menu findById(Long menuId) {
        return menuRepository.findById(menuId).orElseThrow(IllegalArgumentException::new);
    }

    public long countByIdIn(List<Long> menuIds) {
        return menuRepository.countByIdIn(menuIds);
    }
}
