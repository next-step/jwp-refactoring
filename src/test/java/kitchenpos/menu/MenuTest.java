package kitchenpos.menu;

import kitchenpos.menu.domain.CreateMenuValidator;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class MenuTest {
    @Test
    @DisplayName("메뉴를 구성한다.")
    void createMenu() {
        MenuProduct 짜장면상품 = new MenuProduct(1L, 1);
        MenuProduct 탕수육상품 = new MenuProduct(2L, 1);

        CreateMenuValidator menuValidator = mock(CreateMenuValidator.class);
        Menu menu = Menu.create("짜장면셋트", new BigDecimal(20000), 1L, Arrays.asList(짜장면상품, 탕수육상품), menuValidator);
        menu.organizeMenu(Arrays.asList(짜장면상품, 탕수육상품));

        assertThat(menu.getMenuProducts().getMenuProducts()).hasSize(2);
    }
}
