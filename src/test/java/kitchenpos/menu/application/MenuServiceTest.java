package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.dao.MenuProductRepository;
import kitchenpos.menu.dao.MenuRepository;
import kitchenpos.menu.dao.ProductRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupService menuGroupService;

    @Mock
    private MenuProductRepository menuProductRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuService menuService;

    @DisplayName("메뉴를 등록할 수 있다")
    @Test
    void 메뉴_등록() {
        // given
        Menu 메뉴 = Menu.of("짜장면", new BigDecimal("6000"), MenuGroup.from("중식"), null);
        Product 상품 = Product.of("짜장면", new BigDecimal("6000"));
        MenuProduct 메뉴상품 = MenuProduct.of(메뉴, 상품, 1L);
        메뉴.addMenuProducts(Arrays.asList(메뉴상품));

        given(menuGroupService.findById(nullable(Long.class))).willReturn(메뉴.getMenuGroup());
        given(productRepository.findById(nullable(Long.class))).willReturn(Optional.of(상품));
        given(menuRepository.save(any())).willReturn(메뉴);
        given(menuProductRepository.save(메뉴상품)).willReturn(메뉴상품);

        // when
        MenuResponse 저장된_메뉴 = menuService.create(MenuRequest.from(메뉴));

        // then
        assertThat(저장된_메뉴).isEqualTo(MenuResponse.from(메뉴));

    }
    
    @DisplayName("메뉴 등록시 가격은 필수여야한다 - 예외처리")
    @Test
    void 메뉴_등록_가격_필수() {
        // given
        Menu 가격없는_메뉴 = Menu.of("짜장면", null, MenuGroup.from("메뉴그룹"), new ArrayList<MenuProduct>());
        given(menuGroupService.findById(nullable(Long.class))).willReturn(가격없는_메뉴.getMenuGroup());
        
        // when, then
        assertThatThrownBy(() -> {
            menuService.create(MenuRequest.from(가격없는_메뉴));
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("메뉴 가격은 0원 이상이어야 합니다");
    
    }
    
    @DisplayName("메뉴 등록시 가격은 0원 이상이어야한다 - 예외처리")
    @Test
    void 메뉴_등록_가격_0원_이상() {
        // given
        Menu 마이너스_가격_메뉴 = Menu.of("짜장면", new BigDecimal("-6000"), MenuGroup.from("메뉴그룹"), new ArrayList<MenuProduct>());
        given(menuGroupService.findById(nullable(Long.class))).willReturn(마이너스_가격_메뉴.getMenuGroup());
        
        // when, then
        assertThatThrownBy(() -> {
            menuService.create(MenuRequest.from(마이너스_가격_메뉴));
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("메뉴 가격은 0원 이상이어야 합니다");
    }
    
    @DisplayName("메뉴 등록시 메뉴그룹이 지정되어 있어야한다 - 예외처리")
    @Test
    void 메뉴_등록_메뉴그룹_필수() {
        // given
        Menu 메뉴그룹_미지정_메뉴 = Menu.of("짜장면", new BigDecimal("6000"), null, new ArrayList<MenuProduct>());
        given(menuGroupService.findById(nullable(Long.class))).willReturn(메뉴그룹_미지정_메뉴.getMenuGroup());
        
        // when, then
        assertThatThrownBy(() -> {
            menuService.create(MenuRequest.from(메뉴그룹_미지정_메뉴));
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("해당하는 메뉴그룹이 없습니다");
    }
    @DisplayName("메뉴에 등록된 메뉴그룹은 등록된 메뉴그룹이어야한다 - 예외처리")
    @Test
    void 메뉴_등록_등록된_메뉴그룹만() {
        // given
        Menu 메뉴 = Menu.of("짜장면", new BigDecimal("6000"), MenuGroup.from("미등록_메뉴그룹"), new ArrayList<MenuProduct>());
    
        // when, then
        assertThatThrownBy(() -> {
            menuService.create(MenuRequest.from(메뉴));
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("해당하는 메뉴그룹이 없습니다");
    
    }
    
    
    @DisplayName("메뉴에 포함된 상품은 등록된 상품이어야한다 - 예외처리")
    @Test
    void 메뉴_등록_등록된_상품만() {
        // given
        Menu 메뉴 = Menu.of("짜장면", new BigDecimal("6000"), MenuGroup.from("메뉴그룹"), new ArrayList<MenuProduct>());
        
        Product 상품 = Product.of("짜장면", new BigDecimal("6000"));
        메뉴.addMenuProducts(Arrays.asList(MenuProduct.of(메뉴, 상품, 1L)));
        
        given(menuGroupService.findById(nullable(Long.class))).willReturn(메뉴.getMenuGroup());
        
        // when
        when(productRepository.findById(nullable(Long.class))).thenReturn(Optional.empty());
    
        // then
        assertThatThrownBy(() -> {
            menuService.create(MenuRequest.from(메뉴));
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("등록된 상품이 아닙니다");
    
    }
    
    @DisplayName("메뉴 등록시 가격은 포함된 상품들의 총 금액보다 클 수 없다- 예외처리")
    @Test
    void 메뉴_등록_금액_확인() {
        // given
        Menu 메뉴 = Menu.of("짜장면", new BigDecimal("10000"), MenuGroup.from("메뉴그룹"), new ArrayList<MenuProduct>());
        
        Product 상품 = Product.of("짜장면", new BigDecimal("6000"));
        
        메뉴.addMenuProducts(Arrays.asList(MenuProduct.of(메뉴, 상품, 1L)));
    
        given(menuGroupService.findById(nullable(Long.class))).willReturn(메뉴.getMenuGroup());
        given(productRepository.findById(nullable(Long.class))).willReturn(Optional.of(상품));
    
        // when, then
        assertThatThrownBy(() -> {
            menuService.create(MenuRequest.from(메뉴));
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("메뉴 가격이 상품 가격의 합보다 큽니다");
    }
    
    @DisplayName("메뉴 목록을 조회할 수 있다")
    @Test
    void 메뉴_목록_조회() {
        // given
        Menu 첫번째_메뉴 = Menu.of("짜장면", new BigDecimal("6000"), MenuGroup.from("짜장면_메뉴그룹"), new ArrayList<MenuProduct>());
        
        Menu 두번째_메뉴 = Menu.of("짬뽕", new BigDecimal("7000"), MenuGroup.from("짬뽕_메뉴그룹"), new ArrayList<MenuProduct>());
    
        given(menuRepository.findAll()).willReturn(Arrays.asList(첫번째_메뉴, 두번째_메뉴));
    
        // when
        List<MenuResponse> 메뉴_목록 = menuService.list();
    
        // then
        assertThat(메뉴_목록).containsExactly(MenuResponse.from(첫번째_메뉴), MenuResponse.from(두번째_메뉴));
    }
}
