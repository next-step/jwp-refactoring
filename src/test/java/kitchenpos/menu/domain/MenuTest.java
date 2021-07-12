package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class MenuTest {
    @DisplayName("메뉴 생성 예외 - 가격 미입력시")
    @Test
    public void 가격미입력시_메뉴생성_예외() throws Exception {
        //given
        MenuGroup menuGroup = new MenuGroup("메뉴그룹");
        MenuProduct menuProduct = new MenuProduct(new Product("", BigDecimal.TEN), 1L);
        
        //when
        //then
        assertThatThrownBy(() -> new Menu("반반치킨", null, menuGroup, Arrays.asList(menuProduct)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 예외 - 가격 음수입력시")
    @Test
    public void 가격음수입력시_메뉴생성_예외() throws Exception {
        //given
        MenuGroup menuGroup = new MenuGroup("메뉴그룹");
        MenuProduct menuProduct = new MenuProduct(new Product("", BigDecimal.TEN), 1L);

        //when
        //then
        assertThatThrownBy(() -> new Menu("반반치킨", BigDecimal.valueOf(-1),
                menuGroup, Arrays.asList(menuProduct)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격 계산")
    @Test
    public void 메뉴가격_계산_확인() throws Exception {
        //given
        MenuGroup menuGroup = new MenuGroup("메뉴그룹");
        MenuProduct menuProduct1 = new MenuProduct(new Product("", BigDecimal.valueOf(1_000)), 5L);
        MenuProduct menuProduct2 = new MenuProduct(new Product("", BigDecimal.valueOf(2_000)), 2L);
        Menu menu = new Menu("메뉴", BigDecimal.valueOf(8_000), menuGroup);
        menu.addMenuProduct(menuProduct1);
        menu.addMenuProduct(menuProduct2);

        //when
        Price price = menu.calculatePrice();

        //then
        assertThat(price).isEqualTo(new Price(BigDecimal.valueOf(9_000)));
    }

    @DisplayName("메뉴 생성 예외 - 메뉴 가격이 계산한 가격보다 큰 경우")
    @Test
    public void 메뉴가격이계산한가격보다큰경우_계산_예외() throws Exception {
        //given
        MenuGroup menuGroup = new MenuGroup("메뉴그룹");
        MenuProduct menuProduct1 = new MenuProduct(new Product("", BigDecimal.valueOf(1_000)), 5L);
        MenuProduct menuProduct2 = new MenuProduct(new Product("", BigDecimal.valueOf(2_000)), 2L);

        //when
        assertThatThrownBy(() -> new Menu("메뉴", BigDecimal.valueOf(10_000), menuGroup,
                Arrays.asList(menuProduct1, menuProduct2)))
                .hasMessage("입력받은 메뉴가격이 상품의 총 가격보다 같거나 작아야합니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }
}
