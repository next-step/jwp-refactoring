package kitchenpos.application;

import static java.util.Collections.singletonList;
import static kitchenpos.domain.MenuGroupTestFixture.generateMenuGroup;
import static kitchenpos.domain.MenuProductTestFixture.generateMenuProduct;
import static kitchenpos.domain.MenuTestFixture.generateMenu;
import static kitchenpos.domain.ProductTestFixture.generateProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 관련 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private MenuService menuService;

    private Product 감자튀김;
    private Product 불고기버거;
    private Product 치킨버거;
    private Product 콜라;
    private MenuGroup 햄버거세트;
    private MenuProduct 감자튀김상품;
    private MenuProduct 불고기버거상품;
    private MenuProduct 치킨버거상품;
    private MenuProduct 콜라상품;
    private Menu 불고기버거세트;
    private Menu 치킨버거세트;

    @BeforeEach
    void setUp() {
        감자튀김 = generateProduct(1L, "감자튀김", BigDecimal.valueOf(3000L));
        콜라 = generateProduct(2L, "콜라", BigDecimal.valueOf(1500L));
        불고기버거 = generateProduct(3L, "불고기버거", BigDecimal.valueOf(4000L));
        치킨버거 = generateProduct(4L, "치킨버거", BigDecimal.valueOf(4500L));
        햄버거세트 = generateMenuGroup(1L, "햄버거세트");
        감자튀김상품 = generateMenuProduct(1L, null, 감자튀김.getId(), 1L);
        콜라상품 = generateMenuProduct(2L, null, 콜라.getId(), 1L);
        불고기버거상품 = generateMenuProduct(3L, null, 불고기버거.getId(), 1L);
        치킨버거상품 = generateMenuProduct(3L, null, 치킨버거.getId(), 1L);
        불고기버거세트 = generateMenu(1L, "불고기버거세트", BigDecimal.valueOf(8500L), 햄버거세트.getId(),
                Arrays.asList(감자튀김상품, 콜라상품, 불고기버거상품));
        치킨버거세트 = generateMenu(2L, "치킨버거세트", BigDecimal.valueOf(9000L), 햄버거세트.getId(),
                Arrays.asList(감자튀김상품, 콜라상품, 치킨버거상품));
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void createMenu() {
        // given
        when(menuGroupDao.existsById(불고기버거세트.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(감자튀김상품.getProductId())).thenReturn(Optional.of(감자튀김));
        when(productDao.findById(콜라상품.getProductId())).thenReturn(Optional.of(콜라));
        when(productDao.findById(불고기버거상품.getProductId())).thenReturn(Optional.of(불고기버거));
        when(menuDao.save(불고기버거세트)).thenReturn(불고기버거세트);
        when(menuProductDao.save(감자튀김상품)).thenReturn(감자튀김상품);
        when(menuProductDao.save(콜라상품)).thenReturn(콜라상품);
        when(menuProductDao.save(불고기버거상품)).thenReturn(불고기버거상품);

        // when
        Menu saveMenu = menuService.create(불고기버거세트);

        // then
        assertAll(
                () -> assertThat(saveMenu.getId()).isNotNull(),
                () -> assertThat(saveMenu.getMenuProducts()).containsExactly(감자튀김상품, 콜라상품, 불고기버거상품),
                () -> assertThat(saveMenu.getId()).isEqualTo(감자튀김상품.getMenuId()),
                () -> assertThat(saveMenu.getId()).isEqualTo(콜라상품.getMenuId()),
                () -> assertThat(saveMenu.getId()).isEqualTo(불고기버거상품.getMenuId())
        );
    }

    @DisplayName("가격이 비어있는 메뉴는 생성할 수 없다.")
    @Test
    void createMenuThrowErrorWhenPriceIsNull() {
        // given
        Menu menu = generateMenu(1L, "불고기버거세트", null, 햄버거세트.getId(), Arrays.asList(감자튀김상품, 콜라상품, 불고기버거상품));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("가격이 0원 미만인 메뉴는 생성할 수 없다.")
    @ParameterizedTest(name = "등록하고자 하는 메뉴의 가격: {0}")
    @ValueSource(longs = {-1000, -2000})
    void createMenuThrowErrorWhenPriceIsSmallerThenZero(long price) {
        // given
        Menu menu = generateMenu(1L, "불고기버거세트", BigDecimal.valueOf(price), 햄버거세트.getId(), Arrays.asList(감자튀김상품, 콜라상품, 불고기버거상품));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("존재하는 메뉴 그룹에 속하지 않는 메뉴는 생성할 수 없다.")
    @Test
    void createMenuThrowErrorWhenMenuGroupIsNotExists() {
        // given
        Menu menu = generateMenu(1L, "불고기버거세트", BigDecimal.valueOf(8500L), 10L,
                Arrays.asList(감자튀김상품, 콜라상품, 불고기버거상품));
        when(menuGroupDao.existsById(10L)).thenReturn(false);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("존재하지 않는 상품이 메뉴에 존재하면 해당 메뉴를 생성할 수 없다.")
    @Test
    void createMenuThrowErrorWhenMenuProductIsNotExists() {
        // given
        Menu menu = generateMenu(1L, "불고기버거세트", BigDecimal.valueOf(8500L), 햄버거세트.getId(),
                singletonList(감자튀김상품));
        when(menuGroupDao.existsById(불고기버거세트.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(감자튀김상품.getProductId())).thenReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴의 가격은 해당 메뉴에 존재하는 메뉴 상품들의 가격의 합보다 클 수 없다.")
    @Test
    void createMenuThrowErrorWhenMenuPriceIsBiggerThanMenuProductsPriceSum() {
        // given
        Menu menu = generateMenu(1L, "불고기버거세트", BigDecimal.valueOf(9500L), 햄버거세트.getId(),
                Arrays.asList(감자튀김상품, 콜라상품, 불고기버거상품));
        when(menuGroupDao.existsById(불고기버거세트.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(감자튀김상품.getProductId())).thenReturn(Optional.of(감자튀김));
        when(productDao.findById(콜라상품.getProductId())).thenReturn(Optional.of(콜라));
        when(productDao.findById(불고기버거상품.getProductId())).thenReturn(Optional.of(불고기버거));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴 전체 목록을 조회한다.")
    @Test
    void findAllMenus() {
        // given
        List<Menu> menus = Arrays.asList(불고기버거세트, 치킨버거세트);
        when(menuDao.findAll()).thenReturn(menus);
        when(menuProductDao.findAllByMenuId(불고기버거세트.getId())).thenReturn(Arrays.asList(감자튀김상품, 콜라상품, 불고기버거상품));
        when(menuProductDao.findAllByMenuId(치킨버거세트.getId())).thenReturn(Arrays.asList(감자튀김상품, 콜라상품, 치킨버거상품));

        // when
        List<Menu> findMenus = menuService.list();

        // then
        assertAll(
                () -> assertThat(findMenus).hasSize(menus.size()),
                () -> assertThat(findMenus).containsExactly(불고기버거세트, 치킨버거세트)
        );
    }
}
