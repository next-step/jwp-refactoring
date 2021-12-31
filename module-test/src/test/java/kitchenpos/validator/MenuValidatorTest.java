package kitchenpos.validator;

import kitchenpos.common.Price;
import kitchenpos.exception.NotFoundMenuGroupException;
import kitchenpos.exception.NotFoundProductException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class MenuValidatorTest {

    @Autowired
    private MenuValidator menuValidator;

    @Test
    @DisplayName("메뉴 그룹 존재여부 검증")
    void validateMenuGroupTest() {
        assertDoesNotThrow(() -> menuValidator.validateMenuGroup(1L));
    }

    @Test
    @DisplayName("메뉴 그룹을 찾을 수 없습니다.")
    void notFoundMenuGroup() {
        assertThrows(NotFoundMenuGroupException.class, () -> menuValidator.validateMenuGroup(100L));
    }

    @Test
    @DisplayName("상품 존재여부 검증")
    void validateMenuProductsTest() {
        // given
        List<MenuProduct> menuProducts = Arrays.asList(
                new MenuProduct(1L, 1),
                new MenuProduct(2L, 1));
        Menu actual = new Menu(
                "이벤트 메뉴"
                , new Price(BigDecimal.valueOf(30000))
                , 1L
                , menuProducts);
        // when
        //then
        assertDoesNotThrow(() -> menuValidator.validateMenuProducts(actual));
    }

    @Test
    @DisplayName("상품을 찾을 수 없습니다.")
    void notFoundProduct() {
        // given
        List<MenuProduct> menuProducts = Arrays.asList(
                new MenuProduct(1L, 1),
                new MenuProduct(10L, 1));
        Menu actual = new Menu(
                "이벤트 메뉴"
                , new Price(BigDecimal.valueOf(30000))
                , 1L
                , menuProducts);
        // when
        //then
        assertThrows(NotFoundProductException.class, () -> menuValidator.validateMenuProducts(actual));
    }

}