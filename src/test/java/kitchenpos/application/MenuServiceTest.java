package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.*;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.fixture.MenuFixture.양념치킨_콜라_1000원_1개;
import static kitchenpos.fixture.ProductFixture.양념치킨_1000원;
import static kitchenpos.fixture.ProductFixture.콜라_100원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private ProductDao productDao;

    private MenuService menuService;

    private MenuGroup menuGroup;

    private MenuProductCreate 양념치킨;
    private MenuProductCreate 콜라;


    @BeforeEach
    void setUp() {
        this.menuService = new MenuService(menuDao, menuGroupDao, productDao);

        MenuFixture.cleanUp();
        ProductFixture.cleanUp();

        menuGroup = new MenuGroup(1L, "Hello");;

        양념치킨 = new MenuProductCreate(양념치킨_콜라_1000원_1개.getId(), 양념치킨_1000원.getId(), 1);
        콜라 = new MenuProductCreate(양념치킨_콜라_1000원_1개.getId(), 콜라_100원.getId(), 1);
    }

    @Test
    @DisplayName("create - 메뉴의 메뉴 그룹이 DB에 없을경우 IllegalArgumetException 이 발생한다.")
    void 메뉴의_메뉴_그룹이_DB에_없을경우_IllegalArgumentException이_발생한다() {
        // given
        List<MenuProductCreate> menuProductCreates = Arrays.asList(양념치킨, 콜라);

        MenuCreate menuCreate = new MenuCreate("menu", new Price(0), menuGroup.getId(), menuProductCreates);

        // when
        when(menuGroupDao.findById(menuGroup.getId())).thenReturn(Optional.empty());

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menuCreate));

        verify(menuGroupDao, VerificationModeFactory.times(1)).findById(menuGroup.getId());
    }

    @Test
    @DisplayName("create - 메뉴 상품의 상품이 DB에 있는지 확인하고, 없으면 IllegalArgumentException 이 발생한다.")
    void 메뉴_상품의_상품이_DB에_있는지_확인하고_없으면_IllegalArgumentException이_발생한다() {
        // given
        MenuCreate menuCreate = new MenuCreate("menu", new Price(0),
                menuGroup.getId(), Arrays.asList(양념치킨));

        given(menuGroupDao.findById(menuGroup.getId())).willReturn(Optional.of(menuGroup));

        // when
        when(productDao.findAllById(Arrays.asList(양념치킨.getProductId()))).thenReturn(Arrays.asList());

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menuCreate));

        verify(menuGroupDao, VerificationModeFactory.times(1))
                .findById(menuGroup.getId());
        verify(productDao, VerificationModeFactory.times(1))
                .findAllById(Arrays.asList(양념치킨_1000원.getId()));
    }

    @Test
    @DisplayName("create - 메뉴의 가격이 메뉴 상품의 금액 합계보다 크면 IllegalArgumentException 이 발생한다.")
    void 메뉴의_가격이_메뉴_상품의_금액_합계보다_크면_IllegalArgumentException이_발생한다() {
        // given
        MenuCreate menuCreate = new MenuCreate("menu", new Price(2000),
                menuGroup.getId(), Arrays.asList(양념치킨));


        given(menuGroupDao.findById(menuGroup.getId())).willReturn(Optional.of(menuGroup));
        when(productDao.findAllById(Arrays.asList(양념치킨_1000원.getId())))
                .thenReturn(Arrays.asList(양념치킨_1000원));

        // when & then

        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menuCreate));

        verify(menuGroupDao, VerificationModeFactory.times(1)).findById(menuGroup.getId());
        verify(productDao, VerificationModeFactory.times(1))
                .findAllById(Arrays.asList(양념치킨_1000원.getId()));
    }

    @Test
    @DisplayName("create - 정상정인 메뉴 등록")
    void 정상적인_메뉴_등록() {
        // given
        MenuCreate menuCreate = new MenuCreate("menu", new Price(10),
                menuGroup.getId(), Arrays.asList(양념치킨));

        given(menuGroupDao.findById(menuGroup.getId())).willReturn(Optional.of(menuGroup));
        given(productDao.findAllById(any())).willReturn(Arrays.asList(양념치킨_1000원));

        // when
        when(menuDao.save(any())).thenAnswer(i -> i.getArgument(0));

        Menu savedMenu = menuService.create(menuCreate);

        // then
        assertThat(savedMenu.getMenuProducts())
                .map(item -> item.getMenu())
                .containsOnly(savedMenu);

        assertThat(savedMenu.getMenuProducts())
                .map(item -> item.getProduct())
                .containsOnly(양념치킨_1000원);

        assertThat(savedMenu.getMenuProducts())
                .map(item -> item.getQuantity())
                .containsOnly(양념치킨.getQuantity());

        assertThat(savedMenu.getMenuProducts())
                .map(item -> item.getAmount())
                .containsOnly(new Price(양념치킨_1000원.getPrice().getPrice().multiply(BigDecimal.valueOf(1))));

        verify(productDao, VerificationModeFactory.times(1))
                .findAllById(any());
        verify(menuDao, VerificationModeFactory.times(1)).save(savedMenu);
    }

    @Test
    @DisplayName("list - 정상적인 메뉴 전체 조회")
    void 정상적인_메뉴_전체_조회() {
        // given
        List<MenuProduct> menuProducts = Arrays.asList(
                new MenuProduct(양념치킨_1000원, 1L),
                new MenuProduct(콜라_100원, 1L)
        );

        Menu menu = new Menu(1L, "Menu", new Price(1),null, menuProducts);

        // when
        when(menuDao.findAll()).thenReturn(Arrays.asList(menu));

        Menu resultMenu = menuService.list().get(0);
        // then
        assertThat(resultMenu).isEqualTo(menu);
        assertThat(resultMenu.getMenuProducts())
                .containsExactlyElementsOf(menuProducts);
    }

}