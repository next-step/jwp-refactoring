package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("메뉴 서비스")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuValidator menuValidator;
    @InjectMocks
    private MenuService menuService;

    private MenuProduct 매콤치킨구성;
    private MenuProduct 치즈볼구성;

    private Menu 매콤치킨단품;
    private Menu 매콤치즈볼세트;

    @BeforeEach
    void setUp() {
        매콤치킨구성 = new MenuProduct(1L, 1L);
        치즈볼구성 = new MenuProduct(1L, 2L);

        매콤치킨단품 = Menu.of("매콤치킨단품", BigDecimal.valueOf(13000), 1L, Collections.singletonList(매콤치킨구성));
        매콤치즈볼세트 = Menu.of("매콤치즈볼세트", BigDecimal.valueOf(15000), 1L, Arrays.asList(매콤치킨구성, 치즈볼구성));
    }

    @Test
    @DisplayName("메뉴를 등록한다.")
    void create() {
        MenuProductRequest 메뉴상품 = new MenuProductRequest(1L, 1L);
        MenuRequest 메뉴요청 = MenuRequest.of("매콤치킨단품", BigDecimal.valueOf(13000), 1L, Collections.singletonList(메뉴상품));

        when(menuRepository.save(any())).thenReturn(매콤치킨단품);
        MenuResponse response = menuService.create(메뉴요청);

        verify(menuRepository, times(1)).save(any(Menu.class));
        assertThat(response)
                .extracting("name", "price")
                .containsExactly(매콤치킨단품.getName(), 매콤치킨단품.getPrice());
    }

    @Test
    @DisplayName("메뉴 목록을 조회한다.")
    void list() {
        when(menuRepository.findAll()).thenReturn(Arrays.asList(매콤치킨단품, 매콤치즈볼세트));

        List<MenuResponse> responses = menuService.list();

        verify(menuRepository, times(1)).findAll();
        assertThat(responses).hasSize(2);
    }
}