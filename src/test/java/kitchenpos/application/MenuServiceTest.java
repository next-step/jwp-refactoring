package kitchenpos.application;

import static kitchenpos.application.MenuGroupServiceTest.메뉴_그룹_생성;
import static kitchenpos.application.ProductServiceTest.상품_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private ProductDao productDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuProductDao menuProductDao;

    @InjectMocks
    private MenuService menuService;

    private Product 반반;
    private Product 감자튀김;

    private MenuGroup 한마리메뉴;

    private Menu 반반치킨세트;

    private MenuProduct 메뉴반반치킨;
    private MenuProduct 메뉴감자튀김;

    @BeforeEach
    void init() {
        반반 = 상품_생성(1L, "반반", 16_000L);
        감자튀김 = 상품_생성(2L, "감자튀김", 3_000L);
        한마리메뉴 = 메뉴_그룹_생성(1L, "한마리메뉴");
        반반치킨세트 = 메뉴_생성(1L, "반반치킨", 16_000L, 한마리메뉴.getId());

        메뉴반반치킨 = 메뉴_상품_생성(반반.getId(), 1L);
        메뉴감자튀김 = 메뉴_상품_생성(감자튀김.getId(), 1L);
    }

    @Test
    @DisplayName("메뉴를 생성한다.")
    void createMenu() {
        // given
        반반치킨세트.setMenuProducts(Arrays.asList(메뉴반반치킨));
        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(반반.getId())).willReturn(Optional.of(반반));
        given(menuDao.save(any(Menu.class))).willReturn(반반치킨세트);
        given(menuProductDao.save(any(MenuProduct.class))).willReturn(메뉴반반치킨);

        //when
        Menu savedMenu = menuService.create(반반치킨세트);

        // then
        assertAll(
            () -> assertThat(savedMenu).isNotNull(),
            () -> assertThat(savedMenu.getName()).isEqualTo(반반치킨세트.getName()),
            () -> assertThat(savedMenu.getPrice()).isEqualTo(반반치킨세트.getPrice())
        );
    }

    @Test
    @DisplayName("메뉴 가격이 null 이거나 음수일 경우 - 오류")
    void invalidPrice() {
        // given
        Menu 값이_음수인_메뉴 = 메뉴_생성(2L, "반반치킨", -6_000L, 한마리메뉴.getId());
        Menu 값이_없는_메뉴 = 가격이_없는_메뉴_생성(2L, "반반치킨",  한마리메뉴.getId());

        // when then
        assertAll(
            () -> assertThatThrownBy(() -> menuService.create(값이_음수인_메뉴))
                .isInstanceOf(IllegalArgumentException.class),
            () -> assertThatThrownBy(() -> menuService.create(값이_없는_메뉴))
                .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @Test
    @DisplayName("유효하지 않은 메뉴그룹을 가진 메뉴를 생성할 경우 - 오류")
    void notExistMenuGroup() {
        // given
        given(menuGroupDao.existsById(반반치킨세트.getMenuGroupId())).willReturn(false);

        // when then
        assertThatThrownBy(() -> menuService.create(반반치킨세트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("유효하지 않은 상품을 가진 메뉴를 생성할 경우 - 오류")
    void notExistProduct() {
        // given
        반반치킨세트.setMenuProducts(Arrays.asList(메뉴반반치킨));
        given(menuGroupDao.existsById(반반치킨세트.getMenuGroupId())).willReturn(true);
        given(productDao.findById(메뉴반반치킨.getProductId())).willReturn(Optional.empty());

        // when then
        assertThatThrownBy(() -> menuService.create(반반치킨세트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴의 가격이 메뉴 상품 금액의 합보다 클 경우 - 오류")
    void inCaseOfExcessAmount() {
        // given
        반반치킨세트.setMenuProducts(Arrays.asList(메뉴감자튀김));
        given(menuGroupDao.existsById(반반치킨세트.getMenuGroupId())).willReturn(true);
        given(productDao.findById(감자튀김.getId())).willReturn(Optional.of(감자튀김));

        // when then
        assertThatThrownBy(() -> menuService.create(반반치킨세트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 목록을 조회한다.")
    void findAll() {
        // given
        given(menuDao.findAll()).willReturn(Arrays.asList(반반치킨세트));

        // when
        List<Menu> menus = menuService.list();

        // then
        assertThat(menus).containsExactly(반반치킨세트);
    }

    public static  Menu 메뉴_생성(Long menuId, String name, Long price, Long menuGroupId) {
        return new Menu(menuId, name, new BigDecimal(price), menuGroupId);
    }

    public static  Menu 가격이_없는_메뉴_생성(Long menuId, String name, Long menuGroupId) {
        return new Menu(menuId, name, null, menuGroupId);
    }

    public static MenuProduct 메뉴_상품_생성(Long productId, long quantity) {
        return 메뉴_상품_생성(null, productId, quantity);
    }

    public static MenuProduct 메뉴_상품_생성(Long menuId, Long productId, long quantity) {
        return new MenuProduct(menuId, productId, quantity);
    }
}
