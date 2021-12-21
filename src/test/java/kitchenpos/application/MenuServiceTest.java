package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.assertj.core.api.Assertions;
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

import static kitchenpos.fixture.MenuFixture.강정치킨_두마리_셋트;
import static kitchenpos.fixture.MenuGroupFixture.추천_메뉴_그룹;
import static kitchenpos.fixture.MenuProductFixture.강정치킨_두마리;
import static kitchenpos.fixture.ProductFixture.강정치킨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    @DisplayName("메뉴 생성 성공 테스트")
    @Test
    void create_success() {
        // given
        MenuProduct 요청_메뉴_상품 = new MenuProduct();
        요청_메뉴_상품.setProductId(강정치킨.getId());
        요청_메뉴_상품.setQuantity(2);

        Menu 요청_메뉴 = new Menu();
        요청_메뉴.setName("강정치킨_두마리_셋트");
        요청_메뉴.setPrice(BigDecimal.valueOf(30_000));
        요청_메뉴.setMenuGroupId(추천_메뉴_그룹.getId());
        요청_메뉴.setMenuProducts(Arrays.asList(요청_메뉴_상품));

        when(menuGroupDao.existsById(추천_메뉴_그룹.getId())).thenReturn(true);
        when(productDao.findById(강정치킨.getId())).thenReturn(Optional.of(강정치킨));
        when(menuDao.save(any(Menu.class))).thenReturn(강정치킨_두마리_셋트);
        when(menuProductDao.save(any(MenuProduct.class))).thenReturn(강정치킨_두마리);

        // when
        Menu 생성된_메뉴 = menuService.create(요청_메뉴);

        // then
        assertAll(
                () -> assertThat(생성된_메뉴).isEqualTo(강정치킨_두마리_셋트)
                , () -> assertThat(생성된_메뉴.getMenuProducts()).containsExactly(강정치킨_두마리)
        );
    }

    @DisplayName("메뉴 생성 실패 테스트 - 메뉴 가격이 0보다 작음")
    @Test
    void create_failure_invalidPrice() {
        // given
        MenuProduct 요청_메뉴_상품 = new MenuProduct();
        요청_메뉴_상품.setProductId(강정치킨.getId());
        요청_메뉴_상품.setQuantity(2);

        Menu 요청_메뉴 = new Menu();
        요청_메뉴.setName("강정치킨_두마리_셋트");
        요청_메뉴.setPrice(BigDecimal.valueOf(-1));
        요청_메뉴.setMenuGroupId(추천_메뉴_그룹.getId());
        요청_메뉴.setMenuProducts(Arrays.asList(요청_메뉴_상품));

        // when & then
        Assertions.assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(요청_메뉴));
    }

    @DisplayName("메뉴 생성 실패 테스트 - 메뉴의 상품이 존재하지 않음")
    @Test
    void create_failure_notFoundProduct() {
        // given
        MenuProduct 요청_메뉴_상품 = new MenuProduct();
        요청_메뉴_상품.setProductId(강정치킨.getId());
        요청_메뉴_상품.setQuantity(2);

        Menu 요청_메뉴 = new Menu();
        요청_메뉴.setName("강정치킨_두마리_셋트");
        요청_메뉴.setPrice(BigDecimal.valueOf(30_000));
        요청_메뉴.setMenuGroupId(추천_메뉴_그룹.getId());
        요청_메뉴.setMenuProducts(Arrays.asList(요청_메뉴_상품));

        when(menuGroupDao.existsById(추천_메뉴_그룹.getId())).thenReturn(true);

        // when & then
        Assertions.assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(요청_메뉴));
    }

    @DisplayName("메뉴 생성 실패 테스트 - 메뉴가 메뉴 그룹에 속하지 않음")
    @Test
    void create_failure_notExistsMenuGroup() {
        // given
        MenuProduct 요청_메뉴_상품 = new MenuProduct();
        요청_메뉴_상품.setProductId(강정치킨.getId());
        요청_메뉴_상품.setQuantity(2);

        Menu 요청_메뉴 = new Menu();
        요청_메뉴.setName("강정치킨_두마리_셋트");
        요청_메뉴.setPrice(BigDecimal.valueOf(30_000));
        요청_메뉴.setMenuGroupId(추천_메뉴_그룹.getId());
        요청_메뉴.setMenuProducts(Arrays.asList(요청_메뉴_상품));

        when(menuGroupDao.existsById(추천_메뉴_그룹.getId())).thenReturn(false);

        // when & then
        Assertions.assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(요청_메뉴));
    }

    @DisplayName("메뉴 생성 실패 테스트 - 메뉴 가격이 각 메뉴 상품들 가격에 상품 수량을 곱해서 더한 금액을 초과")
    @Test
    void create_failure_exceedMenuPrice() {
        // given
        MenuProduct 요청_메뉴_상품 = new MenuProduct();
        요청_메뉴_상품.setProductId(강정치킨.getId());
        요청_메뉴_상품.setQuantity(2);

        Menu 요청_메뉴 = new Menu();
        요청_메뉴.setName("강정치킨_두마리_셋트");
        요청_메뉴.setPrice(BigDecimal.valueOf(51_000)); // 세마리 가격
        요청_메뉴.setMenuGroupId(추천_메뉴_그룹.getId());
        요청_메뉴.setMenuProducts(Arrays.asList(요청_메뉴_상품));

        when(menuGroupDao.existsById(추천_메뉴_그룹.getId())).thenReturn(true);
        when(productDao.findById(강정치킨.getId())).thenReturn(Optional.of(강정치킨));

        // when & then
        Assertions.assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(요청_메뉴));
    }

    @DisplayName("메뉴 목록 조회 테스트")
    @Test
    void list() {
        // given
        when(menuDao.findAll()).thenReturn(Arrays.asList(강정치킨_두마리_셋트));

        // when
        List<Menu> 조회된_메뉴_목록 = menuService.list();

        // then
        assertThat(조회된_메뉴_목록).containsExactly(강정치킨_두마리_셋트);
    }
}
