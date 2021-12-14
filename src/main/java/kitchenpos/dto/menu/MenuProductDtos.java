package kitchenpos.dto.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.Price;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.Products;

public class MenuProductDtos {
    List<MenuProductDto> menuProductDtos;

    private MenuProductDtos(List<MenuProductDto> menuProductDtos) {
        this.menuProductDtos = menuProductDtos;
    }

    public static MenuProductDtos of(List<MenuProductDto> menuProductDtos) {
        return new MenuProductDtos(menuProductDtos);
    }

    public Price getSumProductPrice(Products products) {
        Price sumOfProductsPrice = Price.of(0);

        for (MenuProductDto menuProductDto : this.menuProductDtos) {
            Price productPrice = products.findById(menuProductDto.getProductId())
                                          .calculatePriceWithQuantity(menuProductDto.getQuantity());

            sumOfProductsPrice = sumOfProductsPrice.add(productPrice);
        }

        return sumOfProductsPrice;
    }

    public List<Long> getProductIds() {
        return menuProductDtos.stream()
                                .map(MenuProductDto::getProductId)
                                .collect(Collectors.toList());
    }

    public List<MenuProduct> createMenuProduct(Menu menu, Products products) {
        List<MenuProduct> menuProduct = new ArrayList<>();

        for (MenuProductDto menuProductDto : this.menuProductDtos) {
            Product matchingProduct = products.findById(menuProductDto.getProductId());
            menuProduct.add(MenuProduct.of(menu, matchingProduct, menuProductDto.getQuantity()));
        }

        return menuProduct;
    }
}
