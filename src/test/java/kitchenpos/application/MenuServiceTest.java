package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.fixture.TestMenuFactory;
import kitchenpos.fixture.TestMenuGroupFactory;
import kitchenpos.fixture.TestMenuProductFactory;
import kitchenpos.fixture.TestProductFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuDao menuDao;
    @Mock
    private MenuGroupDao menuGroupDao;
    @Mock
    private ProductDao productDao;
    @Mock
    private MenuProductDao menuProductDao;

    @InjectMocks
    private MenuService menuService;

    private MenuGroup 분식류;
    private Product 진매;
    private Product 진순이;
    private MenuProduct 메뉴_진매;
    private MenuProduct 메뉴_진순이;
    private Menu 메뉴;

    @BeforeEach
    void setUp() {
        분식류 = TestMenuGroupFactory.create(1L, "분식류");

        진매 = TestProductFactory.create(1L, "진라면 매운맛", 5_000);
        진순이 = TestProductFactory.create(2L, "진라면 순한맛", 5_000);

        메뉴 = TestMenuFactory.create(10L, 4_000, 분식류, "라면메뉴");

        메뉴_진매 = TestMenuProductFactory.create(메뉴, 진매, 1);
        메뉴_진순이 = TestMenuProductFactory.create(메뉴, 진매, 1);
        메뉴.setMenuProducts(Arrays.asList(메뉴_진매, 메뉴_진순이));
    }

    @Test
    @DisplayName("메뉴를 등록할 수 있다")
    void create() throws Exception {
        // given
        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(anyLong())).willReturn(Optional.of(진매));
        given(productDao.findById(anyLong())).willReturn(Optional.of(진순이));
        given(menuDao.save(any(Menu.class))).willReturn(메뉴);
        given(menuProductDao.save(any())).willReturn(메뉴_진매);
        given(menuProductDao.save(any())).willReturn(메뉴_진순이);

        // when
        Menu menu = menuService.create(메뉴);

        // then
        assertThat(menu).isEqualTo(메뉴);
    }

    @DisplayName("메뉴 가격은 0원 이상이어야 한다.")
    @ParameterizedTest
    @CsvSource(value = {"-1", "null"}, nullValues = {"null"})
    void createException1(BigDecimal price) throws Exception {
        // given
        메뉴.setPrice(price);

        // when & then
        assertThatThrownBy(() -> menuService.create(메뉴)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되지 않은 메뉴그룹은 예외가 발생한다")
    @Test
    void createException2() throws Exception {
        // given
        메뉴.setMenuGroupId(50L);

        // when & then
        assertThatThrownBy(() -> menuService.create(메뉴)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴상품 총합이 메뉴가격보다 높으면 예외가 발생한다")
    @Test
    void createException3() throws Exception {
        // given
        메뉴.setPrice(BigDecimal.valueOf(10001));
        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(anyLong())).willReturn(Optional.of(진순이));

        // when & then
        assertThatThrownBy(() -> menuService.create(메뉴)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록된 전체 메뉴를 조회한다")
    @Test
    void list() throws Exception {
        // given
        given(menuDao.findAll()).willReturn(Collections.singletonList(메뉴));

        // when
        List<Menu> list = menuService.list();

        // then
        assertThat(list).containsExactly(메뉴);
    }
}
