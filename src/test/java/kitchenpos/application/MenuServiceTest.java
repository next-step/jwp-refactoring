package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuProductResponse;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.factory.MenuFixtureFactory;
import kitchenpos.factory.MenuGroupFixtureFactory;
import kitchenpos.factory.MenuProductFixtureFactory;
import kitchenpos.factory.ProductFixtureFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kitchenpos.factory.MenuFixtureFactory.*;
import static kitchenpos.factory.MenuGroupFixtureFactory.*;
import static kitchenpos.factory.MenuProductFixtureFactory.*;
import static kitchenpos.factory.ProductFixtureFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private MenuProductDao menuProductDao;
    @Mock
    private ProductDao productDao;
    @InjectMocks
    private MenuService menuService;

    private Menu 메뉴_김치찌개세트;
    private MenuGroup 메뉴그룹_한식;
    private Product 김치찌개;
    private Product 공기밥;
    private MenuProduct 김치찌개세트_김치찌개;
    private MenuProduct 김치찌개세트_공기밥;

    @BeforeEach
    void setUp() {
        메뉴그룹_한식 = createMenuGroup(1L, "한식메뉴");
        김치찌개 = createProduct(1L, "김치찌개", BigDecimal.valueOf(8000L));
        공기밥 = createProduct(2L, "공기밥", BigDecimal.valueOf(1000L));
        메뉴_김치찌개세트 = createMenu(1L, "김치찌개세트", BigDecimal.valueOf(15000L), 메뉴그룹_한식.getId());

        김치찌개세트_김치찌개 = createMenuProduct(1L, 메뉴_김치찌개세트.getId(), 김치찌개.getId(), 2);
        김치찌개세트_공기밥 = createMenuProduct(1L, 메뉴_김치찌개세트.getId(), 공기밥.getId(), 2);
        메뉴_김치찌개세트.setMenuProducts(Arrays.asList(김치찌개세트_김치찌개, 김치찌개세트_공기밥));
    }

    @DisplayName("메뉴를 등록할 수 있다")
    @Test
    void 메뉴_등록(){
        //given
        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(김치찌개.getId())).willReturn(Optional.of(김치찌개));
        given(productDao.findById(공기밥.getId())).willReturn(Optional.of(공기밥));
        given(menuDao.save(any(Menu.class))).willReturn(메뉴_김치찌개세트);
        MenuRequest 메뉴_김치찌개세트_request = MenuRequest.of(
                메뉴_김치찌개세트.getName(),
                메뉴_김치찌개세트.getPrice().intValue(),
                메뉴_김치찌개세트.getMenuGroupId(),
                메뉴_김치찌개세트.getMenuProducts().stream().
                        map(menuProduct -> MenuProductRequest.of(menuProduct.getProductId(), (int) menuProduct.getQuantity()))
                        .collect(Collectors.toList())
        );

        //when
        MenuResponse savedMenu = menuService.create(메뉴_김치찌개세트_request);

        //then
        assertThat(savedMenu).isEqualTo(MenuResponse.from(메뉴_김치찌개세트));
    }

    @DisplayName("메뉴의 가격은 0 이상이어야 한다")
    @Test
    void 메뉴_등록_가격_검증_0이상(){
        //when
        MenuRequest invalidMenu = MenuRequest.of(
                메뉴_김치찌개세트.getName(),
                -15000,
                메뉴_김치찌개세트.getMenuGroupId(),
                메뉴_김치찌개세트.getMenuProducts().stream().
                        map(menuProduct -> MenuProductRequest.of(menuProduct.getProductId(), (int) menuProduct.getQuantity()))
                        .collect(Collectors.toList())
        );

        //then
        assertThrows(IllegalArgumentException.class, () -> menuService.create(invalidMenu));
    }

    @DisplayName("메뉴의 가격은, 메뉴상품의 정가의 합보다 클 수 없다")
    @Test
    void 메뉴_등록_가격_검증_정가이하(){
        //given
        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(김치찌개.getId())).willReturn(Optional.of(김치찌개));
        given(productDao.findById(공기밥.getId())).willReturn(Optional.of(공기밥));

        //when
        MenuRequest invalidMenu = MenuRequest.of(
                메뉴_김치찌개세트.getName(),
                20000,
                메뉴_김치찌개세트.getMenuGroupId(),
                메뉴_김치찌개세트.getMenuProducts().stream().
                        map(menuProduct -> MenuProductRequest.of(menuProduct.getProductId(), (int) menuProduct.getQuantity()))
                        .collect(Collectors.toList())
        );

        //then
        assertThrows(IllegalArgumentException.class, () -> menuService.create(invalidMenu));
    }

    @DisplayName("등록하려는 메뉴그룹이 존재해야 한다")
    @Test
    void 메뉴_등록_메뉴그룹_검증(){
        //given
        given(menuGroupDao.existsById(anyLong())).willReturn(false);

        //when
        MenuRequest invalidMenu = MenuRequest.of(
                메뉴_김치찌개세트.getName(),
                메뉴_김치찌개세트.getPrice().intValue(),
                메뉴_김치찌개세트.getMenuGroupId(),
                메뉴_김치찌개세트.getMenuProducts().stream().
                        map(menuProduct -> MenuProductRequest.of(menuProduct.getProductId(), (int) menuProduct.getQuantity()))
                        .collect(Collectors.toList())
        );

        //then
        assertThrows(IllegalArgumentException.class, () -> menuService.create(invalidMenu));
    }

    @DisplayName("등록하려는 메뉴상품의 상품이 존재해야 한다")
    @Test
    void 메뉴_등록_메뉴상품_검증(){
        //given
        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(김치찌개.getId())).willReturn(Optional.ofNullable(null));
        MenuRequest invalidMenu = MenuRequest.of(
                메뉴_김치찌개세트.getName(),
                메뉴_김치찌개세트.getPrice().intValue(),
                메뉴_김치찌개세트.getMenuGroupId(),
                메뉴_김치찌개세트.getMenuProducts().stream().
                        map(menuProduct -> MenuProductRequest.of(menuProduct.getProductId(), (int) menuProduct.getQuantity()))
                        .collect(Collectors.toList())
        );

        //then
        assertThrows(IllegalArgumentException.class, () -> menuService.create(invalidMenu));
    }

    @DisplayName("메뉴의 목록을 조회할 수 있다")
    @Test
    void 메뉴_목록_조회(){
        //given
        given(menuDao.findAll()).willReturn(Arrays.asList(메뉴_김치찌개세트));

        //when
        List<MenuResponse> list = menuService.list();

        //then
        assertThat(list).containsExactly(MenuResponse.from(메뉴_김치찌개세트));
    }
}