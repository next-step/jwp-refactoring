package kitchenpos.menu.domain;

import kitchenpos.common.exception.InputDataErrorCode;
import kitchenpos.common.exception.InputDataException;
import kitchenpos.menu.exceptions.InputMenuDataErrorCode;
import kitchenpos.menu.exceptions.InputMenuDataException;
import kitchenpos.product.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

@DisplayName("메뉴 단위테스트")
class MenuTest {

    @Test
    @DisplayName("잘못된 가격의 메뉴를 등록하면 에러 처리")
    void saveWrongPriceMenuTest() {
        Assertions.assertThatThrownBy(() -> {
                    Product newProduct = new Product(1L, "신양념치킨", new BigDecimal(50000));
                    MenuProduct menuProduct = new MenuProduct(newProduct, 5);
                    new Menu(1L, "스페셜치킨", new BigDecimal(-10000), 1L, Arrays.asList(menuProduct));
                }).isInstanceOf(InputDataException.class)
                .hasMessageContaining(InputDataErrorCode.THE_PRICE_CAN_NOT_INPUT_LESS_THAN_ZERO.errorMessage());
    }

    @Test
    @DisplayName("메뉴그룹 아이디를 입력하지 않으면 에러 처리")
    void saveEmptyMenuGroupIdMenuTest() {
        Assertions.assertThatThrownBy(() -> {
                    Product newProduct = new Product(1L, "신양념치킨", new BigDecimal(50000));
                    MenuProduct menuProduct = new MenuProduct(newProduct, 5);
                    new Menu(1L, "스페셜치킨", new BigDecimal(-10000), null, Arrays.asList(menuProduct));
                }).isInstanceOf(InputMenuDataException.class)
                .hasMessageContaining(InputMenuDataErrorCode.YOU_MUST_INPUT_MENU_GROUP_ID.errorMessage());
    }

    @Test
    @DisplayName("메뉴그룹 아이디가 0보다 작을 때 에러 처리")
    void saveWrongMenuGroupIdMenuTest() {
        Assertions.assertThatThrownBy(() -> {
                    Product newProduct = new Product(1L, "신양념치킨", new BigDecimal(50000));
                    MenuProduct menuProduct = new MenuProduct(newProduct, 5);
                    new Menu(1L, "스페셜치킨", new BigDecimal(-10000), -1L, Arrays.asList(menuProduct));
                }).isInstanceOf(InputMenuDataException.class)
                .hasMessageContaining(InputMenuDataErrorCode.THE_MENU_GROUP_ID_IS_LESS_THAN_ZERO.errorMessage());
    }

    @Test
    @DisplayName("메뉴의 가격이 메뉴에 속하는 상품 가격의 합보다 클 경우 에러처리.")
    void sumPriceMenuTest() {
        Product product1 = new Product(1L, "양념치킨", new BigDecimal(50100));
        Product product2 = new Product(2L, "신나치킨", new BigDecimal(5000));
        MenuProduct menuProduct1 = new MenuProduct(product1, 5);
        MenuProduct menuProduct2 = new MenuProduct(product1, 5);

        Menu menu = new Menu(1L, "양념신나치킨세트", new BigDecimal(100000), 1L, Arrays.asList(menuProduct1, menuProduct2));

        Assertions.assertThatThrownBy(() -> {
                    BigDecimal sumProductPrice = product1.sumPrice(product2.getPrice());
                    menu.validSum(sumProductPrice);
                }).isInstanceOf(InputMenuDataException.class)
                .hasMessageContaining(InputMenuDataErrorCode.THE_SUM_OF_MENU_PRICE_IS_LESS_THAN_SUM_OF_PRODUCTS.errorMessage());
    }

    @Test
    @DisplayName("메뉴에 상품이 없을 경우 에러처리.")
    void checkMenuHasProductTest() {
        Assertions.assertThatThrownBy(() -> {
                    new Menu(1L, "양념신나치킨세트", new BigDecimal(100000), 1L, null);
                }).isInstanceOf(InputMenuDataException.class)
                .hasMessageContaining(InputMenuDataErrorCode.THE_MENU_MUST_HAVE_PRODUCT.errorMessage());
    }
}
