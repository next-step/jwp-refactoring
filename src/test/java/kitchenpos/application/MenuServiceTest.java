package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

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

    private Menu givenMenu = new Menu();
    private MenuProduct givenMenuProduct = new MenuProduct();
    private Product givenProduct = new Product();

    @BeforeEach
    void setUp() {
        menuProductSetUp();
        productSetUp();
        givenMenu.setId(1L);
        givenMenu.setName("샘플메뉴");
        givenMenu.setMenuGroupId(1L);
        givenMenu.setPrice(BigDecimal.TEN);
        givenMenu.setMenuProducts(Arrays.asList(givenMenuProduct));
    }

    private void productSetUp() {
        givenProduct.setId(1L);
        givenProduct.setPrice(BigDecimal.TEN);
        givenProduct.setName("샘플상품");
    }

    private void menuProductSetUp() {
        givenMenuProduct.setMenuId(1L);
        givenMenuProduct.setSeq(1L);
        givenMenuProduct.setQuantity(1L);
        givenMenuProduct.setProductId(1L);
    }

    @DisplayName("가격이 0원 이상이어야 한다")
    @Test
    void createFailBecauseOfWrongPriceTest() {
        //given
        givenMenu.setPrice(null);

        //when && //then
        assertThatThrownBy(() -> menuService.create(givenMenu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("메뉴 가격은 0원 이상이어야 합니다");

        //given
        givenMenu.setPrice(BigDecimal.valueOf(-1));

        //when && //then
        assertThatThrownBy(() -> menuService.create(givenMenu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("메뉴 가격은 0원 이상이어야 합니다");
    }

    @DisplayName("메뉴 그룹이 지정되어있지 않으면 실패")
    @Test
    void createFailBecauseOfEmptyGroupMenuTest() {
        //given
        given(menuGroupDao.existsById(givenMenu.getId())).willReturn(false);

        //when && //then
        assertThatThrownBy(() -> menuService.create(givenMenu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("메뉴 그룹이 없는 메뉴는 등록할 수 없습니다.");

    }

    @DisplayName("상품이 있는 메뉴만 등록 가능하다")
    @Test
    void createFailBecauseOfNotExistProductMenuTest() {
        //given
        given(menuGroupDao.existsById(givenMenu.getId())).willReturn(true);
        given(productDao.findById(givenMenuProduct.getProductId())).willReturn(Optional.empty());
        //when && //then
        assertThatThrownBy(() -> menuService.create(givenMenu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("등록된 상품이 아닌 메뉴는 등록할 수 없습니다.");

    }

    @DisplayName("메뉴 가격이 상품 가격의 총 합보다 클 수 없다")
    @Test
    void createFailBecauseOfMissMatchProductPriceMenuTest() {
        //given
        given(menuGroupDao.existsById(givenMenu.getId())).willReturn(true);
        given(productDao.findById(givenMenuProduct.getProductId())).willReturn(Optional.ofNullable(givenProduct));
        givenMenu.setPrice(BigDecimal.valueOf(11));

        //when && //then
        assertThatThrownBy(() -> menuService.create(givenMenu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("메뉴 가격은 상품 가격의 총 합보다 클 수 없습니다.");

    }

    @DisplayName("메뉴를 등록한다")
    @Test
    void createTest() {
        //given
        given(menuGroupDao.existsById(givenMenu.getId())).willReturn(true);
        given(productDao.findById(givenMenuProduct.getProductId())).willReturn(Optional.ofNullable(givenProduct));
        given(menuDao.save(givenMenu)).willReturn(givenMenu);
        given(menuProductDao.save(givenMenuProduct)).willReturn(givenMenuProduct);

        //when
        menuService.create(givenMenu);

        //then
        verify(menuDao).save(givenMenu);
        verify(menuProductDao).save(givenMenuProduct);
    }

    @DisplayName("메뉴 목록을 조회할 수 있다 ")
    @Test
    void list() {
        //given
        List<Menu> expect = Arrays.asList(givenMenu);
        given(menuDao.findAll())
                .willReturn(expect);

        //when
        List<Menu> result = menuService.list();

        //then
        verify(menuDao).findAll();
        assertThat(result.size()).isEqualTo(expect.size());
        assertThat(result).containsExactly(givenMenu);
    }



}