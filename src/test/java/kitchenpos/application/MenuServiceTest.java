package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

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

    @DisplayName("메뉴를 등록할 수 있다")
    @Test
    void 메뉴_등록() {
        // given
        MenuGroup 메뉴그룹 = new MenuGroup();
        메뉴그룹.setId(1L);
        메뉴그룹.setName("중식");

        Menu 메뉴 = new Menu();
        메뉴.setId(1L);
        메뉴.setName("짜장면");
        메뉴.setPrice(new BigDecimal("6000"));
        메뉴.setMenuGroupId(메뉴그룹.getId());
        
        Product 상품 = new Product();
        상품.setId(1L);
        상품.setName("짜장면");
        상품.setPrice(new BigDecimal("6000"));

        MenuProduct 메뉴상품 = new MenuProduct();
        메뉴상품.setSeq(1L);
        메뉴상품.setMenuId(메뉴.getId());
        메뉴상품.setProductId(상품.getId());
        메뉴상품.setQuantity(1L);

        메뉴.setMenuProducts(Arrays.asList(메뉴상품));
        given(menuGroupDao.existsById(메뉴.getMenuGroupId())).willReturn(true);
        given(productDao.findById(메뉴상품.getProductId())).willReturn(Optional.of(상품));
        given(menuDao.save(메뉴)).willReturn(메뉴);
        given(menuProductDao.save(메뉴상품)).willReturn(메뉴상품);

        // when
        Menu 저장된_메뉴 = menuService.create(메뉴);

        // then
        assertThat(저장된_메뉴).isEqualTo(메뉴);

    }

    @DisplayName("메뉴 등록시 가격은 필수여야한다 - 예외처리")
    @Test
    void 메뉴_등록_가격_필수() {
        // given
        Menu 가격없는_메뉴 = new Menu();
        가격없는_메뉴.setId(1L);
        가격없는_메뉴.setName("짜장면");
        
        // when
        가격없는_메뉴.setPrice(null);

        // then
        assertThatThrownBy(() -> {
            menuService.create(가격없는_메뉴);
        }).isInstanceOf(IllegalArgumentException.class);

    }
    
    @DisplayName("메뉴 등록시 가격은 0원 이상이어야한다 - 예외처리")
    @Test
    void 메뉴_등록_가격_0원_이상() {
        // given
        Menu 마이너스_가격_메뉴 = new Menu();
        마이너스_가격_메뉴.setId(1L);
        마이너스_가격_메뉴.setName("짜장면");
        
        // when
        마이너스_가격_메뉴.setPrice(new BigDecimal("-6000"));

        // then
        assertThatThrownBy(() -> {
            menuService.create(마이너스_가격_메뉴);
        }).isInstanceOf(IllegalArgumentException.class);
    }
    
    @DisplayName("메뉴 등록시 메뉴그룹이 지정되어 있어야한다 - 예외처리")
    @Test
    void 메뉴_등록_메뉴그룹_필수() {
        // given
        Menu 메뉴그룹_미지정_메뉴 = new Menu();
        메뉴그룹_미지정_메뉴.setId(1L);
        메뉴그룹_미지정_메뉴.setName("짜장면");
        메뉴그룹_미지정_메뉴.setPrice(new BigDecimal("6000"));
        
        // when
        메뉴그룹_미지정_메뉴.setMenuGroupId(null);

        // then
        assertThatThrownBy(() -> {
            menuService.create(메뉴그룹_미지정_메뉴);
        }).isInstanceOf(IllegalArgumentException.class);
    }
    
    @DisplayName("메뉴에 등록된 메뉴그룹은 등록된 메뉴그룹이어야한다 - 예외처리")
    @Test
    void 메뉴_등록_등록된_메뉴그룹만() {
        // given
        Menu 메뉴 = new Menu();
        메뉴.setId(1L);
        메뉴.setName("짜장면");
        메뉴.setPrice(new BigDecimal("6000"));
        메뉴.setMenuGroupId(1L);
        
        // when
        when(menuGroupDao.existsById(anyLong())).thenReturn(false);

        // when, then
        assertThatThrownBy(() -> {
            menuService.create(메뉴);
        }).isInstanceOf(IllegalArgumentException.class);

    }
    
    @DisplayName("메뉴에 포함된 상품은 등록된 상품이어야한다 - 예외처리")
    @Test
    void 메뉴_등록_등록된_상품만() {
        // given
        Menu 메뉴 = new Menu();
        메뉴.setId(1L);
        메뉴.setName("짜장면");
        메뉴.setPrice(new BigDecimal("6000"));
        메뉴.setMenuGroupId(1L);
        
        MenuProduct 메뉴상품 = new MenuProduct();
        메뉴상품.setSeq(1L);
        메뉴상품.setMenuId(메뉴.getId());
        메뉴상품.setProductId(1L);
        메뉴.setMenuProducts(Arrays.asList(메뉴상품));
        
        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        
        // when
        when(productDao.findById(anyLong())).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> {
            menuService.create(메뉴);
        }).isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("메뉴 등록시 가격은 포함된 상품들의 총 금액보다 클 수 없다- 예외처리")
    @Test
    void 메뉴_등록_금액_확인() {
        // given
        Menu 메뉴 = new Menu();
        메뉴.setId(1L);
        메뉴.setName("짜장면");
        메뉴.setPrice(new BigDecimal("10000"));
        메뉴.setMenuGroupId(1L);
        
        Product 상품 = new Product();
        상품.setId(1L);
        상품.setName("짜장면");
        상품.setPrice(new BigDecimal("6000"));
        
        MenuProduct 메뉴상품 = new MenuProduct();
        메뉴상품.setSeq(1L);
        메뉴상품.setMenuId(메뉴.getId());
        메뉴상품.setProductId(상품.getId());
        메뉴상품.setQuantity(1L);
        메뉴.setMenuProducts(Arrays.asList(메뉴상품));

        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(anyLong())).willReturn(Optional.of(상품));

        // when, then
        assertThatThrownBy(() -> {
            menuService.create(메뉴);
        }).isInstanceOf(IllegalArgumentException.class);
    }
    
    @DisplayName("메뉴 목록을 조회할 수 있다")
    @Test
    void 메뉴_목록_조회() {
        // given
        Menu 첫번째_메뉴 = new Menu();
        첫번째_메뉴.setId(1L);
        첫번째_메뉴.setName("짜장면");
        첫번째_메뉴.setPrice(new BigDecimal("6000"));
        
        Menu 두번째_메뉴 = new Menu();
        두번째_메뉴.setId(2L);
        두번째_메뉴.setName("짬뽕");
        두번째_메뉴.setPrice(new BigDecimal("7000"));

        given(menuDao.findAll()).willReturn(Arrays.asList(첫번째_메뉴, 두번째_메뉴));

        // when
        List<Menu> 메뉴_목록 = menuService.list();

        // then
        assertThat(메뉴_목록).containsExactly(첫번째_메뉴, 두번째_메뉴);
    }

}
