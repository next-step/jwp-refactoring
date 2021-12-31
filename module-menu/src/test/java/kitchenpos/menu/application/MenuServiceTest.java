package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.application.MenuValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.dao.MenuRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupService menuGroupService;
    
    @Mock
    private MenuValidator menuValidator;

    @InjectMocks
    private MenuService menuService;

    @DisplayName("메뉴를 등록할 수 있다")
    @Test
    void 메뉴_등록() {
        // given
        Menu 메뉴 = Menu.of("짜장면", 6000L, MenuGroup.from("중식"));
        Product 상품 = Product.of("짜장면", 6000L);
        Long 상품_Id = 1L;
        MenuProduct 메뉴상품 = MenuProduct.of(상품_Id, 1L);
        메뉴.addMenuProducts(Arrays.asList(메뉴상품));
        
        MenuProductRequest 메뉴_상품_요청 = MenuProductRequest.of(1L, 1L);
        MenuRequest 메뉴_생성_요청 = MenuRequest.of("짜장면", 6000L, 1L, Arrays.asList(메뉴_상품_요청));
        
        given(menuGroupService.findById(anyLong())).willReturn(메뉴.getMenuGroup());
        given(menuRepository.save(any())).willReturn(메뉴);

        // when
        MenuResponse 저장된_메뉴 = menuService.create(메뉴_생성_요청);

        // then
        assertAll(
                () -> assertThat(저장된_메뉴.getName()).isEqualTo("짜장면"),
                () -> assertThat(저장된_메뉴.getPrice()).isEqualTo(6000)
        );

    }
    
    @DisplayName("메뉴 목록을 조회할 수 있다")
    @Test
    void 메뉴_목록_조회() {
        // given
        Menu 첫번째_메뉴 = Menu.of("짜장면", 6000L, MenuGroup.from("짜장면_메뉴그룹"));
        
        Menu 두번째_메뉴 = Menu.of("짬뽕", 7000L, MenuGroup.from("짬뽕_메뉴그룹"));
    
        given(menuRepository.findAll()).willReturn(Arrays.asList(첫번째_메뉴, 두번째_메뉴));
    
        // when
        List<MenuResponse> 메뉴_목록 = menuService.list();
    
        // then
        assertThat(메뉴_목록.size()).isEqualTo(2);
    }
}
