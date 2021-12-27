package kitchenpos.menu.domain;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kitchenpos.common.domain.Price;
import kitchenpos.common.vo.MenuGroupId;
import kitchenpos.common.vo.ProductId;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.Products;
import kitchenpos.product.exception.NotFoundProductException;
import kitchenpos.menu.dto.MenuDto;
import kitchenpos.menu.dto.MenuProductDto;
import kitchenpos.menu.exception.NotCorrectMenuPriceException;

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

        Menu newMenu = Menu.of(menuDto.getName(), Price.of(menuDto.getPrice()), MenuGroupId.of(menuGroup.getId()), menuProducts);

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
            throw new NotFoundProductException("요청된 상품과 조회된 상품의 수가 일치하지 않습니다.");
        }
    }

    private void checkMenuPrice(Menu menu) {
        List<Long> productIds = menu.getProductIds().stream().map(ProductId::value).collect(Collectors.toList());
        Products products = Products.of(productService.findAllByIds(productIds));

        Price sumOfProductsPrice = getSumOfProductsPrice(menu.getMenuProducts(), products);

        if (menu.getPrice().compareTo(sumOfProductsPrice) > 0) {
            throw new NotCorrectMenuPriceException("메뉴의 가격은 상품들의 가격의 합보다 클 수 없습니다.");
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
