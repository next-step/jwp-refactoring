package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.application.CommonTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
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

    private Product 싸이버거, 콜라;
    private MenuProduct 싱글세트_싸이버거, 더블세트_싸이버거, 세트_콜라;
    private MenuGroup 맘스세트메뉴;

    @BeforeEach
    void setUp() {
        싸이버거 = createProduct(1L, "싸이버거", 3500);
        콜라 = createProduct(2L, "콜라", 1500);
        싱글세트_싸이버거 = createMenuProduct(1, 1L, 싸이버거.getId(), 1);
        더블세트_싸이버거 = createMenuProduct(2, 1L, 싸이버거.getId(), 2);
        세트_콜라 = createMenuProduct(3, 1L, 콜라.getId(), 1);
        맘스세트메뉴 = createMenuGroup(1L, "맘스세트메뉴");
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void create_success() {
        // given
        Menu 싱글세트 = createMenu(1L, 맘스세트메뉴.getId(), "싱글세트", 5000, Lists.newArrayList(싱글세트_싸이버거, 세트_콜라));
        given(menuGroupDao.existsById(맘스세트메뉴.getId())).willReturn(true);
        given(productDao.findById(싸이버거.getId())).willReturn(Optional.of(싸이버거));
        given(productDao.findById(콜라.getId())).willReturn(Optional.of(콜라));
        given(menuProductDao.save(싱글세트_싸이버거)).willReturn(싱글세트_싸이버거);
        given(menuProductDao.save(세트_콜라)).willReturn(세트_콜라);
        given(menuDao.save(싱글세트)).willReturn(싱글세트);

        // when
        Menu saved = menuService.create(싱글세트);

        // then
        assertAll(
                () -> assertThat(saved).isNotNull(),
                () -> assertThat(saved.getName()).isEqualTo(싱글세트.getName()),
                () -> assertThat(saved.getPrice()).isEqualTo(싱글세트.getPrice())
        );
    }

    @DisplayName("메뉴 등록에 실패한다. (메뉴의 가격이 0 원 미만인 경우")
    @Test
    void create_fail() {
        // given
        Menu 싱글세트 = createMenu(1L, 맘스세트메뉴.getId(), "싱글세트", -1, Lists.newArrayList(싱글세트_싸이버거, 세트_콜라));

        // then
        assertThatThrownBy(() -> {
            menuService.create(싱글세트);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록에 실패한다. (메뉴가 속한 메뉴 그룹이 존재하지 않는 경우)")
    @Test
    void create_fail_invalidMenuGroup() {
        // given
        Menu 싱글세트 = createMenu(1L, 맘스세트메뉴.getId(), "싱글세트", 5000, Lists.newArrayList(싱글세트_싸이버거, 세트_콜라));
        given(menuGroupDao.existsById(맘스세트메뉴.getId())).willReturn(false);

        // then
        assertThatThrownBy(() -> {
            menuService.create(싱글세트);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록에 실패한다. (메뉴 상품 가격의 총 합이 메뉴 가격보다 큰 경우")
    @Test
    void create_fail_priceOver() {
        // given
        Menu 싱글세트 = createMenu(1L, 맘스세트메뉴.getId(), "싱글세트", 6000, Lists.newArrayList(싱글세트_싸이버거, 세트_콜라));
        given(menuGroupDao.existsById(맘스세트메뉴.getId())).willReturn(true);
        given(productDao.findById(싸이버거.getId())).willReturn(Optional.of(싸이버거));
        given(productDao.findById(콜라.getId())).willReturn(Optional.of(콜라));

        // then
        assertThatThrownBy(() -> {
            menuService.create(싱글세트);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        // given
        Menu 싱글세트 = createMenu(1L, 맘스세트메뉴.getId(), "싱글세트", 5000, Lists.newArrayList(싱글세트_싸이버거, 세트_콜라));
        Menu 더블세트 = createMenu(2L, 맘스세트메뉴.getId(), "싱글떡강정세트", 8500, Lists.newArrayList(더블세트_싸이버거, 세트_콜라));
        given(menuDao.findAll()).willReturn(Arrays.asList(싱글세트, 더블세트));

        // when
        List<Menu> menuList = menuService.list();

        // then
        assertThat(menuList).containsExactly(싱글세트, 더블세트);
    }
}
