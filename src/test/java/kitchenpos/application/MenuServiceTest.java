package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.creator.MenuGroupHelper;
import kitchenpos.application.creator.MenuHelper;
import kitchenpos.application.creator.MenuProductHelper;
import kitchenpos.application.creator.ProductHelper;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dto.MenuDto;
import kitchenpos.dto.MenuGroupDto;
import kitchenpos.dto.MenuProductDto;
import kitchenpos.dto.ProductDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-10
 */
@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;


    @DisplayName("메뉴 생성")
    @Test
    void menuCreateTest() {
        MenuDto menu = getMenu();
        MenuDto savedMenu = menuService.create(menu);

        assertThat(savedMenu.getId()).isNotNull();
        assertThat(savedMenu.getName()).isEqualTo(menu.getName());
        assertThat(savedMenu.getPrice().longValue()).isEqualTo(menu.getPrice().longValue());
        assertThat(savedMenu.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
        assertThat(savedMenu.getMenuProducts().get(0).getQuantity()).isEqualTo(menu.getMenuProducts().get(0).getQuantity());
        assertThat(savedMenu.getMenuProducts().get(0).getProductId()).isEqualTo(menu.getMenuProducts().get(0).getProductId());
        assertThat(savedMenu.getMenuProducts().get(1).getQuantity()).isEqualTo(menu.getMenuProducts().get(1).getQuantity());
        assertThat(savedMenu.getMenuProducts().get(1).getProductId()).isEqualTo(menu.getMenuProducts().get(1).getProductId());
    }

    @Test
    @DisplayName("메뉴 생성시 금액이 0원 아래일 경우")
    void menuCreateWithMinusPriceTest() {
        MenuDto menu = getMenu();
        menu.setPrice(BigDecimal.valueOf(-1));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성시 금액이 null인 경우")
    @Test
    void menuCreateWithNullPriceTest() {
        MenuDto menu = getMenu();
        menu.setPrice(null);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성시 메뉴 그룹이 존재하지 않을 경우")
    @Test
    void menuCreateWithoutMenuGroup() {
        MenuDto menu = getMenu();
        menu.setMenuGroupId(20L);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성시 메뉴의 합보다 같거나 작아야한다.")
    @Test
    void menuCreateWithWrongAmountTotalSum() {
        MenuDto menu = getMenu();
        menu.setPrice(BigDecimal.valueOf(50_001));
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 조회 테스트")
    @Test
    void menuListTest() {
        menuService.create(getMenu());
        menuService.create(getMenu());
        menuService.create(getMenu());

        List<MenuDto> list = menuService.list();
        assertThat(list.size()).isGreaterThan(2);
    }


    private MenuDto getMenu() {
        ProductDto savedProduct01 = productDao.save(
                ProductHelper.create("product01", 10_000));
        ProductDto savedProduct02 = productDao.save(
                ProductHelper.create("product02", 20_000));

        MenuProductDto menuProduct01 = MenuProductHelper.create(savedProduct01, 1);
        MenuProductDto menuProduct02 = MenuProductHelper.create(savedProduct02, 2);

        MenuGroupDto menuGroup = menuGroupDao.save(MenuGroupHelper.create("메뉴 그룹"));

        return MenuHelper.create("메뉴", 50_000, menuGroup, menuProduct01, menuProduct02);
    }
}
