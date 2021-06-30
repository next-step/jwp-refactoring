package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.*;
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

    private final Long simpleMenuId = 1L;
    private final Long simpleProductId = 1L;
    
    private MenuProduct simpleMenuProduct;
    
    @BeforeEach
    void setUp() {
        this.menuService = new MenuService(menuDao, menuGroupDao, productDao);

        this.simpleMenuProduct = new MenuProduct(1L, simpleMenuId, simpleProductId, 1L);
    }

    @Test
    @DisplayName("create - 메뉴의 메뉴 그룹이 DB에 없을경우 IllegalArgumetException 이 발생한다.")
    void 메뉴의_메뉴_그룹이_DB에_없을경우_IllegalArgumentException이_발생한다() {
        // given
        Long menuGroupId = 1L;
        Menu menu = new Menu(simpleMenuId,
                "Menu",
                BigDecimal.valueOf(0),
                menuGroupId,
                Arrays.asList(simpleMenuProduct));

        // when
        when(menuGroupDao.findById(menuGroupId)).thenReturn(Optional.empty());

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));

        verify(menuGroupDao, VerificationModeFactory.times(1)).findById(menuGroupId);
    }

    @Test
    @DisplayName("create - 메뉴 상품의 상품이 DB에 있는지 확인하고, 없으면 IllegalArgumentException 이 발생한다.")
    void 메뉴_상품의_상품이_DB에_있는지_확인하고_없으면_IllegalArgumentException이_발생한다() {
        // given
        Long menuGroupId = 1L;

        MenuGroup menuGroup = new MenuGroup(menuGroupId, "MenuGroup");

        Menu menu = new Menu(1L,
                "Menu",
                BigDecimal.valueOf(0),
                menuGroupId,
                Arrays.asList(simpleMenuProduct));

        given(menuGroupDao.findById(menuGroupId))
                .willReturn(Optional.of(menuGroup));

        // when
        when(productDao.findAllById(Arrays.asList(1L))).thenReturn(Arrays.asList());

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));

        verify(menuGroupDao, VerificationModeFactory.times(1))
                .findById(menuGroupId);
        verify(productDao, VerificationModeFactory.times(1))
                .findAllById(Arrays.asList(1L));
    }

    @Test
    @DisplayName("create - 메뉴의 가격이 메뉴 상품의 금액 합계보다 크면 IllegalArgumentException 이 발생한다.")
    void 메뉴의_가격이_메뉴_상품의_금액_합계보다_크면_IllegalArgumentException이_발생한다() {
        // given
        Long menuGroupId = 1L;

        MenuGroup menuGroup = new MenuGroup(menuGroupId, "Hello");

        Product product = new Product(simpleProductId, "PRODUCT", BigDecimal.valueOf(100));

        Menu menu = new Menu(1L, "Menu", BigDecimal.valueOf(1000), menuGroupId, Arrays.asList(simpleMenuProduct));


        given(menuGroupDao.findById(menuGroupId)).willReturn(Optional.of(menuGroup));
        when(productDao.findAllById(Arrays.asList(product.getId()))).thenReturn(Arrays.asList(product));

        // when & then

        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));

        verify(menuGroupDao, VerificationModeFactory.times(1)).findById(menuGroupId);
        verify(productDao, VerificationModeFactory.times(1)).findAllById(Arrays.asList(product.getId()));
    }

    @Test
    @DisplayName("create - 정상정인 메뉴 등록")
    void 정상적인_메뉴_등록() {
        // given
        Long menuGroupId = 1L;

        MenuGroup menuGroup = new MenuGroup(menuGroupId, "Hello");

        Product product = new Product(simpleProductId, "PRODUCT", BigDecimal.valueOf(100));

        Menu menu = new Menu(simpleMenuId,
                "Menu",
                BigDecimal.valueOf(10),
                menuGroupId,
                Arrays.asList(simpleMenuProduct));

        given(menuGroupDao.findById(menuGroupId)).willReturn(Optional.of(menuGroup));
        given(productDao.findAllById(any())).willReturn(Arrays.asList(product));

        // when
        when(menuDao.save(any())).thenAnswer(i -> i.getArgument(0));

        Menu savedMenu = menuService.create(menu);

        // then
        assertThat(savedMenu.getMenuProducts())
                .map(item -> item.getMenu())
                .containsOnly(savedMenu);

        assertThat(savedMenu.getMenuProducts())
                .map(item -> item.getProduct())
                .containsOnly(product);

        assertThat(savedMenu.getMenuProducts())
                .map(item -> item.getQuantity())
                .containsOnly(simpleMenuProduct.getQuantity());

        assertThat(savedMenu.getMenuProducts())
                .map(item -> item.getAmount())
                .containsOnly(new Price(product.getPrice().getPrice().multiply(BigDecimal.valueOf(simpleMenuProduct.getQuantity()))));

        verify(productDao, VerificationModeFactory.times(1))
                .findAllById(any());
        verify(menuDao, VerificationModeFactory.times(1)).save(savedMenu);
    }

    @Test
    @DisplayName("list - 정상적인 메뉴 전체 조회")
    void 정상적인_메뉴_전체_조회() {
        // given
        Long menuId = 1L;

        List<MenuProduct> menuProducts = Arrays.asList(
                new MenuProduct(1L, menuId, 1L, 1L),
                new MenuProduct(2L, menuId, 2L, 2L),
                new MenuProduct(3L, menuId, 3L, 3L)
        );

        Menu menu = new Menu(menuId, "Menu", BigDecimal.valueOf(1), 1L, menuProducts);

        // when
        when(menuDao.findAll()).thenReturn(Arrays.asList(menu));

        Menu resultMenu = menuService.list().get(0);
        // then
        assertThat(resultMenu).isEqualTo(menu);
        assertThat(resultMenu.getMenuProducts()).isEqualTo(menuProducts);
    }

}