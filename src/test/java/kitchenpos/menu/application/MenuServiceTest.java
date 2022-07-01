package kitchenpos.menu.application;

import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.dao.MenuProductDao;
import kitchenpos.product.dao.ProductDao;
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

import static kitchenpos.common.ServiceTestFactory.메뉴생성;
import static kitchenpos.common.ServiceTestFactory.메뉴그룹생성;
import static kitchenpos.common.ServiceTestFactory.메뉴상품생성;
import static kitchenpos.common.ServiceTestFactory.상품생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;


@DisplayName("메뉴 서비스 테스트")
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

    private Product 지코바치킨;
    private MenuGroup 메뉴그룹;
    private MenuProduct 메뉴상품;
    private Menu 오늘의메뉴;

    @BeforeEach
    void setUp() {
        지코바치킨 = 상품생성(1L, "지코바치킨", BigDecimal.valueOf(20000));
        메뉴그룹 = 메뉴그룹생성(1L, "메뉴그룹");
        메뉴상품 = 메뉴상품생성(1L, 지코바치킨.getId(), 1L);
        오늘의메뉴 = 메뉴생성(1L, "오늘의메뉴", 지코바치킨.getPrice().getValue(), 메뉴그룹.getId(), Arrays.asList(메뉴상품));
    }

    @Test
    void 메뉴를_등록할_수_있다() {
        given(menuGroupDao.existsById(오늘의메뉴.getMenuGroupId())).willReturn(true);
        given(productDao.findById(메뉴상품.getProductId())).willReturn(Optional.of(지코바치킨));
        given(menuDao.save(오늘의메뉴)).willReturn(오늘의메뉴);
        given(menuProductDao.save(메뉴상품)).willReturn(메뉴상품);

        Menu createdMenu = menuService.create(오늘의메뉴);

        assertAll(
                () -> assertThat(createdMenu.getName()).isEqualTo(오늘의메뉴.getName()),
                () -> assertThat(createdMenu).isNotNull()
        );
    }

    @Test
    void 메뉴의_가격이_음수이면_메뉴를_등록할_수_없다() {
        Menu menu = 메뉴생성(100L, "메뉴이름", BigDecimal.valueOf(-1000), 메뉴그룹.getId(), Arrays.asList(메뉴상품));
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴그룹이_존재하지_않으면_메뉴를_등록할_수_없다() {
        long 존재하지않는_메뉴그룹ID = 1000L;
        Menu menu = 메뉴생성(100L, "메뉴이름", BigDecimal.valueOf(1000), 존재하지않는_메뉴그룹ID, Arrays.asList(메뉴상품));
        given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(false);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품이_존재하지않으면_메뉴를_등록할_수_없다() {
        MenuProduct 존재하지않는_메뉴상품 = new MenuProduct();
        Menu menu = 메뉴생성(100L, "메뉴이름", BigDecimal.valueOf(1000), 메뉴그룹.getId(), Arrays.asList(존재하지않는_메뉴상품));
        given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(true);
        given(productDao.findById(존재하지않는_메뉴상품.getProductId())).willThrow(IllegalArgumentException.class);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품들의_가격의_합보다_메뉴price가_크면_메뉴를_등록할_수_없다() {
        Product 특별상품 = 상품생성(2L, "특별상품", BigDecimal.valueOf(3000));
        Product 신선상품 = 상품생성(3L, "신선상품", BigDecimal.valueOf(4000));
        MenuProduct 특별메뉴상품 = 메뉴상품생성(3L, 특별상품.getId(), 1L);
        MenuProduct 신선메뉴상품 = 메뉴상품생성(4L, 신선상품.getId(), 1L);
        Menu 신상메뉴 = 메뉴생성(2L, "신상메뉴", BigDecimal.valueOf(10000), 메뉴그룹.getId(), Arrays.asList(특별메뉴상품, 신선메뉴상품));

        given(menuGroupDao.existsById(신상메뉴.getMenuGroupId())).willReturn(true);
        given(productDao.findById(특별메뉴상품.getProductId())).willReturn(Optional.of(특별상품));
        given(productDao.findById(신선메뉴상품.getProductId())).willReturn(Optional.of(신선상품));

        assertThatThrownBy(() -> menuService.create(신상메뉴))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴를_조회할_수_있다() {
        given(menuDao.findAll()).willReturn(Arrays.asList(오늘의메뉴));
        given(menuProductDao.findAllByMenuId(오늘의메뉴.getId()))
                .willReturn(Arrays.asList(메뉴상품));

        List<Menu> list = menuService.list();

        assertAll(
                () -> assertThat(list.size()).isEqualTo(1),
                () -> assertThat(list.get(0).getName()).isEqualTo("오늘의메뉴")
        );
    }



}
