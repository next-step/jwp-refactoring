package kitchenpos.dto;

import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.Price;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.menu.MenuProductDto;
import kitchenpos.dto.menu.MenuProductDtos;

@DisplayName("메뉴상품 전문관련")
public class MenuProductDtosTest {
    @DisplayName("상품의 가격합계를 계산한다.")
    @Test
    void calculate_sumProductPrice() {
        // given
        Product 상품1 = Product.of(1L, "상품1", Price.of(1_200));
        Product 상품2 = Product.of(2L, "상품2", Price.of(2_200));

        MenuProductDtos menuProductDtos = MenuProductDtos.of(List.of(MenuProductDto.of(1L, 1L), MenuProductDto.of(2L, 2L)));

        // when

        Price price = menuProductDtos.getSumProductPrice(List.of(상품1, 상품2));

        // then
        Assertions.assertThat(price).isEqualTo(Price.of(5600));
    }

    @DisplayName("메뉴 아이디만 조회된다.")
    @Test
    void search_menuIds() {
        // given
        MenuProductDtos menuProductDtos = MenuProductDtos.of(List.of(MenuProductDto.of(1L, 1L), MenuProductDto.of(2L, 2L)));        

        // when 
        List<Long> productIds = menuProductDtos.getProductIds();

        // then
        Assertions.assertThat(productIds).isEqualTo(List.of(1L, 2L));
    }
    
    @DisplayName("메뉴 상품객체가 생성된다.")
    @Test
    void create_menuProduct() {
        // given
        Menu menu = Menu.of("신메뉴", Price.of(1_800));

        Product 상품1 = Product.of(1L, "상품1", Price.of(1_200));
        Product 상품2 = Product.of(2L, "상품2", Price.of(2_200));

        MenuProductDtos menuProductDtos = MenuProductDtos.of(List.of(MenuProductDto.of(1L, 1L), MenuProductDto.of(2L, 2L)));        

        // when 
        List<MenuProduct> menuProducts = menuProductDtos.createMenuProduct(menu, List.of(상품1, 상품2));

        // then
        assertAll(
            () -> Assertions.assertThat(menuProducts).hasSize(2),
            () -> Assertions.assertThat(menuProducts.get(0).getMenu().getName()).isEqualTo("신메뉴"),
            () -> Assertions.assertThat(menuProducts.get(0).getMenu().getPrice()).isEqualTo(Price.of(1_800)),
            () -> Assertions.assertThat(menuProducts.get(0).getProduct()).isEqualTo(Product.of(1L, "상품1", Price.of(1_200))),
            () -> Assertions.assertThat(menuProducts.get(0).getQuantity()).isEqualTo(1L),
            () -> Assertions.assertThat(menuProducts.get(1).getMenu().getName()).isEqualTo("신메뉴"),
            () -> Assertions.assertThat(menuProducts.get(1).getMenu().getPrice()).isEqualTo(Price.of(1_800)),
            () -> Assertions.assertThat(menuProducts.get(1).getProduct()).isEqualTo(Product.of(2L, "상품2", Price.of(2_200))),
            () -> Assertions.assertThat(menuProducts.get(1).getQuantity()).isEqualTo(2L)
        );
    }
}
