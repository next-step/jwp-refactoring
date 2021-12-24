package kitchenpos.domain.menu;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kitchenpos.application.menugroup.MenuGroupService;
import kitchenpos.application.product.ProductService;
import kitchenpos.domain.Price;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.Products;
import kitchenpos.dto.menu.MenuDto;
import kitchenpos.dto.menu.MenuProductDto;
import kitchenpos.exception.menu.NotCorrectMenuPriceException;
import kitchenpos.exception.product.NotFoundProductException;
import kitchenpos.vo.MenuGroupId;
import kitchenpos.vo.ProductId;

@Component
public class MenuValidator {
    private final ProductService productService;
    private final MenuGroupService menuGroupService;

    public MenuValidator (
        final ProductService productService,
        final MenuGroupService menuGroupService
    ) {
        this.productService = productService;
        this.menuGroupService = menuGroupService;
    }

    public Menu getValidatedMenu(MenuDto menuDto) {
        MenuGroup menuGroup = menuGroupService.findById(menuDto.getMenuGroupId());
        MenuProducts menuProducts = createMenuProducts(menuDto.getMenuProducts());

        Menu newMenu = Menu.of(menuDto.getName(), Price.of(menuDto.getPrice()), MenuGroupId.of(menuGroup), menuProducts);

        validateForCreate(newMenu);

        return newMenu;
    }

    private MenuProducts createMenuProducts(List<MenuProductDto> menuProductDtos) {
        return MenuProducts.of(menuProductDtos.stream()
                                                .map(menuProductDto -> MenuProduct.of(ProductId.of(menuProductDto.getProductId()), menuProductDto.getQuantity()))
                                                .collect(Collectors.toList())
                             );
    }

    private void validateForCreate(Menu menu) {
        checkAllFindProducts(menu);
        checkMenuPrice(menu);
    }

    private void checkAllFindProducts(Menu menu) {
        List<Long> productIds = menu.getProductIds().stream().map(ProductId::value).collect(Collectors.toList());
        Products products = Products.of(productService.findAllByIds(productIds));

        if (productIds.size() != products.size()) {
            throw new NotFoundProductException();
        }
    }

    private void checkMenuPrice(Menu menu) {
        List<Long> productIds = menu.getProductIds().stream().map(ProductId::value).collect(Collectors.toList());
        Products products = Products.of(productService.findAllByIds(productIds));

        Price sumOfProductsPrice = getSumOfProductsPrice(menu.getMenuProducts(), products);

        if (menu.getPrice().compareTo(sumOfProductsPrice) > 0) {
            throw new NotCorrectMenuPriceException();
        }
    }

    private Price getSumOfProductsPrice(MenuProducts menuProducts, Products products) {
        Price sumOfProductsPrice = Price.of(0);

        for (int index = 0; index < menuProducts.size(); index++) {
            MenuProduct menuProduct = menuProducts.get(index);
            Product product = products.findById(menuProduct.getProductId().value());

            sumOfProductsPrice = sumOfProductsPrice.add(product.getPrice().multiply(menuProduct.getQuantity()));
        }

        return sumOfProductsPrice;
    }
}
