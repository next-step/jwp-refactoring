package kitchenpos.menu.domain;

import kitchenpos.menu.exceptions.InputMenuDataErrorCode;
import kitchenpos.menu.exceptions.InputMenuDataException;
import kitchenpos.product.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("메뉴 단위테스트")
class MenuTest {

    @Test
    @DisplayName("메뉴를 등록한다.")
    void saveMenuTest() {
        Menu menu = new Menu(1L, "스페셜치킨", new BigDecimal("10000"), 1L);
        checkValidMenu(menu);
    }

    @Test
    @DisplayName("잘못된 가격의 메뉴를 등록하면 에러 처리")
    void saveWrongPriceMenuTest() {
        Assertions.assertThatThrownBy(() -> {
                    new Menu(1L, "스페셜치킨", new BigDecimal(-10000), 1L);
                }).isInstanceOf(InputMenuDataException.class)
                .hasMessageContaining(InputMenuDataErrorCode.IT_CAN_NOT_INPUT_MENU_PRICE_LESS_THAN_ZERO.errorMessage());
    }

    @Test
    @DisplayName("메뉴그룹 아이디를 입력하지 않으면 에러 처리")
    void saveEmptyMenuGroupIdMenuTest() {
        Assertions.assertThatThrownBy(() -> {
                    new Menu(1L, "스페셜치킨", new BigDecimal(10000), null);
                }).isInstanceOf(InputMenuDataException.class)
                .hasMessageContaining(InputMenuDataErrorCode.YOU_MUST_INPUT_MENU_GROUP_ID.errorMessage());
    }

    @Test
    @DisplayName("메뉴그룹 아이디가 0보다 작을 때 에러 처리")
    void saveWrongMenuGroupIdMenuTest() {
        Assertions.assertThatThrownBy(() -> {
                    new Menu(1L, "스페셜치킨", new BigDecimal(10000), -1L);
                }).isInstanceOf(InputMenuDataException.class)
                .hasMessageContaining(InputMenuDataErrorCode.THE_MENU_GROUP_ID_IS_LESS_THAN_ZERO.errorMessage());
    }

    @Test
    @DisplayName("메뉴의 가격이 메뉴에 속하는 상품 가격의 합보다 클 경우 에러처리.")
    void sumPriceMenuTest(){
        Menu menu = new Menu(1L, "양념신나치킨세트", new BigDecimal(100000), 1L);
        Product product1 = new Product(1L, "양념치킨", new BigDecimal(50100));
        Product product2 = new Product(2L, "신나치킨", new BigDecimal(5000));

        Assertions.assertThatThrownBy(() -> {
                    BigDecimal sumPrice = product1.sumPrice(product2.getPrice());
                    menu.validSum(sumPrice);
                }).isInstanceOf(InputMenuDataException.class)
                .hasMessageContaining(InputMenuDataErrorCode.THE_SUM_OF_MENU_PRICE_IS_LESS_THAN_SUM_OF_PRODUCTS.errorMessage());
    }


    private void checkValidMenu(Menu menu) {
        assertAll(
                () -> assertThat(menu.getId()).isEqualTo(1L),
                () -> assertThat(menu.getName().getName()).isEqualTo("스페셜치킨"),
                () -> assertThat(menu.getPrice().getPrice()).isEqualTo(new BigDecimal("10000")),
                () -> assertThat(menu.getMenuGroupId()).isEqualTo(1L)
        );
    }
}
