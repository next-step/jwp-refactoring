package kitchenpos.menu.application;

import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuProductRepository;
import kitchenpos.menu.MenuRepository;
import kitchenpos.menu.MenuTestFixture;
import kitchenpos.menu.dto.MenuResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    MenuRepository menuRepository;

    @Mock
    MenuProductRepository menuProductRepository;

    @Mock
    MenuValidator menuValidator;

    @InjectMocks
    private MenuService menuService;

    @DisplayName("0원 이상의 가격으로 메뉴를 등록한다")
    @Test
    void 메뉴_등록() {
        //Given
        when(menuRepository.save(any())).thenReturn(MenuTestFixture.맥모닝콤보);

        //When
        menuService.create(MenuTestFixture.맥모닝콤보_요청);

        //Then
        verify(menuRepository, times(1)).save(any());
        verify(menuProductRepository,times(1)).saveAll(any());
    }

    @DisplayName("메뉴 목록을 조회할 수 있다")
    @Test
    void 메뉴_목록_조회() {
        //Given
        List<Menu> 입력한_메뉴_목록 = new ArrayList<>(Arrays.asList(MenuTestFixture.맥모닝콤보));
        when(menuRepository.findAll()).thenReturn(입력한_메뉴_목록);
        when(menuProductRepository.findAllByMenuIdIn(any())).thenReturn(Arrays.asList(MenuTestFixture.아이스_아메리카노_한잔, MenuTestFixture.에그맥머핀_한개));

        //When
        List<MenuResponse> 조회된_메뉴_목록 = menuService.list();

        //Then
        verify(menuRepository, times(1)).findAll();
        Assertions.assertThat(조회된_메뉴_목록).hasSize(입력한_메뉴_목록.size());

    }
}
