package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
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
import static kitchenpos.fixture.MenuGroupTestFixture.중국집_1인_메뉴_세트;
import static kitchenpos.fixture.MenuGroupTestFixture.중국집_1인_메뉴_세트_요청;
import static kitchenpos.fixture.MenuProductTestFixture.*;
import static kitchenpos.fixture.MenuTestFixture.createMenu;
import static kitchenpos.fixture.MenuTestFixture.메뉴_세트_생성;
import static kitchenpos.fixture.ProductTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("메뉴 관련 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

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
    private MenuProduct 짜장면메뉴상품;
    private MenuProduct 짬뽕메뉴상품;
    private MenuProduct 탕수육메뉴상품;
    private MenuProduct 단무지메뉴상품;
    private MenuRequest 짜장면_탕수육_1인_메뉴_세트_요청;
    private MenuRequest 짬뽕_탕수육_1인_메뉴_세트_요청;
    private Menu 짜장면_탕수육_1인_메뉴_세트;
    private Menu 짬뽕_탕수육_1인_메뉴_세트;

    @BeforeEach
    public void setUp() {
        중국집_1인_메뉴_세트 = 중국집_1인_메뉴_세트(중국집_1인_메뉴_세트_요청());
        짜장면 = 상품생성(짜장면_요청());
        탕수육 = 상품생성(탕수육_요청());
        짬뽕 = 상품생성(짬뽕_요청());
        단무지 = 상품생성(단무지_요청());
        짜장면메뉴상품 = 짜장면메뉴상품();
        짬뽕메뉴상품 = 짬뽕메뉴상품();
        탕수육메뉴상품 = 탕수육메뉴상품();
        단무지메뉴상품 = 단무지메뉴상품();
        짜장면_탕수육_1인_메뉴_세트_요청 = createMenu(1L, "짜장면_탕수육_1인_메뉴_세트", BigDecimal.valueOf(20000L),
                중국집_1인_메뉴_세트.getId(), Arrays.asList(짜장면메뉴상품, 탕수육메뉴상품, 단무지메뉴상품));
        짬뽕_탕수육_1인_메뉴_세트_요청 = createMenu(2L, "짬뽕_탕수육_1인_메뉴_세트", BigDecimal.valueOf(21000L),
                중국집_1인_메뉴_세트.getId(), Arrays.asList(짬뽕메뉴상품, 탕수육메뉴상품, 단무지메뉴상품));
        짜장면_탕수육_1인_메뉴_세트 = 메뉴_세트_생성(짜장면_탕수육_1인_메뉴_세트_요청);
        짬뽕_탕수육_1인_메뉴_세트 = 메뉴_세트_생성(짬뽕_탕수육_1인_메뉴_세트_요청);
    }

    @DisplayName("메뉴 생성 작업을 성공한다.")
    @Test
    void create() {
        // given
        when(productDao.findById(짜장면.getId())).thenReturn(Optional.of(짜장면));
        when(productDao.findById(탕수육.getId())).thenReturn(Optional.of(탕수육));
        when(productDao.findById(단무지.getId())).thenReturn(Optional.of(단무지));
        when(menuDao.save(any())).thenReturn(짜장면_탕수육_1인_메뉴_세트);
        when(menuProductDao.save(짜장면메뉴상품)).thenReturn(짜장면메뉴상품);
        when(menuProductDao.save(탕수육메뉴상품)).thenReturn(탕수육메뉴상품);
        when(menuProductDao.save(단무지메뉴상품)).thenReturn(단무지메뉴상품);

        // when
        MenuResponse saveMenu = menuService.create(짜장면_탕수육_1인_메뉴_세트_요청);

        // then
        assertAll(
                () -> assertThat(saveMenu.getId()).isNotNull(),
                () -> assertThat(saveMenu.getMenuProducts()).containsExactly(짜장면메뉴상품, 탕수육메뉴상품, 단무지메뉴상품)
        );
    }

    @DisplayName("가격이 0원 미만인 메뉴를 생성하면 IllegalArgumentException을 반환한다.")
    @Test
    void createWithException() {
        // given
        MenuRequest menu = createMenu(1L, "짜장면_탕수육_1인_메뉴_세트", BigDecimal.valueOf(-1000L),
                짜장면_탕수육_1인_메뉴_세트.getId(), Arrays.asList(짜장면메뉴상품, 탕수육메뉴상품, 단무지메뉴상품));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("존재하지 않는 메뉴그룹이 포함된 메뉴를 생성하면 IllegalArgumentException을 반환한다.")
    @Test
    void createWithException2() {
        // given
        MenuRequest menu = createMenu(1L, "짜장면_탕수육_1인_메뉴_세트", BigDecimal.valueOf(20000L), 10L,
                Arrays.asList(짜장면메뉴상품, 탕수육메뉴상품, 단무지메뉴상품));
        when(menuGroupDao.existsById(10L)).thenReturn(false);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("존재하지 않는 상품이 포함된 메뉴를 생성하면 IllegalArgumentException을 반환한다.")
    @Test
    void createWithException3() {
        // given
        MenuRequest menu = createMenu(1L, "짜장면_탕수육_1인_메뉴_세트", BigDecimal.valueOf(20000L), 중국집_1인_메뉴_세트.getId(),
                singletonList(짜장면메뉴상품));
        when(productDao.findById(짜장면.getId())).thenReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴의 가격이 메뉴 상품들의 가격의 합보다 큰 메뉴를 생성하면 IllegalArgumentException을 반환한다.")
    @Test
    void createWithException4() {
        // given
        MenuRequest menu = createMenu(1L, "짜장면_탕수육_1인_메뉴_세트", BigDecimal.valueOf(21000L), 중국집_1인_메뉴_세트.getId(),
                Arrays.asList(짜장면메뉴상품, 탕수육메뉴상품, 단무지메뉴상품));
        when(productDao.findById(짜장면.getId())).thenReturn(Optional.of(짜장면));
        when(productDao.findById(탕수육.getId())).thenReturn(Optional.of(탕수육));
        when(productDao.findById(단무지.getId())).thenReturn(Optional.of(단무지));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴 전체 목록 조회 작업을 성공한다.")
    @Test
    void list() {
        // given
        List<Menu> menus = Arrays.asList(메뉴_세트_생성(짜장면_탕수육_1인_메뉴_세트_요청), 메뉴_세트_생성(짬뽕_탕수육_1인_메뉴_세트_요청));
        when(menuDao.findAll()).thenReturn(menus);
        when(menuProductDao.findAllByMenuId(짜장면_탕수육_1인_메뉴_세트.getId())).thenReturn(Arrays.asList(짜장면메뉴상품, 탕수육메뉴상품, 단무지메뉴상품));
        when(menuProductDao.findAllByMenuId(짬뽕_탕수육_1인_메뉴_세트.getId())).thenReturn(Arrays.asList(짜장면메뉴상품, 탕수육메뉴상품, 단무지메뉴상품));

        // when
        List<MenuResponse> findMenus = menuService.list();

        // then
        assertAll(
                () -> assertThat(findMenus).hasSize(menus.size())
        );
    }
}
