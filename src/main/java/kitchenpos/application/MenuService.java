package kitchenpos.application;

import kitchenpos.domain.Price;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(
        final MenuRepository menuRepository,
        final MenuGroupRepository menuGroupRepository,
        final MenuProductRepository menuProductRepository,
        final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuDto create(final MenuDto menu) {
        validationOfCreate(menu);

        MenuGroup menuGroup = menuGroupRepository.findById(menu.getMenuGroupId()).orElseThrow(IllegalArgumentException::new);
        
        final Menu savedMenu = menuRepository.save(Menu.of(menu.getName(), Price.of(menu.getPrice()), menuGroup, null));
        final List<MenuProduct> savedMenuProducts = saveMenuProduct(savedMenu, menu.getMenuProducts());

        savedMenu.changeMenuProducts(savedMenuProducts);

        return MenuDto.of(savedMenu);
    }

    private void validationOfCreate(final MenuDto menu) {
        checkMenuPrice(Price.of(menu.getPrice()), menu.getMenuProducts());
    }

    private void checkMenuPrice(final Price menuPrice, final List<MenuProductDto> menuProducts) {
        Price sumOfProductsPrice = sumOfProductPrice(menuProducts);

        if (menuPrice.compareTo(sumOfProductsPrice) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private Price sumOfProductPrice(final List<MenuProductDto> menuProducts) {
        Price sum = Price.of(0);

        for (final MenuProductDto menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                                                        .orElseThrow(IllegalArgumentException::new);

            sum = sum.add(product.getPrice().multiply(menuProduct.getQuantity()));
        }

        return sum;
    }

    private List<MenuProduct> saveMenuProduct(Menu savedMenu,  List<MenuProductDto> menuProducts) {
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();

        for (MenuProductDto menuProductDto : menuProducts) {
            Product product = productRepository.findById(menuProductDto.getProductId()).orElseThrow(IllegalArgumentException::new);
            savedMenuProducts.add(menuProductRepository.save(MenuProduct.of(savedMenu, product, menuProductDto.getQuantity())));
        }

        return savedMenuProducts;
    }

    public List<MenuDto> list() {
        final List<Menu> menus = menuRepository.findAll();

        for (final Menu menu : menus) {
            menu.changeMenuProducts(menuProductRepository.findAllByMenuId(menu.getId()));
        }

        return menus.stream()
                    .map(MenuDto::of)
                    .collect(Collectors.toList());
    }
}
