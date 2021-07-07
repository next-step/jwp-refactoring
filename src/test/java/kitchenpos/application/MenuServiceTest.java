package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메뉴를 관리한다")
@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    private String name = "치즈치킨";
    private long nonExistentID = 9999L;

    @DisplayName("메뉴를 등록할수 있다.")
    @Test
    void createTest() {
        // given
        Menu menu = new Menu(name, BigDecimal.valueOf(15000), 1L);
        menu.setMenuProducts(Arrays.asList(new MenuProduct(1L, 1L,1)));

        // when
        Menu actualMenu = menuService.create(menu);

        // then
        assertThat(actualMenu).isNotNull();
    }

    @DisplayName("메뉴를 등록시, 가격은 필수값이고, 가격이 0원 이상이어야 한다.")
    @Test
    void createExceptionTest1() {
        // given
        assertThatThrownBy(() -> menuService.create(new Menu(name, null, 1L)))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("가격");

        assertThatThrownBy(() -> menuService.create(new Menu(name, BigDecimal.valueOf(-1), 1L)))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("가격").hasMessageContaining("0원");
    }

    @DisplayName("메뉴를 등록시, 메뉴 그룹이 존재하는 메뉴만 등록할 수 있다.")
    @Test
    void createExceptionTest2() {
        // given
        Menu menu = new Menu(name, BigDecimal.valueOf(15000), nonExistentID);
        menu.setMenuProducts(Arrays.asList(new MenuProduct(1L, 1L,1)));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("메뉴 그룹").hasMessageContaining("존재");
    }

    @DisplayName("메뉴를 등록시, 상품에 등록된 메뉴만 등록할 수 있다.")
    @Test
    void createExceptionTest3() {
        // given
        Menu menu = new Menu(name, BigDecimal.valueOf(15000), 1L);
        menu.setMenuProducts(Arrays.asList(new MenuProduct(1L, nonExistentID,1)));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("상품에 등록된");
    }

    @DisplayName("메뉴를 등록시, 메뉴의 가격은 `[메뉴의 수량] X [상품의 가격]` 보다 비쌀 수 없다.")
    @Test
    void createExceptionTest4() {
        // given
        Menu menu = new Menu(name, BigDecimal.valueOf(1500000), 1L);
        menu.setMenuProducts(Arrays.asList(new MenuProduct(10L, 1L,1)));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("메뉴의 가격").hasMessageContaining("보다 비쌀 수 없다");
    }

    @DisplayName("메뉴들을 조회할수 있다.")
    @Test
    void selectTest() {
        // when
        List<Menu> menus = menuService.list();

        // then
        assertThat(menus).isNotNull();
    }
}