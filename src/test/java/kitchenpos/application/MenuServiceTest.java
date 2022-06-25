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

import static kitchenpos.fixture.MenuFixture.기본_메뉴;
import static kitchenpos.fixture.MenuFixture.추천_기본_메뉴;
import static kitchenpos.fixture.MenuProductFixture.메뉴_양념_치킨;
import static kitchenpos.fixture.MenuProductFixture.메뉴_치킨;
import static kitchenpos.fixture.ProductFixture.양념_치킨;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
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

    @Test
    @DisplayName("메뉴 등록 테스트")
    void create() {
        when(menuGroupDao.existsById(추천_기본_메뉴.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(양념_치킨.getId())).thenReturn(Optional.of(양념_치킨));
        when(menuDao.save(추천_기본_메뉴)).thenReturn(추천_기본_메뉴);
        when(menuProductDao.save(any(MenuProduct.class))).thenReturn(메뉴_양념_치킨);

        Menu 메뉴_등록_결과 = menuService.create(추천_기본_메뉴);

        Assertions.assertThat(메뉴_등록_결과).isEqualTo(추천_기본_메뉴);
    }

    @Test
    @DisplayName("메뉴 등록시 가격이 0원보다 미만인 경우 실패 테스트")
    void create2() {
        Menu 테스트_메뉴 = new Menu();
        테스트_메뉴.setPrice(BigDecimal.valueOf(-1000));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> menuService.create(테스트_메뉴));
    }

    @Test
    @DisplayName("메뉴 등록시 메뉴 그룹 ID가 없는 경우 실패 테스트")
    void create3() {
        Menu 테스트_메뉴 = new Menu();
        테스트_메뉴.setMenuGroupId(null);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> menuService.create(테스트_메뉴));
    }

    @Test
    @DisplayName("메뉴 등록시 상품 정보가 없는 경우 실패 테스트")
    void create4() {
        when(menuGroupDao.existsById(추천_기본_메뉴.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(양념_치킨.getId())).thenReturn(Optional.empty());

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> menuService.create(추천_기본_메뉴));
    }

    @Test
    @DisplayName("메뉴 등록시 상품 가격의 합보다 메뉴가격이 큰 경우 실패 테스트")
    void create5() {
        Menu 테스트_메뉴 = new Menu();
        테스트_메뉴.setPrice(BigDecimal.valueOf(50_000));
        테스트_메뉴.setMenuProducts(Arrays.asList(메뉴_양념_치킨));

        when(menuGroupDao.existsById(테스트_메뉴.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(양념_치킨.getId())).thenReturn(Optional.of(양념_치킨));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> menuService.create(테스트_메뉴));
    }

    @Test
    @DisplayName("메뉴 조회 테스트")
    void list() {
        when(menuDao.findAll()).thenReturn(Arrays.asList(기본_메뉴));
        when(menuProductDao.findAllByMenuId(기본_메뉴.getId())).thenReturn(Arrays.asList(메뉴_치킨));

        List<Menu> 메뉴_조회_테스트_결과 = menuService.list();

        assertAll(
                () -> Assertions.assertThat(메뉴_조회_테스트_결과).hasSize(1),
                () -> Assertions.assertThat(메뉴_조회_테스트_결과).containsExactly(기본_메뉴)
        );
    }
}
