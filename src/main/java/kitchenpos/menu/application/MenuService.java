package kitchenpos.menu.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProductAmount;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuDto;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final ProductService productService;

    public MenuService(
            final MenuRepository menuRepository,
            final ProductService productService
    ) {
        this.menuRepository = menuRepository;
        this.productService = productService;
    }

    @Transactional
    public MenuDto create(final MenuDto menuDto) {
        Menu menu = menuDto.toMenu();
        MenuProducts menuProducts = menu.getMenuProducts();
        List<Long> productIds = menuProducts.productIds();
        List<Product> products = productService.findAllProductByIds(productIds);
        List<MenuProductAmount> menuProductAmounts = products.stream()
                .map(product -> {
                    long quantity = menuProducts.findQuantityByProductId(product.getId());
                    return new MenuProductAmount(quantity, product.getPrice());
                }).collect(toList());
        menu.checkSumPriceOfProducts(menuProductAmounts);
        return MenuDto.of(menuRepository.save(menu));
    }

    public List<MenuDto> list() {
        List<Menu> menus = menuRepository.findAll();
        return menus.stream().map(MenuDto::of).collect(toList());
    }
}
