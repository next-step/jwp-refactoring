package kitchenpos.application;

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

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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

    private MenuGroup 패스트푸드_그룹;

    private Product 빅맥;
    private Product 감자튀김;
    private Product 콜라;

    private MenuProduct 빅맥_메뉴상품;
    private MenuProduct 감자튀김_메뉴상품;
    private MenuProduct 콜라_메뉴상품;
    private Menu 빅맥세트;



    @BeforeEach
    void init() {
        패스트푸드_그룹 = new MenuGroup(1L, "패스트푸드");
        빅맥 = new Product(1L, "빅맥", new BigDecimal(4000));
        감자튀김 = new Product(2L, "감자튀김", new BigDecimal(2000));
        콜라 = new Product(3L, "콜라", new BigDecimal(1000));

        빅맥세트 = new Menu(1L, "빅맥 세트", new BigDecimal(7000), 패스트푸드_그룹.getId(), Collections.emptyList());
        빅맥_메뉴상품 = new MenuProduct(빅맥세트.getId(), 빅맥.getId(), 1);
        감자튀김_메뉴상품 = new MenuProduct(빅맥세트.getId(), 감자튀김.getId(), 1);
        콜라_메뉴상품 = new MenuProduct(빅맥세트.getId(), 콜라.getId(), 1);
        빅맥세트.setMenuProducts(Arrays.asList(빅맥_메뉴상품, 감자튀김_메뉴상품, 콜라_메뉴상품));
    }

    @DisplayName("메뉴 등록 테스트")
    @Test
    void 메뉴_등록() {
        //given
        when(menuGroupDao.existsById(빅맥세트.getId())).thenReturn(true);
        when(productDao.findById(빅맥.getId())).thenReturn(Optional.ofNullable(빅맥));
        when(productDao.findById(감자튀김.getId())).thenReturn(Optional.ofNullable(감자튀김));
        when(productDao.findById(콜라.getId())).thenReturn(Optional.ofNullable(콜라));
        given(menuDao.save(any())).willReturn(빅맥세트);

        //when
        Menu 빅맥_세트_메뉴 = menuService.create(빅맥세트);

        //then
        assertAll(
                () -> assertThat(빅맥_세트_메뉴.getName()).isEqualTo("빅맥 세트"),
                () -> assertThat(빅맥_세트_메뉴.getPrice()).isEqualTo(new BigDecimal(7000))
        );

    }

    @DisplayName("메뉴 목록 조회 테스트")
    @Test
    void 메뉴_목록_조회() {
        //given
        Menu 불고기_버거_세트 = new Menu(2L, "불고기 버거 세트", new BigDecimal(6000), 패스트푸드_그룹.getId(), Collections.emptyList());
        given(menuDao.findAll()).willReturn(Arrays.asList(불고기_버거_세트, 빅맥세트));

        //when
        List<Menu> menuList = menuService.list();

        //then
        assertThat(menuList.size()).isEqualTo(2);
    }

    @DisplayName("메뉴 등록 가능 테스트")
    @Test
    void 메뉴_가격이_0보다_작을_경우_등록_에러() {
        //given
        빅맥세트 = new Menu(1L, "빅맥 세트", new BigDecimal(-1), 패스트푸드_그룹.getId(), Collections.emptyList());

        //when+then
        assertThrows(IllegalArgumentException.class, () -> menuService.create(빅맥세트));
    }

    @DisplayName("메뉴 그룹에 메뉴가 포함되지 않을 경우 메뉴를 등록할 수 없다.")
    @Test
    void 메뉴_그룹에_메뉴가_존재하지_않을_경우_등록_에러() {
        Menu 불고기_버거_세트 = new Menu(2L, "불고기 버거 세트", new BigDecimal(6000), 패스트푸드_그룹.getId(), Collections.emptyList());

        assertThrows(IllegalArgumentException.class, () -> menuService.create(불고기_버거_세트));
    }

    @DisplayName("메뉴의 가격이 메뉴 상품 가격의 합보다 클 경우 메뉴를 등록할 수 없다.")
    @Test
    void 메뉴_가격이_메뉴_가격의_합보다_클_경우_등록_에러() {
        Menu 뉴빅맥_세트 = new Menu(3L, "뉴빅맥 세트", new BigDecimal(8000), 패스트푸드_그룹.getId(), Collections.emptyList());
        뉴빅맥_세트.setMenuProducts(Arrays.asList(빅맥_메뉴상품, 감자튀김_메뉴상품, 콜라_메뉴상품));

        assertThrows(IllegalArgumentException.class, () -> menuService.create(뉴빅맥_세트));
    }
}
