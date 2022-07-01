package kitchenpos.menu;

import kitchenpos.application.MenuService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
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

import static kitchenpos.util.testFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @InjectMocks
    MenuService menuService;

    @Mock
    MenuGroupDao menuGroupDao;

    @Mock
    ProductDao productDao;

    @Mock
    MenuProductDao menuProductDao;

    @Mock
    MenuDao menuDao;

    private MenuGroup 한마리메뉴;
    private Product 후라이드;
    private Product 양념;
    private MenuProduct 후라이드메뉴상품;
    private MenuProduct 양념메뉴상품;
    private Menu 후라이드치킨;
    private Menu 양념치킨;

    @BeforeEach
    void setUp() {
        한마리메뉴 = 한마리_메뉴_그룹_생성();

        후라이드 = 후라이드_상품_생성();
        양념 = 양념치킨_상품_생성();

        후라이드메뉴상품 = 후라이드_메뉴_상품_생성(후라이드.getId());
        양념메뉴상품 = 양념_메뉴_상품_생성(양념.getId());

        후라이드치킨 = 후라이드_치킨_메뉴_생성(한마리메뉴.getId(), Arrays.asList(후라이드메뉴상품));
        양념치킨 = 양념_치킨_메뉴_생성(한마리메뉴.getId(), Arrays.asList(양념메뉴상품));
    }

    @DisplayName("메뉴 등록")
    @Test
    void createMenu() {
        // given
        when(menuGroupDao.existsById(한마리메뉴.getId()))
                .thenReturn(true);
        when(productDao.findById(후라이드.getId()))
                .thenReturn(Optional.ofNullable(후라이드));
        when(menuProductDao.save(후라이드메뉴상품))
                .thenReturn(후라이드메뉴상품);
        when(menuDao.save(후라이드치킨))
                .thenReturn(후라이드치킨);

        // when
        Menu result = menuService.create(후라이드치킨);

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(후라이드치킨.getId()),
                () -> assertThat(result.getName()).isEqualTo(후라이드치킨.getName()),
                () -> assertThat(result.getPrice()).isEqualTo(후라이드치킨.getPrice()),
                () -> assertThat(result.getMenuGroupId()).isEqualTo(후라이드치킨.getMenuGroupId()),
                () -> assertThat(result.getMenuProducts()).isEqualTo(후라이드치킨.getMenuProducts())
        );
    }

    @DisplayName("메뉴 등록 시 가격이 NULL인 경우 등록 불가")
    @Test
    void createMenuAndIsNullPrice() {
        // given
        후라이드치킨.setPrice(null);

        // then
        assertThatThrownBy(() -> {
            menuService.create(후라이드치킨);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 시 가격이 `0`보다 작은 경우 등록 불가")
    @Test
    void createMenuAndIsNegativePrice() {
        // given
        후라이드치킨.setPrice(new BigDecimal(-16000));

        // then
        assertThatThrownBy(() -> {
            menuService.create(후라이드치킨);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 그룹이 등록되지 않은 경우 등록 불가")
    @Test
    void createMenuAndIsNotRegisterMenuGroup() {
        // given
        when(menuGroupDao.existsById(한마리메뉴.getId()))
                .thenReturn(false);

        // then
        assertThatThrownBy(() -> {
            menuService.create(후라이드치킨);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격이 상품 리스트의 총 가격보다 작은 경우 등록 불가")
    @Test
    void createMenuAndIsBiggerTotalProductPrice() {
        // given
        후라이드.setPrice(new BigDecimal(15000));
        when(menuGroupDao.existsById(한마리메뉴.getId()))
                .thenReturn(true);
        when(productDao.findById(후라이드.getId()))
                .thenReturn(Optional.ofNullable(후라이드));

        // then
        assertThatThrownBy(() -> {
            menuService.create(후라이드치킨);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 전체 조회")
    @Test
    void findAllMenus() {
        // given
        when(menuDao.findAll())
                .thenReturn(Arrays.asList(후라이드치킨, 양념치킨));

        // when
        List<Menu> list = menuService.list();

        // then
        assertAll(
                () -> assertThat(list).hasSize(2),
                () -> assertThat(list).containsExactly(후라이드치킨, 양념치킨)
        );
    }
}
