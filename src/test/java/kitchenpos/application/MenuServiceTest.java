package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.repository.ProductRepository;
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

import static kitchenpos.factory.MenuFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    MenuDao menuDao;

    @Mock
    MenuGroupDao menuGroupDao;

    @Mock
    MenuProductDao menuProductDao;

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    MenuService menuService;

    MenuProduct 양념치킨한마리;
    MenuProduct 간장치킨한마리;

    MenuGroup 한마리메뉴;
    Product 양념치킨;
    Product 간장치킨;
    Menu 치킨;

    @Test
    @DisplayName("메뉴를 생성한다 (Happy Path)")
    void create() {
        //given
        한마리메뉴 = 메뉴그룹_생성(1L, "한마리메뉴");
        양념치킨 = 상품생성(1L, "양념치킨", 15000);
        간장치킨 = 상품생성(2L, "간장치킨", 15000);
        양념치킨한마리 = 메뉴_상품_생성(양념치킨);
        간장치킨한마리 = 메뉴_상품_생성(간장치킨);
        치킨 = new Menu(1L, "치킨", new BigDecimal(15000), 한마리메뉴.getId(), Arrays.asList(양념치킨한마리, 간장치킨한마리));
        given(menuDao.save(any(Menu.class))).willReturn(치킨);
        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productRepository.findById(양념치킨한마리.getProductId())).willReturn(Optional.of(양념치킨));
        given(productRepository.findById(간장치킨한마리.getProductId())).willReturn(Optional.of(간장치킨));

        //when
        Menu savedMenu = menuService.create(치킨);

        //then
        assertThat(savedMenu).isNotNull()
                .satisfies(menu -> {
                            menu.getId().equals(치킨.getId());
                            menu.getName().equals(치킨.getName());
                            menu.getMenuGroupId().equals(한마리메뉴.getId());
                            menu.getMenuProducts().containsAll(Arrays.asList(양념치킨한마리, 간장치킨한마리));
                        }
                );
    }

    @Test
    @DisplayName("유효하지 않은 메뉴그룹으로 메뉴 생성은 불가능하다.")
    void createInvalidMenuGroup() {
        한마리메뉴 = 메뉴그룹_생성(1L, "한마리메뉴");
        양념치킨 = 상품생성(1L, "양념치킨", 15000);
        간장치킨 = 상품생성(2L, "간장치킨", 15000);
        양념치킨한마리 = 메뉴_상품_생성(양념치킨);
        간장치킨한마리 = 메뉴_상품_생성(간장치킨);

        //given
        치킨 = new Menu(1L, "치킨", new BigDecimal(15000), 2L, Arrays.asList(양념치킨한마리, 간장치킨한마리));
        given(menuGroupDao.existsById(anyLong())).willReturn(false);

        //then
        assertThatThrownBy(() -> {
            menuService.create(치킨);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("유효하지 않은 금액(0원 미만)으로 메뉴 생성은 불가능하다.")
    void createInvalidPrice() {
        //given
        한마리메뉴 = 메뉴그룹_생성(1L, "한마리메뉴");
        양념치킨 = 상품생성(1L, "양념치킨", 15000);
        간장치킨 = 상품생성(2L, "간장치킨", 15000);
        양념치킨한마리 = 메뉴_상품_생성(양념치킨);
        간장치킨한마리 = 메뉴_상품_생성(간장치킨);
        치킨 = new Menu(1L, "치킨", new BigDecimal(-1), 2L, Arrays.asList(양념치킨한마리, 간장치킨한마리));

        //then
        assertThatThrownBy(() -> {
            menuService.create(치킨);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 가격이, 메뉴에 포함된 상품들의 합보다 비싸면 메뉴 생성이 불가")
    void createInvalidProduct() {
        //given
        한마리메뉴 = 메뉴그룹_생성(1L, "한마리메뉴");
        양념치킨 = 상품생성(1L, "양념치킨", 15000);
        간장치킨 = 상품생성(2L, "간장치킨", 15000);
        양념치킨한마리 = 메뉴_상품_생성(양념치킨);
        간장치킨한마리 = 메뉴_상품_생성(간장치킨);
        치킨 = new Menu(1L, "치킨", new BigDecimal(50000), 2L, Arrays.asList(양념치킨한마리, 간장치킨한마리));
        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productRepository.findById(양념치킨한마리.getProductId())).willReturn(Optional.of(양념치킨));
        given(productRepository.findById(간장치킨한마리.getProductId())).willReturn(Optional.of(간장치킨));

        //then
        assertThatThrownBy(() -> {
            menuService.create(치킨);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("유효하지 않은 상품으로 메뉴 생성은 불가능하다.")
    void createDiffMenuProductSum() {
        //given
        한마리메뉴 = 메뉴그룹_생성(1L, "한마리메뉴");
        양념치킨 = 상품생성(1L, "양념치킨", 15000);
        간장치킨 = 상품생성(2L, "간장치킨", 15000);
        양념치킨한마리 = 메뉴_상품_생성(양념치킨);
        간장치킨한마리 = 메뉴_상품_생성(간장치킨);
        치킨 = new Menu(1L, "치킨", new BigDecimal(15000), 2L, Arrays.asList(양념치킨한마리, 간장치킨한마리));
        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productRepository.findById(양념치킨한마리.getProductId())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> {
            menuService.create(치킨);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        //given
        한마리메뉴 = 메뉴그룹_생성(1L, "한마리메뉴");
        양념치킨 = 상품생성(1L, "양념치킨", 15000);
        간장치킨 = 상품생성(2L, "간장치킨", 15000);
        양념치킨한마리 = 메뉴_상품_생성(양념치킨);
        간장치킨한마리 = 메뉴_상품_생성(간장치킨);
        치킨 = new Menu(1L, "치킨", new BigDecimal(15000), 한마리메뉴.getId(), Arrays.asList(양념치킨한마리, 간장치킨한마리));
        given(menuDao.findAll()).willReturn(Arrays.asList(치킨));

        //when
        List<Menu> menus = menuService.list();

        //then
        assertThat(menus).containsExactlyInAnyOrderElementsOf(Arrays.asList(치킨));
    }
}