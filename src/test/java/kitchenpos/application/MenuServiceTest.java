package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuGroupRepository;
import kitchenpos.menu.MenuProduct;
import kitchenpos.menu.MenuRepository;
import kitchenpos.product.domain.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private MenuService menuService;

    private Product product1;
    private Product product2;
    private MenuProduct menuProduct1;
    private MenuProduct menuProduct2;

    @BeforeEach
    void setUp() {
        product1 = new Product(1L, "양념치킨", BigDecimal.valueOf(18000L));
        product2 = new Product(2L, "후라이드치킨", BigDecimal.valueOf(17000L));
        menuProduct1 = MenuProduct.of(1L, 1L, 1);
        menuProduct2 = MenuProduct.of(1L, 2L, 1);
    }

    @DisplayName("메뉴 생성")
    @Test
    void 메뉴_생성() {
        // given
        Menu menu = Menu.of("후라이드+양념", BigDecimal.valueOf(34000), 1L,
            Lists.newArrayList(menuProduct1, menuProduct2));

        given(menuGroupRepository.existsById(any())).willReturn(true);
        given(productDao.findById(1L)).willReturn(Optional.of(product1));
        given(productDao.findById(2L)).willReturn(Optional.of(product2));
        given(menuRepository.save(menu)).willReturn(menu);
        given(menuProductDao.save(menuProduct1)).willReturn(menuProduct1);
        given(menuProductDao.save(menuProduct2)).willReturn(menuProduct2);

        // when
        Menu result = menuService.create(menu);

        // then
        assertThat(result.getName()).isEqualTo("후라이드+양념");
        assertThat(result.getPrice()).isEqualTo(BigDecimal.valueOf(34000));
        assertThat(result.getMenuGroupId()).isEqualTo(1L);
        assertThat(result.getMenuProducts()).hasSize(2);
        assertThat(result.getMenuProducts().get(0).getProductId()).isEqualTo(1L);
        assertThat(result.getMenuProducts().get(1).getProductId()).isEqualTo(2L);

    }

    @DisplayName("메뉴의 가격이 없으면 예외 발생")
    @Test
    void 메뉴_생성_예외1() {
        // given
        Menu menu = Menu.of("후라이드+양념", null, 1L,
            Lists.newArrayList(menuProduct1, menuProduct2));

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> menuService.create(menu)
        );
    }

    @DisplayName("메뉴의 가격이 음수면 예외 발생")
    @Test
    void 메뉴_생성_예외2() {
        // given
        Menu menu = Menu.of("후라이드+양념", BigDecimal.valueOf(-20000), 1L,
            Lists.newArrayList(menuProduct1, menuProduct2));

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> menuService.create(menu)
        );
    }

    @DisplayName("단일 상품 가격의 합보다 메뉴의 가격이 높으면 예외 발생")
    @Test
    void 메뉴_생성_예외3() {
        // given
        Menu menu = Menu.of("후라이드+양념", BigDecimal.valueOf(36000), 1L,
            Lists.newArrayList(menuProduct1, menuProduct2));

        given(menuGroupRepository.existsById(any())).willReturn(true);
        given(productDao.findById(1L)).willReturn(Optional.of(product1));
        given(productDao.findById(2L)).willReturn(Optional.of(product2));

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> menuService.create(menu)
        );
    }

    @DisplayName("메뉴 목록 조회")
    @Test
    void 메뉴_목록_조회() {
        // given
        Menu menu1 = Menu.of("후라이드+양념", BigDecimal.valueOf(34000), 1L,
            Lists.newArrayList(menuProduct1, menuProduct2));

        MenuProduct menuProduct3 = MenuProduct.of(2L, 2L, 2);
        Menu menu2 = Menu.of("후라이드+후라이드", BigDecimal.valueOf(33000), 1L,
            Lists.newArrayList(menuProduct3));

        given(menuRepository.findAll()).willReturn(Lists.newArrayList(menu1, menu2));
        given(menuProductDao.findAllByMenuId(any())).willReturn(
            Lists.newArrayList(menuProduct1, menuProduct2),
            Lists.newArrayList(menuProduct3));

        // when
        List<Menu> result = menuService.list();

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("후라이드+양념");
        assertThat(result.get(0).getPrice()).isEqualTo(BigDecimal.valueOf(34000));
        assertThat(result.get(0).getMenuProducts()).hasSize(2);
        assertThat(result.get(1).getName()).isEqualTo("후라이드+후라이드");
        assertThat(result.get(1).getPrice()).isEqualTo(BigDecimal.valueOf(33000));
        assertThat(result.get(1).getMenuProducts()).hasSize(1);

    }

}
