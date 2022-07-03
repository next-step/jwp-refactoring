package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @DisplayName("메뉴를 등록한다.")
    @Test
    public void createMenu(){
        //given
        Menu menu = new Menu("메뉴", BigDecimal.valueOf(1000), 1L, createMenuProducts());
        //when
        Menu result = menuService.create(menu);
        //then
        assertThat(result).isNotNull();
    }

    @DisplayName("메뉴의 가격을 0원 이하로 등록 시 에러")
    @Test
    public void createMinusPriceMenu(){
        //given
        Menu menu = new Menu("메뉴", BigDecimal.valueOf(-1), 1L, createMenuProducts());
        //when
        //then
        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되지 않은 메뉴그룹으로 메뉴를 등록시 에러")
    @Test
    public void createWithNoExistMenuGroup(){
        //given
        Menu menu = new Menu("메뉴", BigDecimal.valueOf(1000), 99999L, createMenuProducts());
        //when
        //then
        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되지 않은 메뉴 상품으로 메뉴를 등록시 에러")
    @Test
    public void createWithNoExistMenuProducts(){
        //given
        Menu menu = new Menu("메뉴", BigDecimal.valueOf(1000), 1L, createNoExistMenuProducts());
        //when
        //then
        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격은 메뉴에 속한 메뉴 상품 가격의 합보다 크면 에러")
    @Test
    public void createWithGreaterSumPrice(){
        //given
        Menu menu = new Menu("메뉴", BigDecimal.valueOf(20000), 1L, createMenuProducts());
        //when
        //then
        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("메뉴 전체를 조회한다.")
    @Test
    public void getMenus(){
        //given
        Menu menu = menuService.create(new Menu("메뉴", BigDecimal.valueOf(1000), 1L, createMenuProducts()));
        //when
        List<Menu> result = menuService.list();
        //then ( 사전에 DB에 저장이 되어 있음 )
        assertAll(() -> assertThat(result.contains(menu)),
            () -> assertThat(result).hasSizeGreaterThan(1));

    }

    //16000원
    private List<MenuProduct> createMenuProducts() {
        return Arrays.asList(new MenuProduct( 1L, 1L, 1L));
    }

    private List<MenuProduct> createNoExistMenuProducts() {
        return Arrays.asList(new MenuProduct( 999L, 999L, 999L));
    }

}
