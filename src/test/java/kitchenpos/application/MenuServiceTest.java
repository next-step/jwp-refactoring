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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static kitchenpos.domain.MenuGroupTestFixture.createMenuGroup;
import static kitchenpos.domain.MenuProductTestFixture.createMenuProduct;
import static kitchenpos.domain.MenuTestFixture.createMenu;
import static kitchenpos.domain.ProductTestFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@DisplayName("메뉴 관련 서비스 테스트")
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

    private Product 짜장면;
    private Product 짬뽕;
    private Product 탕수육;
    private Product 단무지;
    private MenuGroup 중국집_1인_메뉴_세트;
    private MenuProduct 짜장면상품;
    private MenuProduct 짬뽕상품;
    private MenuProduct 탕수육상품;
    private MenuProduct 단무지상품;
    private Menu 짜장면_탕수육_1인_메뉴_세트;
    private Menu 짬뽕_탕수육_1인_메뉴_세트;

    @BeforeEach
    public void setUp() {
        중국집_1인_메뉴_세트 = createMenuGroup(1L,"중국집_1인_메뉴_세트");
        짜장면 = createProduct(1L,"짜장면", BigDecimal.valueOf(8000L));
        짬뽕 = createProduct(2L,"짬뽕", BigDecimal.valueOf(9000L));
        탕수육 = createProduct(3L,"탕수육", BigDecimal.valueOf(12000L));
        단무지 = createProduct(4L,"단무지", BigDecimal.valueOf(0L));
        짜장면상품 = createMenuProduct(1L, null, 짜장면.getId(), 1L);
        짬뽕상품 = createMenuProduct(2L, null, 짬뽕.getId(), 1L);
        탕수육상품 = createMenuProduct(3L, null, 탕수육.getId(), 1L);
        단무지상품 = createMenuProduct(4L, null, 단무지.getId(), 1L);
        짜장면_탕수육_1인_메뉴_세트 = createMenu(1L, "짜장면_탕수육_1인_메뉴_세트", BigDecimal.valueOf(20000L),
                중국집_1인_메뉴_세트.getId(), Arrays.asList(짜장면상품, 탕수육상품, 단무지상품));
        짬뽕_탕수육_1인_메뉴_세트 = createMenu(2L,"짬뽕_탕수육_1인_메뉴_세트", BigDecimal.valueOf(21000L),
                중국집_1인_메뉴_세트.getId(), Arrays.asList(짬뽕상품, 탕수육상품, 단무지상품));
    }

    @DisplayName("메뉴 생성 작업을 성공한다.")
    @Test
    void create() {
        // given
        when(menuGroupDao.existsById(짜장면_탕수육_1인_메뉴_세트.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(짜장면상품.getProductId())).thenReturn(Optional.of(짜장면));
        when(productDao.findById(탕수육상품.getProductId())).thenReturn(Optional.of(탕수육));
        when(productDao.findById(단무지상품.getProductId())).thenReturn(Optional.of(단무지));
        when(menuDao.save(짜장면_탕수육_1인_메뉴_세트)).thenReturn(짜장면_탕수육_1인_메뉴_세트);
        when(menuProductDao.save(짜장면상품)).thenReturn(짜장면상품);
        when(menuProductDao.save(탕수육상품)).thenReturn(탕수육상품);
        when(menuProductDao.save(단무지상품)).thenReturn(단무지상품);

        // when
        Menu saveMenu = menuService.create(짜장면_탕수육_1인_메뉴_세트);

        // then
        assertAll(
                () -> assertThat(saveMenu.getId()).isNotNull(),
                () -> assertThat(saveMenu.getMenuProducts()).containsExactly(짜장면상품, 탕수육상품, 단무지상품)
        );
    }

    @DisplayName("가격이 0원 미만인 메뉴를 생성하면 IllegalArgumentException을 반환한다.")
    @Test
    void createWithException() {
        // given
        Menu menu = createMenu(1L, "짜장면_탕수육_1인_메뉴_세트", BigDecimal.valueOf(-1000L),
                짜장면_탕수육_1인_메뉴_세트.getId(), Arrays.asList(짜장면상품, 탕수육상품, 단무지상품));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("존재하지 않는 메뉴그룹이 포함된 메뉴를 생성하면 IllegalArgumentException을 반환한다.")
    @Test
    void createWithException2() {
        // given
        Menu menu = createMenu(1L, "짜장면_탕수육_1인_메뉴_세트", BigDecimal.valueOf(20000L), 10L,
                Arrays.asList(짜장면상품, 탕수육상품, 단무지상품));
        when(menuGroupDao.existsById(10L)).thenReturn(false);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("존재하지 않는 상품이 포함된 메뉴를 생성하면 IllegalArgumentException을 반환한다.")
    @Test
    void createWithException3() {
        // given
        Menu menu = createMenu(1L, "짜장면_탕수육_1인_메뉴_세트", BigDecimal.valueOf(20000L), 중국집_1인_메뉴_세트.getId(),
                singletonList(짜장면상품));
        when(menuGroupDao.existsById(짜장면_탕수육_1인_메뉴_세트.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(짜장면상품.getProductId())).thenReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴의 가격이 메뉴 상품들의 가격의 합보다 큰 메뉴를 생성하면 IllegalArgumentException을 반환한다.")
    @Test
    void createWithException4() {
        // given
        Menu menu = createMenu(1L, "짜장면_탕수육_1인_메뉴_세트", BigDecimal.valueOf(21000L), 중국집_1인_메뉴_세트.getId(),
                Arrays.asList(짜장면상품, 탕수육상품, 단무지상품));
        when(menuGroupDao.existsById(짜장면_탕수육_1인_메뉴_세트.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(짜장면상품.getProductId())).thenReturn(Optional.of(짜장면));
        when(productDao.findById(탕수육상품.getProductId())).thenReturn(Optional.of(탕수육));
        when(productDao.findById(단무지상품.getProductId())).thenReturn(Optional.of(단무지));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴 전체 목록 조회 작업을 성공한다.")
    @Test
    void list() {
        // given
        List<Menu> menus = Arrays.asList(짜장면_탕수육_1인_메뉴_세트, 짬뽕_탕수육_1인_메뉴_세트);
        when(menuDao.findAll()).thenReturn(menus);
        when(menuProductDao.findAllByMenuId(짜장면_탕수육_1인_메뉴_세트.getId())).thenReturn(Arrays.asList(짜장면상품, 탕수육상품, 단무지상품));
        when(menuProductDao.findAllByMenuId(짬뽕_탕수육_1인_메뉴_세트.getId())).thenReturn(Arrays.asList(짜장면상품, 탕수육상품, 단무지상품));

        // when
        List<Menu> findMenus = menuService.list();

        // then
        assertAll(
                () -> assertThat(findMenus).hasSize(menus.size()),
                () -> assertThat(findMenus).containsExactly(짜장면_탕수육_1인_메뉴_세트, 짬뽕_탕수육_1인_메뉴_세트)
        );
    }
}
