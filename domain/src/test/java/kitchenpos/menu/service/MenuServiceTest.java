package kitchenpos.menu.service;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    private String name;
    private BigDecimal price;
    private List<MenuProductRequest> menuProducts;

    @BeforeEach
    public void setup() {
        name = "후라이드치킨";
        price = new BigDecimal(16000);
        menuProducts = new ArrayList<>();
        menuProducts.add(new MenuProductRequest(1L, 1L));
    }

    @Test
    @DisplayName("메뉴를 생성 한다")
    public void createMenu() {
        //given
        MenuRequest menu = new MenuRequest(name, price, 1L, menuProducts);

        //when
        MenuResponse createMenu = menuService.create(menu);

        //then
        assertThat(createMenu.getName()).isEqualTo(name);
        assertThat(createMenu.getPrice().compareTo(price)).isSameAs(0);
        assertThat(createMenu.getMenuGroupId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("메뉴 생성 실패 - 가격이 음수")
    public void createMenuFailByPriceMinus() {
        //given
        MenuRequest menu = new MenuRequest(name, new BigDecimal(-10000), 1L, menuProducts);

        //when
        //then
        assertThrows(IllegalArgumentException.class, () ->  menuService.create(menu));
    }

    @Test
    @DisplayName("메뉴 리스트를 가져온다")
    public void selectMenuList() {
        //when
        List<MenuResponse> menuResponses = menuService.list();

        //then
        for (MenuResponse menuResponse : menuResponses) {
            assertThat(menuResponse.getId()).isNotNull();
            assertThat(menuResponse.getName()).isNotNull();
            assertThat(menuResponse.getPrice()).isNotNull();
            assertThat(menuResponse.getMenuProducts()).isNotEmpty();
        }
    }
}
