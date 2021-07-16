package kitchenpos.menu.application;

import kitchenpos.common.Price;
import kitchenpos.common.Quantity;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    MenuRepository menuRepository;

    @Mock
    MenuValidator menuValidator;

    @InjectMocks
    private MenuService menuService;

    private final Long 메뉴_ID = 1L;
    private final Long 메뉴그룹_ID = 1L;
    private Long 후라이드_상품_ID = 1L;
    private Long 콜라_상품_ID = 2L;

    private final MenuGroup 인기메뉴 = new MenuGroup(메뉴그룹_ID, "인기메뉴");
    private final MenuProduct 후라이드_한마리 = new MenuProduct(후라이드_상품_ID, Quantity.of(1L));
    private final MenuProduct 콜라_한개 = new MenuProduct(콜라_상품_ID, Quantity.of(1L));
    private final List<MenuProduct> 메뉴상품_목록 = Arrays.asList(후라이드_한마리, 콜라_한개);
    private Menu 후라이드세트 = new Menu("후라이드세트", Price.valueOf(10000), 인기메뉴.getId(), 메뉴상품_목록);

    @DisplayName("0원 이상의 가격으로 메뉴를 등록한다")
    @Test
    void 메뉴_등록() {
        //Given
        MenuProductRequest 후라이드_한마리_요청 = new MenuProductRequest(메뉴_ID, 후라이드_상품_ID, 1L);
        MenuProductRequest 콜라_한개_요청 = new MenuProductRequest(메뉴_ID, 콜라_상품_ID, 1L);
        MenuRequest 후라이드세트_요청 = new MenuRequest(후라이드세트.getName(), 후라이드세트.getPrice().value(),
                메뉴그룹_ID, Arrays.asList(후라이드_한마리_요청, 콜라_한개_요청));
        when(menuRepository.save(any())).thenReturn(후라이드세트);

        //When
        menuService.create(후라이드세트_요청);

        //Then
        verify(menuRepository, times(1)).save(any());
    }

    @DisplayName("메뉴 목록을 조회할 수 있다")
    @Test
    void 메뉴_목록_조회() {
        //Given
        List<Menu> 입력한_메뉴_목록 = new ArrayList<>(Arrays.asList(후라이드세트));
        when(menuRepository.findAll()).thenReturn(입력한_메뉴_목록);

        //When
        List<MenuResponse> 조회된_메뉴_목록 = menuService.list();

        //Then
        verify(menuRepository, times(1)).findAll();
        assertThat(조회된_메뉴_목록).hasSize(입력한_메뉴_목록.size());

    }
}
