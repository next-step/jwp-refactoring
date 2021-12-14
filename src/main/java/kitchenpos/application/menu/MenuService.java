package kitchenpos.application.menu;

import kitchenpos.application.product.ProductService;
import kitchenpos.domain.Price;
import kitchenpos.exception.menu.NotCorrectMenuPriceException;
import kitchenpos.exception.menu.NotFoundMenuException;
import kitchenpos.exception.menu.NotFoundMenuGroupException;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.menu.MenuDto;
import kitchenpos.dto.menu.MenuProductDtos;

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
        final ProductService productService
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productService = productService;
    }

    @Transactional
    public MenuDto create(final MenuDto menu) {
        validationOfCreate(menu);

        MenuGroup menuGroup = menuGroupRepository.findById(menu.getMenuGroupId()).orElseThrow(NotFoundMenuGroupException::new);
        
        Menu newMenu = Menu.of(menu.getName(), Price.of(menu.getPrice()), menuGroup);
        mappingMenuProduct(newMenu, MenuProductDtos.of(menu.getMenuProducts()));

        return MenuDto.of(menuRepository.save(newMenu));
    }

    private void mappingMenuProduct(Menu newMenu, MenuProductDtos menuProductDtos) {
        List<Long> productIds = menuProductDtos.getProductIds();
        List<Product> products = productService.findAllByIds(productIds);
        menuProductDtos.createMenuProduct(newMenu, products);
    }

    private void validationOfCreate(final MenuDto menu) {
        checkMenuPrice(Price.of(menu.getPrice()), MenuProductDtos.of(menu.getMenuProducts()));
    }

    private void checkMenuPrice(final Price menuPrice, final MenuProductDtos menuProductDtos) {
        List<Long> productIds = menuProductDtos.getProductIds(); 
        List<Product> products = productService.findAllByIds(productIds);

        Price sumOfProductsPrice = menuProductDtos.getSumProductPrice(products);
        
        if (menuPrice.compareTo(sumOfProductsPrice) > 0) {
            throw new NotCorrectMenuPriceException();
        }
    }

    public List<MenuDto> list() {
        return menuRepository.findAll().stream()
                            .map(MenuDto::of)
                            .collect(Collectors.toList());
    }

    public Menu findById(Long menuId) {
        return menuRepository.findById(menuId).orElseThrow(NotFoundMenuException::new);
    }

    public long countByIdIn(List<Long> menuIds) {
        return menuRepository.countByIdIn(menuIds);
    }

    public List<Menu> findAllByIdIn(List<Long> menuIds) {
        return menuRepository.findAllByIdIn(menuIds);
    }
}
