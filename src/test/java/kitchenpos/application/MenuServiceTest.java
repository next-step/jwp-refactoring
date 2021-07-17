package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.math.BigDecimal.valueOf;
import static java.util.Arrays.asList;
import static kitchenpos.fixture.MenuFixture.메뉴_양념_후라이드_두마리_치킨_세트;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품_양념_치킨;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품_후라이드_치킨;
import static kitchenpos.fixture.ProductFixture.상품_양념_치킨;
import static kitchenpos.fixture.ProductFixture.상품_후라이드_치킨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.when;

@TestInstance(PER_CLASS)
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    private MenuService menuService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        this.menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
    }

    @MethodSource("methodSource_create_예외_유효하지_않은_메뉴가격")
    @ParameterizedTest
    void create_예외_유효하지_않은_메뉴가격(Menu menu) {
        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> menuService.create(menu));
    }

    Stream<Arguments> methodSource_create_예외_유효하지_않은_메뉴가격() {
        return Stream.of(
                Arguments.of(
                        new Menu(메뉴_양념_후라이드_두마리_치킨_세트.getId(),
                                메뉴_양념_후라이드_두마리_치킨_세트.getName(),
                                valueOf(-1),
                                메뉴_양념_후라이드_두마리_치킨_세트.getMenuGroupId(),
                                메뉴_양념_후라이드_두마리_치킨_세트.getMenuProducts())
                ),
                Arguments.of(
                        new Menu(메뉴_양념_후라이드_두마리_치킨_세트.getId(),
                                메뉴_양념_후라이드_두마리_치킨_세트.getName(),
                                null,
                                메뉴_양념_후라이드_두마리_치킨_세트.getMenuGroupId(),
                                메뉴_양념_후라이드_두마리_치킨_세트.getMenuProducts())
                )
        );
    }

    @Test
    void create_예외_존재하지_않는_메뉴그룹() {
        // when
        when(menuGroupDao.existsById(메뉴_양념_후라이드_두마리_치킨_세트.getMenuGroupId())).thenReturn(false);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> menuService.create(메뉴_양념_후라이드_두마리_치킨_세트));
    }

    @Test
    void create_예외_부적절한_메뉴_가격() {
        // given
        Menu 단품_보다_가격이_더_높은_가격의_메뉴 = new Menu(메뉴_양념_후라이드_두마리_치킨_세트.getId(),
                메뉴_양념_후라이드_두마리_치킨_세트.getName(),
                valueOf(38_000),
                메뉴_양념_후라이드_두마리_치킨_세트.getMenuGroupId(),
                메뉴_양념_후라이드_두마리_치킨_세트.getMenuProducts());

        // when
        when(productDao.findById(상품_후라이드_치킨.getId())).thenReturn(Optional.of(상품_후라이드_치킨));
        when(productDao.findById(상품_양념_치킨.getId())).thenReturn(Optional.of(상품_양념_치킨));
        when(menuGroupDao.existsById(메뉴_양념_후라이드_두마리_치킨_세트.getMenuGroupId())).thenReturn(true);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> menuService.create(단품_보다_가격이_더_높은_가격의_메뉴));
    }

    @MethodSource("methodSource_create_성공")
    @ParameterizedTest
    void create_성공(Menu menu) {
        // when
        when(productDao.findById(상품_후라이드_치킨.getId())).thenReturn(Optional.of(상품_후라이드_치킨));
        when(productDao.findById(상품_양념_치킨.getId())).thenReturn(Optional.of(상품_양념_치킨));

        when(menuGroupDao.existsById(menu.getMenuGroupId())).thenReturn(true);

        when(menuDao.save(menu)).thenReturn(menu);

        when(menuProductDao.save(메뉴_상품_후라이드_치킨)).thenReturn(메뉴_상품_후라이드_치킨);
        when(menuProductDao.save(메뉴_상품_양념_치킨)).thenReturn(메뉴_상품_양념_치킨);

        Menu createdMenu = menuService.create(menu);

        // then
        assertThat(createdMenu).isEqualTo(menu);
    }

    Stream<Arguments> methodSource_create_성공() {
        return Stream.of(
                Arguments.of(
                        new Menu(메뉴_양념_후라이드_두마리_치킨_세트.getId(),
                                메뉴_양념_후라이드_두마리_치킨_세트.getName(),
                                메뉴_양념_후라이드_두마리_치킨_세트.getPrice(),
                                메뉴_양념_후라이드_두마리_치킨_세트.getMenuGroupId(),
                                메뉴_양념_후라이드_두마리_치킨_세트.getMenuProducts())
                ),
                Arguments.of(
                        new Menu(메뉴_양념_후라이드_두마리_치킨_세트.getId(),
                                메뉴_양념_후라이드_두마리_치킨_세트.getName(),
                                메뉴_양념_후라이드_두마리_치킨_세트.getPrice().subtract(valueOf(3_000)),
                                메뉴_양념_후라이드_두마리_치킨_세트.getMenuGroupId(),
                                메뉴_양념_후라이드_두마리_치킨_세트.getMenuProducts())
                )
        );
    }

    @Test
    void list_성공() {
        // given
        List<Menu> expectedMenus = asList(메뉴_양념_후라이드_두마리_치킨_세트);

        // when
        when(menuDao.findAll()).thenReturn(asList(메뉴_양념_후라이드_두마리_치킨_세트));
        when(menuProductDao.findAllByMenuId(메뉴_양념_후라이드_두마리_치킨_세트.getId())).thenReturn(메뉴_양념_후라이드_두마리_치킨_세트.getMenuProducts());

        List<Menu> foundMenu = menuService.list();
        // then
        assertThat(foundMenu).isEqualTo(expectedMenus);
    }
}