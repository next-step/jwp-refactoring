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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    MenuDao menuDao;
    @Mock
    MenuGroupDao menuGroupDao;
    @Mock
    ProductDao productDao;
    @Mock
    MenuProductDao menuProductDao;

    MenuService menuService;

    MenuGroup 한마리메뉴;
    Product 후라이드치킨;
    Product 양념치킨;
    MenuProduct 반반세트후라이드;
    MenuProduct 반반세트양념;
    Menu 반반세트;

    @BeforeEach
    void setUp() {
        한마리메뉴 = new MenuGroup(1L, "한마리메뉴");
        후라이드치킨 = new Product(1L, "후라이드치킨", BigDecimal.valueOf(18000L));
        양념치킨 = new Product(2L, "양념치킨", BigDecimal.valueOf(19000L));
        반반세트후라이드 = new MenuProduct(후라이드치킨.getId(), 1);
        반반세트양념 = new MenuProduct(양념치킨.getId(), 1);
        반반세트 = new Menu("반반세트", BigDecimal.valueOf(35000L), 한마리메뉴.getId(),
                Arrays.asList(반반세트후라이드, 반반세트양념));

        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
    }

    @Test
    @DisplayName("정상적으로 메뉴 등록")
    void 정상적으로_메뉴_등록() {
        //given
        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(후라이드치킨.getId())).willReturn(Optional.of(후라이드치킨));
        given(productDao.findById(양념치킨.getId())).willReturn(Optional.of(양념치킨));
        given(menuProductDao.save(반반세트후라이드)).willReturn(반반세트후라이드);
        given(menuProductDao.save(반반세트양념)).willReturn(반반세트양념);
        given(menuDao.save(반반세트)).willReturn(new Menu(1L, 반반세트));

        //when
        Menu savedMenu = menuService.create(반반세트);

        //then
        assertThat(savedMenu.getId()).isEqualTo(1L);
        assertThat(savedMenu.getMenuProducts()).hasSize(2);
    }

    @Test
    @DisplayName("등록시 메뉴의 금액 입력이 잘못된 경우")
    void 메뉴_금액_오류() {
        //given
        Menu 반반세트 = new Menu("반반세트", BigDecimal.valueOf(-1000L), 한마리메뉴.getId(),
                Arrays.asList(반반세트후라이드, 반반세트양념));

        //when, then
        assertThatThrownBy(() -> menuService.create(반반세트)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("등록시 메뉴의 메뉴 그룹이 잘못 지정된 경우")
    void 메뉴_그룹_오류() {
        //given
        given(menuGroupDao.existsById(anyLong())).willReturn(false);

        //when, then
        assertThatThrownBy(() -> menuService.create(반반세트)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("등록시 메뉴의 상품이 등록이 안되어 있는 경우")
    void 상품_등록이_안되어_있음() {
        //given
        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(anyLong())).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> menuService.create(반반세트)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("세트메뉴 가격이 단일 메뉴 합한 가격보다 큰 경우")
    void 세트_메뉴_가격이_단일_메뉴_합한_가격보다_큰_경우() {
        //given
        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(후라이드치킨.getId())).willReturn(Optional.of(후라이드치킨));
        given(productDao.findById(양념치킨.getId())).willReturn(Optional.of(양념치킨));

        //when
        Menu 반반세트 = new Menu("반반세트", BigDecimal.valueOf(40000L), 한마리메뉴.getId(),
                Arrays.asList(반반세트후라이드, 반반세트양념));

        //then
        assertThatThrownBy(() -> menuService.create(반반세트)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 목록을 불러옴")
    void 메뉴_목록을_불러옴() {
        MenuGroup 파격할인메뉴 = new MenuGroup(1L, "파격할인메뉴");
        //given
        given(menuDao.findAll()).willReturn(Arrays.asList(
                new Menu(1L, "반반세트", BigDecimal.valueOf(40000L), 한마리메뉴.getId(),
                        Arrays.asList(반반세트후라이드, 반반세트양념)),
                new Menu(2L, "반반세트", BigDecimal.valueOf(1000L), 파격할인메뉴.getId(),
                        Arrays.asList(반반세트후라이드, 반반세트양념))
        ));

        //when, then
        assertThat(menuService.list()).hasSize(2);
    }

}
