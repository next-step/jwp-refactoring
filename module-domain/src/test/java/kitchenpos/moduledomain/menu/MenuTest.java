package kitchenpos.moduledomain.menu;

import static java.util.Arrays.asList;
import static kitchenpos.moduledomain.common.MenuGroupFixture.메뉴그룹_한마리;
import static kitchenpos.moduledomain.common.MenuProductFixture.양념치킨_1개;
import static kitchenpos.moduledomain.common.MenuProductFixture.콜라_1개;
import static kitchenpos.moduledomain.common.ProductFixture.양념치킨;
import static kitchenpos.moduledomain.common.ProductFixture.콜라;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.moduledomain.common.exception.Message;
import kitchenpos.moduledomain.menu.Menu;
import kitchenpos.moduledomain.menu.MenuGroup;
import kitchenpos.moduledomain.menu.MenuGroupDao;
import kitchenpos.moduledomain.menu.MenuProduct;
import kitchenpos.moduledomain.menu.MenuProducts;
import kitchenpos.moduledomain.menu.MenuValidation;
import kitchenpos.moduledomain.product.Amount;
import kitchenpos.moduledomain.product.ProductDao;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuTest {

    @Mock
    private ProductDao productDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuValidation menuValidation;

    @Test
    void 메뉴_정상등록() {
        // given
        MenuGroup 메뉴그룹_한마리 = 메뉴그룹_한마리();
        Menu menu = Menu.of("양념치킨", Amount.of(18000), 메뉴그룹_한마리.getId());
        MenuProduct 양념치킨_1개 = 양념치킨_1개();
        MenuProduct 콜라_1개 = 콜라_1개();

        // when
        menu.addMenuProducts(MenuProducts.of(asList(양념치킨_1개, 콜라_1개)),
            new MenuValidation(null, null));

        // then
        assertAll(() -> {
            Assertions.assertThat(menu.getMenuGroupId()).isEqualTo(메뉴그룹_한마리.getId());
            Assertions.assertThat(menu.getName()).isEqualTo("양념치킨");
            Assertions.assertThat(menu.getPrice().getPrice()).isEqualTo(new BigDecimal("18000"));
            Assertions.assertThat(menu.getProducts()).contains(양념치킨_1개, 콜라_1개);
        });
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 메뉴의_이름값이_빈값이면_예외(String input) {
        MenuGroup 메뉴그룹_한마리 = 메뉴그룹_한마리();
        Assertions.assertThatThrownBy(() -> {
                Menu.of(input, Amount.of(10000), 메뉴그룹_한마리.getId());
            }).isInstanceOf(IllegalArgumentException.class)
            .hasMessage(Message.MENU_NAME_IS_NOT_NULL.getMessage());
    }

    /**
     * 양념치킨 가격 16000 콜라 1개 가격 2000
     */
    @Test
    void 메뉴생성시_등록한_상품의합보다_큰_금액을_입력시_예외() {
        // given
        MenuGroup 메뉴그룹_한마리 = 메뉴그룹_한마리();
        List<MenuProduct> menuProducts = asList(양념치킨_1개(), 콜라_1개());

        when(productDao.findById(anyLong())).thenReturn(Optional.of(양념치킨()), Optional.of(콜라()));

        Assertions.assertThatThrownBy(() -> {
            Menu menu = Menu.of(1L, "양념치킨", Amount.of(50000), 메뉴그룹_한마리.getId());
            menu.addMenuProducts(MenuProducts.of(menuProducts), menuValidation);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴그룹_ID가_비어있다면_예외() {
        // given
        List<MenuProduct> menuProducts = asList(양념치킨_1개(), 콜라_1개());

        when(productDao.findById(anyLong())).thenReturn(Optional.of(양념치킨()), Optional.of(콜라()));

        // then
        Assertions.assertThatThrownBy(() -> {
            Menu menu = Menu.of(1L, "양념치킨", Amount.of(14000), null);
            menu.addMenuProducts(MenuProducts.of(menuProducts), new MenuValidation(productDao, menuGroupDao));
        }).isInstanceOf(IllegalArgumentException.class);
    }

}