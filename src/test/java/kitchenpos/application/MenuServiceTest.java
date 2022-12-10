package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.application.MenuGroupServiceTest.메뉴그룹_생성;
import static kitchenpos.application.ProductServiceTest.generateProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴")
class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private ProductDao productDao;

    private MenuGroup 메뉴그룹;

    private Product 상품1;
    private Product 상품2;

    private MenuProduct 메뉴상품1;
    private MenuProduct 메뉴상품2;

    private List<MenuProduct> 메뉴상품들;

    private Menu 메뉴1;
    private Menu 메뉴2;
    private Menu 메뉴3;
    private Menu 메뉴4;
    private Menu 메뉴5;

    @BeforeEach
    void setUp() {
        메뉴그룹 = 메뉴그룹_생성(1L, "menuGroup");

        상품1 = generateProduct(1L, "product1", 가격(1000));
        상품2 = generateProduct(2L, "product2", 가격(1500));

        메뉴상품1 = generateMenuProduct(상품1.getId(), 1);
        메뉴상품2 = generateMenuProduct(상품2.getId(), 1);

        메뉴상품들 = Arrays.asList(메뉴상품1, 메뉴상품2);

        메뉴1 = generateMenu(1L, "menu1", 가격(2500), 메뉴그룹.getId(), 메뉴상품들);
        메뉴2 = generateMenu(2L, "menu2", 가격(1000), 메뉴그룹.getId(), 메뉴상품들);
        메뉴3 = generateMenu(3L, "menu3", 가격(1500), 메뉴그룹.getId(), 메뉴상품들);
    }

    @Test
    @DisplayName("전체 메뉴를 조회할 수 있다.")
    void menuTest1() {
        List<Menu> 메뉴들 = 메뉴들_생성();

        given(menuDao.findAll()).willReturn(메뉴들);
        given(menuProductDao.findAllByMenuId(any(Long.class))).willReturn(메뉴상품들);

        List<Menu> 조회된_메뉴들 = menuService.list();
        assertThat(조회된_메뉴들.size()).isEqualTo(메뉴들.size());
    }

    @Test
    @DisplayName("새로운 메뉴를 추가할 수 있다.")
    void menuTest2() {
        given(menuGroupDao.existsById(메뉴1.getMenuGroupId())).willReturn(true);
        given(productDao.findById(상품1.getId())).willReturn(Optional.of(상품1));
        given(productDao.findById(상품2.getId())).willReturn(Optional.of(상품2));
        given(menuDao.save(any(Menu.class))).willReturn(메뉴1);

        Menu 추가된_메뉴 = menuService.create(메뉴1);
        assertThat(추가된_메뉴.getName()).isEqualTo(메뉴1.getName());
    }

    @Test
    @DisplayName("새로운 메뉴 추가 : 메뉴 가격은 필수값이며, 음수여서는 안된다.")
    void menuTest3() {
        Menu 가격이_NULL인_메뉴 = generateMenu(1L, "menu1", null, 메뉴그룹.getId(), 메뉴상품들);
        Menu 가격이_음수인_메뉴 = generateMenu(2L, "menu2", 가격(-1), 메뉴그룹.getId(), 메뉴상품들);

        assertThatThrownBy(() -> menuService.create(가격이_NULL인_메뉴))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> menuService.create(가격이_음수인_메뉴))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("새로운 메뉴 추가 : 존재하지 않는 메뉴 그룹으로 요청할 수 없다.")
    void menuTest4() {
        Menu 메뉴그룹이_NULL인_메뉴 = generateMenu(1L, "menu1", 가격(1500), 999L, 메뉴상품들);

        assertThatThrownBy(() -> menuService.create(메뉴그룹이_NULL인_메뉴))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("새로운 메뉴 추가 : 존재하지 않는 상품으로 요청할 수 없다.")
    void menuTest5() {
        List<MenuProduct> 존재하지않는_제품이포함된_메뉴제품들 = new ArrayList<>();
        존재하지않는_제품이포함된_메뉴제품들.add(generateMenuProduct(999L, 1));

        Menu 존재하지않는_제품이포함된_메뉴 = generateMenu(1L, "menu1", 가격(1500), 메뉴그룹.getId(), 존재하지않는_제품이포함된_메뉴제품들);

        assertThatThrownBy(() -> menuService.create(존재하지않는_제품이포함된_메뉴))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("새로운 메뉴 추가 : 메뉴 가격은 메뉴 상품들의 가격의 합보다 크면 안된다.")
    void menuTest6() {
        Menu 상품들의_가격의합보다_큰메뉴 = generateMenu(1L, "menu1", 가격(3000), 메뉴그룹.getId(), 메뉴상품들);

        assertThatThrownBy(() -> menuService.create(상품들의_가격의합보다_큰메뉴))
                .isInstanceOf(IllegalArgumentException.class);
    }

    public static Menu generateMenu(Long id, String name, BigDecimal price,
                                    Long menuGroupId, List<MenuProduct> menuProducts) {
        return Menu.of(id, name, price, menuGroupId, menuProducts);
    }

    public static MenuProduct generateMenuProduct(Long productId, long quantity) {
        return MenuProduct.of(null, null, productId, quantity);
    }

    private List<Menu> 메뉴들_생성() {
        return Arrays.asList(메뉴1, 메뉴2, 메뉴3);
    }

    private BigDecimal 가격(int price) {
        return new BigDecimal(price);
    }

}