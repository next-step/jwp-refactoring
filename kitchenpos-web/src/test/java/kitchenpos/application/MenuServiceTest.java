package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import kitchenpos.application.creator.MenuGroupHelper;
import kitchenpos.application.creator.MenuHelper;
import kitchenpos.application.creator.MenuProductHelper;
import kitchenpos.application.creator.ProductHelper;
import kitchenpos.domain.model.MenuGroup;
import kitchenpos.domain.model.Product;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuDto;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.domain.repository.MenuGroupDao;
import kitchenpos.domain.repository.ProductDao;
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
        MenuGroup menuGroup = menuGroupDao.save(MenuGroupHelper.createRequest("메뉴 그룹").toEntity());
        MenuCreateRequest menu = MenuHelper.createRequest("메뉴", 50_000, menuGroup.getId(), getMenuProductsRequest());

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
        MenuGroup menuGroup = menuGroupDao.save(MenuGroupHelper.createRequest("메뉴 그룹").toEntity());
        MenuCreateRequest menu = MenuHelper.createRequest("메뉴", -1, menuGroup.getId(), getMenuProductsRequest());

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성시 금액이 null인 경우")
    @Test
    void menuCreateWithNullPriceTest() {
        MenuGroup menuGroup = menuGroupDao.save(MenuGroupHelper.createRequest("메뉴 그룹").toEntity());
        MenuCreateRequest menu = MenuHelper.createRequest("메뉴", null, menuGroup.getId(), getMenuProductsRequest());

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성시 메뉴 그룹이 존재하지 않을 경우")
    @Test
    void menuCreateWithoutMenuGroup() {
        MenuCreateRequest menu = MenuHelper.createRequest("메뉴", 50_000, 999L, getMenuProductsRequest());

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성시 메뉴의 합보다 같거나 작아야한다.")
    @Test
    void menuCreateWithWrongAmountTotalSum() {
        MenuGroup menuGroup = menuGroupDao.save(MenuGroupHelper.createRequest("메뉴 그룹").toEntity());
        MenuCreateRequest menu = MenuHelper.createRequest("메뉴", 50_001, menuGroup.getId(), getMenuProductsRequest());

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 조회 테스트")
    @Test
    void menuListTest() {
        MenuGroup menuGroup = menuGroupDao.save(MenuGroupHelper.createRequest("메뉴 그룹").toEntity());
        MenuCreateRequest menu = MenuHelper.createRequest("메뉴", 50_000, menuGroup.getId(), getMenuProductsRequest());

        menuService.create(menu);
        menuService.create(menu);
        menuService.create(menu);

        List<MenuDto> list = menuService.list();
        assertThat(list.size()).isGreaterThan(2);
    }

    private List<MenuProductRequest> getMenuProductsRequest() {
        Product savedProduct01 = productDao.save(ProductHelper.createRequest("product01", 10_000).toEntity());
        Product savedProduct02 = productDao.save(ProductHelper.createRequest("product02", 20_000).toEntity());
        MenuProductRequest menuProduct01 = MenuProductHelper.createRequest(savedProduct01.getId(), 1);
        MenuProductRequest menuProduct02 = MenuProductHelper.createRequest(savedProduct02.getId(), 2);
        return Arrays.asList(menuProduct01, menuProduct02);
    }

}
