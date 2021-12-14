package kitchenpos.dto.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.Price;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.exception.product.NotFoundProductException;

public class MenuProductDtos {
    List<MenuProductDto> menuProductDtos;

    private MenuProductDtos(List<MenuProductDto> menuProductDtos) {
        this.menuProductDtos = menuProductDtos;
    }

    public static MenuProductDtos of(List<MenuProductDto> menuProductDtos) {
        return new MenuProductDtos(menuProductDtos);
    }

    public Price getSumProductPrice(List<Product> products) {
        Price sumOfProductsPrice = Price.of(0);

        for (MenuProductDto menuProductDto : this.menuProductDtos) {
            sumOfProductsPrice = sumOfProductsPrice.add(products.stream()
                                                                .filter(product -> product.getId().equals(menuProductDto.getProductId()))
                                                                .map(product -> product.calculatePriceWithQuantity(menuProductDto.getQuantity()))
                                                                .findFirst()
                                                                .orElseThrow(NotFoundProductException::new)
                                                       );
        }

        return sumOfProductsPrice;
    }

    public List<Long> getProductIds() {
        return menuProductDtos.stream()
                                .map(MenuProductDto::getProductId)
                                .collect(Collectors.toList());
    }

    public List<MenuProduct> createMenuProduct(Menu menu, List<Product> products) {
        List<MenuProduct> menuProduct = new ArrayList<>();

        for (MenuProductDto menuProductDto : this.menuProductDtos) {
            Product matchingProduct = products.stream()
                                                .filter(product -> product.getId().equals(menuProductDto.getProductId()))
                                                .findFirst()
                                                .orElseThrow(NotFoundProductException::new);

            menuProduct.add(MenuProduct.of(menu, matchingProduct, menuProductDto.getQuantity()));
        }

        return menuProduct;
    }
}
