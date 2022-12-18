package kitchenpos.Menu.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
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

import static kitchenpos.Menu.domain.MenuFixture.메뉴;
import static kitchenpos.Menu.domain.MenuGroupFixture.메뉴그룹;
import static kitchenpos.Menu.domain.MenuProductFixture.메뉴상품;
import static kitchenpos.Product.domain.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴 테스트")
public class MenuServiceTest {
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

    private Product 알리오올리오;
    private Product 봉골레오일;
    private Product 쉬림프로제;
    private Product 카프레제샐러드;
    private Product 레드와인;

    private MenuGroup 세트;
    private MenuGroup 코스;

    private Menu 오일2인세트;
    private Menu 풀코스;

    private MenuProduct 오일2인세트_알리오올리오;
    private MenuProduct 오일2인세트_봉골레오일;
    private MenuProduct 풀코스_카프레제샐러드;
    private MenuProduct 풀코스_알리오올리오;
    private MenuProduct 풀코스_쉬림프로제;
    private MenuProduct 풀코스_레드와인;

    @BeforeEach
    void setup() {
        알리오올리오 = 상품(1L, "알리오올리오", new BigDecimal(17000));
        봉골레오일 = 상품(2L, "봉골레오일", new BigDecimal(19000));
        쉬림프로제 = 상품(3L, "쉬림프로제", new BigDecimal(18000));
        카프레제샐러드 = 상품(4L, "카프레제샐러드", new BigDecimal(13000));
        레드와인 = 상품(5L, "레드와인", new BigDecimal(9000));

        세트 = 메뉴그룹(1L, "세트");
        코스 = 메뉴그룹(2L, "코스");;

        오일2인세트_알리오올리오 = 메뉴상품(1L, 1L, 1);
        오일2인세트_봉골레오일 = 메뉴상품(2L, 2L, 1);
        풀코스_카프레제샐러드 = 메뉴상품(3L, 4L, 1);
        풀코스_알리오올리오 = 메뉴상품(4L, 1L, 1);
        풀코스_쉬림프로제 = 메뉴상품(5L, 3L, 1);
        풀코스_레드와인 = 메뉴상품(6L, 5L, 2);

        오일2인세트 = 메뉴(1L, "오일2인세트", new BigDecimal(34000), 세트.getId(), Arrays.asList(오일2인세트_알리오올리오 ,오일2인세트_봉골레오일));
        풀코스 = 메뉴(2L, "풀코스", new BigDecimal(62000), 세트.getId(), Arrays.asList(오일2인세트_알리오올리오 ,오일2인세트_봉골레오일));
    }

    @DisplayName("메뉴를 생성한다")
    @Test
    void 메뉴_생성() {
        // given
        given(menuGroupDao.existsById(세트.getId())).willReturn(true);
        given(productDao.findById(오일2인세트_알리오올리오.getProductId())).willReturn(Optional.ofNullable(알리오올리오));
        given(productDao.findById(오일2인세트_봉골레오일.getProductId())).willReturn(Optional.ofNullable(봉골레오일));
        given(menuDao.save(any())).willReturn(오일2인세트);
        given(menuProductDao.save(오일2인세트_알리오올리오)).willReturn(오일2인세트_알리오올리오);
        given(menuProductDao.save(오일2인세트_봉골레오일)).willReturn(오일2인세트_봉골레오일);

        // when
        Menu menu = menuService.create(오일2인세트);

        // then
        verify(menuDao, times(1)).save(any());
        verify(menuProductDao, times(2)).save(any());
        assertAll(
                () -> assertThat(오일2인세트.getMenuProducts()).hasSize(2),
                () -> assertThat(오일2인세트.getMenuProducts()).containsExactly(오일2인세트_알리오올리오, 오일2인세트_봉골레오일),
                () -> assertThat(오일2인세트.getPrice()).isEqualTo(new BigDecimal(34000))
        );
    }

    @DisplayName("전체 메뉴 목록을 조회한다")
    @Test
    void 전체_메뉴_목록_조회() {
        // given
        given(menuDao.findAll()).willReturn(Arrays.asList(오일2인세트, 풀코스));
        given(menuProductDao.findAllByMenuId(오일2인세트.getId())).willReturn(Arrays.asList(오일2인세트_알리오올리오, 오일2인세트_봉골레오일));
        given(menuProductDao.findAllByMenuId(풀코스.getId()))
                .willReturn(Arrays.asList(풀코스_카프레제샐러드, 풀코스_알리오올리오, 풀코스_쉬림프로제, 풀코스_레드와인));

        // when
        List<Menu> menus = menuService.list();

        // then
        assertAll(
                () -> assertThat(menus).hasSize(2),
                () -> assertThat(menus).containsExactly(오일2인세트, 풀코스)
        );
    }

    @DisplayName("가격 정보가 없는 메뉴를 생성한다")
    @Test
    void 가격_정보가_없는_메뉴_생성() {
        // given & when
        오일2인세트.setPrice(null);

        // then
        assertThatThrownBy(
                () -> menuService.create(오일2인세트)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격이 음수인 메뉴를 생성한다")
    @Test
    void 가격이_음수인_메뉴_생성() {
        // given & when
        오일2인세트.setPrice(new BigDecimal(-34000));

        // then
        assertThatThrownBy(
                () -> menuService.create(오일2인세트)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 그룹 정보가 없는 메뉴를 생성한다")
    @Test
    void 메뉴_그룹_정보가_없는_메뉴_생성() {
        // given & when
        given(menuGroupDao.existsById(세트.getId())).willReturn(false);

        // then
        assertThatThrownBy(
                () -> menuService.create(오일2인세트)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 정보가 없는 메뉴를 생성한다")
    @Test
    void 상품_정보가_없는_메뉴_생성() {
        // given & when
        given(menuGroupDao.existsById(세트.getId())).willReturn(true);
        given(productDao.findById(오일2인세트_알리오올리오.getProductId())).willReturn(Optional.ofNullable(알리오올리오));
        given(productDao.findById(오일2인세트_봉골레오일.getProductId())).willReturn(Optional.ofNullable(null));

        // then
        assertThatThrownBy(
                () -> menuService.create(오일2인세트)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단일 상품 가격 합보다 메뉴 가격이 큰 메뉴를 생성한다")
    @Test
    void 단일_상품_가격_합보다_큰_메뉴_가격의_메뉴_생성() {
        // given
        given(menuGroupDao.existsById(세트.getId())).willReturn(true);
        given(productDao.findById(오일2인세트_알리오올리오.getProductId())).willReturn(Optional.ofNullable(알리오올리오));
        given(productDao.findById(오일2인세트_봉골레오일.getProductId())).willReturn(Optional.ofNullable(봉골레오일));

        // when
        오일2인세트.setPrice(new BigDecimal(100000));

        // then
        assertThatThrownBy(
                () -> menuService.create(오일2인세트)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
