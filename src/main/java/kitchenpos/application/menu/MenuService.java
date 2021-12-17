package kitchenpos.application.menu;

import kitchenpos.application.product.ProductService;
import kitchenpos.domain.Price;
import kitchenpos.exception.menu.NotFoundMenuException;
import kitchenpos.exception.menu.NotFoundMenuGroupException;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.Products;
import kitchenpos.dto.menu.MenuDto;
import kitchenpos.dto.menu.MenuProductDto;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
        MenuGroup menuGroup = menuGroupRepository.findById(menu.getMenuGroupId()).orElseThrow(NotFoundMenuGroupException::new);
        MenuProducts menuProducts = createMenuProducts(menu.getMenuProducts());

        Menu newMenu = Menu.of(menu.getName(), Price.of(menu.getPrice()), menuGroup, menuProducts);
        
        return MenuDto.of(menuRepository.save(newMenu));
    }

    private MenuProducts createMenuProducts(List<MenuProductDto> menuProductDtos) {
        List<Long> productIds = findProductIds(menuProductDtos);

        Products products = Products.of(productService.findAllByIds(productIds));

        List<MenuProduct> menuProducts = new ArrayList<>();

        for (MenuProductDto menuProductDto : menuProductDtos) {
            Product matchingProduct = products.findById(menuProductDto.getProductId());
            menuProducts.add(MenuProduct.of(matchingProduct, menuProductDto.getQuantity()));
        }

        return MenuProducts.of(menuProducts);
    }

    private List<Long> findProductIds(List<MenuProductDto> menuProductDtos) {
        return menuProductDtos.stream()
                                .map(MenuProductDto::getProductId)
                                .collect(Collectors.toList());
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
