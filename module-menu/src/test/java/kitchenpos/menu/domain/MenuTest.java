package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuTest {

    MenuGroup menuGroup;
    List<MenuProduct> menuProductList;

    @BeforeEach
    void beforeEach(){
        menuGroup = new MenuGroup("메뉴그룹 1");
        MenuProduct menuProduct1 = new MenuProduct(new Product("상품1", new BigDecimal(1000)), 1);
        MenuProduct menuProduct2 = new MenuProduct(new Product("상품2", new BigDecimal(1000)), 1);
        menuProductList = Arrays.asList(menuProduct1, menuProduct2);
    }

    @Test
    @DisplayName("메뉴 생성 테스트")
    void createTest(){
        // given

        // when
        Menu menu = new Menu("메뉴1", new BigDecimal(1500), menuGroup, menuProductList);

        // then
        assertThat(menu).isNotNull();
        assertThat(menu.getMenuProducts()).hasSize(2);

    }

    @Test
    @DisplayName("가격 정보가 없으면 메뉴를 생성할 수 없다.")
    void createFailTest1(){
        // given

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> new Menu("메뉴1", null, menuGroup, menuProductList)
        );
    }

    @Test
    @DisplayName("가격 정보가 음수면 메뉴를 생성할 수 없다.")
    void createFailTest2(){
        // given

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> new Menu("메뉴1", new BigDecimal(-100) , menuGroup, menuProductList)
        );
    }

    @Test
    @DisplayName("가격 정보가 포함된 상품의 가격*수량 보다 클 수 없다.")
    void createFailTest3(){
        // given

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> new Menu("메뉴1", new BigDecimal(2001) , menuGroup, menuProductList)
        );
    }
}
