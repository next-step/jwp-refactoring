package kitchenpos.application;

import static kitchenpos.domain.MenuProductTestFixture.*;
import static kitchenpos.domain.MenuTestFixture.menu;
import static kitchenpos.domain.ProductTestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 비즈니스 로직 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuDao menuDao;
    @Mock
    private MenuGroupDao menuGroupDao;
    @Mock
    private MenuProductDao menuProductDao;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private MenuService menuService;

    @Test
    @DisplayName("가격이 없는 메뉴를 등록한다.")
    void createMenuByPriceIsNull() {
        // given
        Menu 짜장1 = menu(1L, "짜장1", 1L, null, Collections.singletonList(짜장면_1그릇));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(짜장1));
    }

    @Test
    @DisplayName("가격이 0원 이하인 메뉴를 등록한다.")
    void createMenuByPriceLessThanZero() {
        // given
        Menu 짜장1 = menu(1L, "짜장1", 1L, BigDecimal.valueOf(-2000), Collections.singletonList(짜장면_1그릇));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(짜장1));
    }

    @Test
    @DisplayName("메뉴 그룹에 속해있지 않은 메뉴를 등록한다.")
    void createMenuByNotInMenuGroup() {
        // given
        MenuProduct 탕수육_소_1그릇 = menuProduct(2L, null, 5L, 1);
        Menu 탕수육소1 = menu(1L, "탕수육소1", 2L, BigDecimal.valueOf(18000), Collections.singletonList(탕수육_소_1그릇));
        given(menuGroupDao.existsById(2L)).willReturn(false);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(탕수육소1));
    }

    @Test
    @DisplayName("상품으로 등록되어 있지 않은 메뉴를 등록한다.")
    void createMenuByNotCreateProduct() {
        // given
        MenuProduct 탕수육_소_1그릇 = menuProduct(2L, null, 5L, 1);
        Menu 탕수육소1 = menu(1L, "탕수육소1", 2L, BigDecimal.valueOf(18000), Collections.singletonList(탕수육_소_1그릇));
        given(menuGroupDao.existsById(2L)).willReturn(true);
        given(productRepository.findById(5L)).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(탕수육소1));
    }

    @Test
    @DisplayName("메뉴 가격은 메뉴 상품가격의 합계보다 클 수 없다.")
    void createMenuByNotMoreThanProductsSum() {
        // given
        MenuProduct 짬뽕_2그릇 = menuProduct(2L, null, 2L, 2);
        Menu 짜장1_짬뽕2 = menu(1L, "짜장1_짬뽕2", 1L, BigDecimal.valueOf(25000), Arrays.asList(짜장면_1그릇, 짬뽕_2그릇));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(짜장1_짬뽕2));
    }
    
    @Test
    @DisplayName("메뉴를 등록한다.")
    void createMenu() {
        // given
        MenuProduct 짬뽕_2그릇 = menuProduct(2L, null, 2L, 2);
        Menu 짜장1_짬뽕2 = menu(1L, "짜장1_짬뽕2", 1L, BigDecimal.valueOf(23000), Arrays.asList(짜장면_1그릇, 짬뽕_2그릇));
        given(menuGroupDao.existsById(1L)).willReturn(true);
        given(productRepository.findById(1L)).willReturn(Optional.of(짜장면));
        given(productRepository.findById(2L)).willReturn(Optional.of(짬뽕));
        given(menuProductDao.save(짜장면_1그릇)).willReturn(짜장면_1그릇);
        given(menuProductDao.save(짬뽕_2그릇)).willReturn(짬뽕_2그릇);
        given(menuDao.save(짜장1_짬뽕2)).willReturn(짜장1_짬뽕2);

        // when
        Menu actual = menuService.create(짜장1_짬뽕2);

        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isInstanceOf(Menu.class)
        );
    }

    @Test
    @DisplayName("메뉴 목록을 조회하면 메뉴 목록이 반환된다.")
    void findMenus() {
        // given
        MenuProduct 짬뽕_2그릇 = menuProduct(2L, null, 2L, 2);
        Menu 짜장1 = menu(1L, "짜장1", 1L, BigDecimal.valueOf(7000), Collections.singletonList(짜장면_1그릇));
        Menu 짬뽕2 = menu(2L, "짬뽕2", 1L, BigDecimal.valueOf(16000), Collections.singletonList(짬뽕_2그릇));
        List<Menu> menus = Arrays.asList(짜장1, 짬뽕2);
        given(menuDao.findAll()).willReturn(menus);

        // when
        List<Menu> actual = menuService.list();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual).containsExactly(짜장1, 짬뽕2)
        );
    }
}
