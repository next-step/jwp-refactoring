package kitchenpos.application;

import static kitchenpos.domain.MenuGroupTest.메뉴그룹_생성;
import static kitchenpos.domain.MenuProductTest.메뉴상품_생성;
import static kitchenpos.domain.MenuTest.메뉴_생성;
import static kitchenpos.product.domain.ProductTest.상품_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.product.domain.Product;
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
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao MenuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private MenuService menuService;

    private Product 미역국;
    private Product 소머리국밥;
    private MenuGroup 식사;
    private MenuProduct 미역국_메뉴상품;
    private MenuProduct 소머리국밥_메뉴상품;
    private Menu 미역국_메뉴;
    private Menu 소머리국밥_메뉴;

    @BeforeEach
    public void setUp() {
        미역국 = 상품_생성(1L, "미역국", BigDecimal.valueOf(6000));
        소머리국밥 = 상품_생성(2L, "소머리국밥", BigDecimal.valueOf(8000));
        식사 = 메뉴그룹_생성(1L, "식사");

        미역국_메뉴상품 = 메뉴상품_생성(null, 미역국.getId(), 미역국.getId(), 1L);
        미역국_메뉴 = 메뉴_생성(1L, "미역국", BigDecimal.valueOf(6000), 식사.getId(), Arrays.asList(미역국_메뉴상품));

        소머리국밥_메뉴상품 = 메뉴상품_생성(null, 소머리국밥.getId(), 소머리국밥.getId(), 1L);
        소머리국밥_메뉴 = 메뉴_생성(2L, "소머리국밥", BigDecimal.valueOf(8000), 식사.getId(), Arrays.asList(소머리국밥_메뉴상품));
    }


    @Test
    @DisplayName("메뉴 생성")
    void create() {
        // given
        when(MenuGroupDao.existsById(any())).thenReturn(true);
        when(productDao.findById(any())).thenReturn(Optional.of(소머리국밥));
        when(menuDao.save(소머리국밥_메뉴)).thenReturn(소머리국밥_메뉴);
        when(menuProductDao.save(소머리국밥_메뉴상품)).thenReturn(소머리국밥_메뉴상품);

        // when
        Menu 소머리국밥_메뉴_생성_결과 = menuService.create(소머리국밥_메뉴);

        // then
        assertThat(소머리국밥_메뉴_생성_결과).isEqualTo(소머리국밥_메뉴);
    }

    @Test
    @DisplayName("메뉴 목록 조회")
    void list() {
        // given
        when(menuDao.findAll()).thenReturn(Arrays.asList(미역국_메뉴, 소머리국밥_메뉴));
        when(menuProductDao.findAllByMenuId(any())).thenReturn(Arrays.asList(미역국_메뉴상품, 소머리국밥_메뉴상품));

        // when
        List<Menu> menus = menuService.list();

        // then
        assertThat(menus).hasSize(2);
        assertThat(menus).contains(미역국_메뉴, 소머리국밥_메뉴);
    }
}