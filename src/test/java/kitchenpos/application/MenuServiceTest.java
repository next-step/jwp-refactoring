package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
class MenuServiceTest {
    @Autowired
    private MenuGroupDao menuGroupDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private MenuService menuService;

    private MenuGroup 추천메뉴;
    private MenuProduct 페페로니;
    private MenuProduct 치즈피자;

    @BeforeEach
    void setUp() {
        추천메뉴 = createMenuGroup("추천 메뉴");
        페페로니 = createMenuProduct("페페로니", BigDecimal.valueOf(2000), 2);
        치즈피자 = createMenuProduct("치즈피자", BigDecimal.valueOf(1000), 1);
    }

    @DisplayName("정상적으로 메뉴를 생성한 경우")
    @Test
    void create() {
        Menu 기본피자 = createMenu("기본피자", BigDecimal.valueOf(2000), 추천메뉴.getId(), Arrays.asList(페페로니, 치즈피자));

        Menu response = menuService.create(기본피자);

        assertAll(
                () -> assertThat(response.getName()).isEqualTo("기본피자"),
                () -> assertThat(response.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(2000)),
                () -> assertThat(response.getMenuGroupId()).isEqualTo(추천메뉴.getId()),
                () -> assertThat(response.getMenuProducts()).hasSize(2)
        );
    }

    @DisplayName("메뉴의 가격이 null이거나 0보다 작을 수 없다.")
    @Test
    void createMenuPriceNullOrZeroException() {
        Menu 기본피자 = createMenu("기본피자", BigDecimal.valueOf(-1), 추천메뉴.getId(), Arrays.asList(페페로니, 치즈피자));
        Menu 기본피자2 = createMenu("기본피자", null, 추천메뉴.getId(), Arrays.asList(페페로니, 치즈피자));

        assertAll(
                () -> assertThatThrownBy(() -> menuService.create(기본피자))
                        .isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> menuService.create(기본피자2))
                        .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @DisplayName("메뉴 그룹이 존재하지 않으면 생성할 수 없다.")
    @Test
    void createMenuWhenMenuGroupNotExistException() {
        Menu 기본피자 = createMenu("기본피자", BigDecimal.valueOf(-1), null, Arrays.asList(페페로니, 치즈피자));

        assertThatThrownBy(() -> menuService.create(기본피자))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격이 메뉴 상품들의 합보다 클 수 없다.")
    @Test
    void createMenuPriceOverTheProducts() {
        Menu 기본피자 = createMenu("기본피자", BigDecimal.valueOf(5100), 추천메뉴.getId(), Arrays.asList(페페로니, 치즈피자));

        assertThatThrownBy(() -> menuService.create(기본피자))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 조회")
    @Test
    void list() {
        Menu 기본피자 = createMenu("기본피자", BigDecimal.valueOf(2000), 추천메뉴.getId(), Arrays.asList(페페로니, 치즈피자));
        menuService.create(기본피자);

        List<Menu> response = menuService.list();

        List<String> names = response.stream()
                .map(Menu::getName)
                .collect(Collectors.toList());
        assertThat(names).contains("기본피자");
    }

    private Menu createMenu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(name, price, menuGroupId, menuProducts);
    }

    private MenuProduct createMenuProduct(String name, BigDecimal price, long quantity) {
        Product product = productDao.save(new Product(name, price));
        return new MenuProduct(product.getId(), quantity);
    }

    private MenuGroup createMenuGroup(String name) {
        return menuGroupDao.save(new MenuGroup(name));
    }
}
