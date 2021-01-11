package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static kitchenpos.util.TestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@SpringBootTest
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

    @BeforeEach
    void setUp() {
        후라이드_1개 = menuProduct_생성(1L, 후라이드.getId(), 1);
        양념치킨_1개 = menuProduct_생성(1L, 양념치킨.getId(), 1);
        menu = menu_생성(1L, "후라이드양념치킨", 16000, 두마리_메뉴그룹.getId(), Arrays.asList(후라이드_1개, 양념치킨_1개));
    }

    /**
     * Menu: 후라이드_양념_치킨_메뉴
     *  ㄴ MenuGroup: 두마리_메뉴그룹
     *  ㄴ Price: 16,000
     *  ㄴ MenuProducts:
     *      1. MenuProduct 후라이드_1개
     *          ㄴ Price: 16000
     *      2. MenuProduct 양념치킨_1개
     *          ㄴ Price: 16000
     **/
    @DisplayName("메뉴를 등록한다.")
    @Test
    void createMenu() {
        given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(true);
        given(productDao.findById(후라이드_1개.getProductId())).willReturn(Optional.of(후라이드));
        given(productDao.findById(양념치킨_1개.getProductId())).willReturn(Optional.of(양념치킨));
        given(menuDao.save(menu)).willReturn(menu);
        given(menuProductDao.save(후라이드_1개)).willReturn(menuProduct에_menuId_추가(후라이드_1개, menu.getId()));
        given(menuProductDao.save(양념치킨_1개)).willReturn(menuProduct에_menuId_추가(양념치킨_1개, menu.getId()));

        Menu result = menuService.create(menu);

        assertThat(result).isEqualTo(menu);
        assertThat(result.getMenuProducts().get(0).getMenuId()).isEqualTo(menu.getId());
        assertThat(result.getMenuProducts().get(1).getMenuId()).isEqualTo(menu.getId());
    }

    @DisplayName("메뉴 가격을 설정 안했을 경우 등록하지 못한다.")
    @Test
    void createMenuException1() {
        menu.setPrice(null);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격이 0보다 작을 경우 등록하지 못한다.")
    @Test
    void createMenuException2() {
        menu.setPrice(BigDecimal.valueOf(-1));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 그룹이 등록되어 있지 않을 경우 등록하지 못한다.")
    @Test
    void createMenuException3() {
        given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(false);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 상품들 중 하나라도 등록되어 있지 않으면 등록하지 못한다.")
    @Test
    void createMenuException4() {
        given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(true);
        given(productDao.findById(후라이드_1개.getProductId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격이 메뉴 상품들의 총 금액보다 크면 등록하지 못한다.")
    @Test
    void createMenuException5() {
        given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(true);
        given(productDao.findById(후라이드_1개.getProductId())).willReturn(Optional.of(후라이드));
        given(productDao.findById(양념치킨_1개.getProductId())).willReturn(Optional.of(양념치킨));
        menu.setPrice(BigDecimal.valueOf(33000));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
