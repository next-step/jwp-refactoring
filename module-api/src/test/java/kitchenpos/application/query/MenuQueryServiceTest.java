package kitchenpos.application.query;

import kitchenpos.application.command.MenuQueryService;
import kitchenpos.domain.menu.MenuProductRepository;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.dto.response.MenuViewResponse;
import kitchenpos.fixture.CleanUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static kitchenpos.fixture.MenuFixture.양념치킨_콜라_1000원_1개;
import static kitchenpos.fixture.MenuFixture.양념치킨_콜라_1000원_1개_MenuProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuQueryServiceTest {
    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuProductRepository menuProductRepository;

    private MenuQueryService menuQueryService;

    @BeforeEach
    void setUp() {
        CleanUp.cleanUp();

        menuQueryService = new MenuQueryService(menuRepository, menuProductRepository);
    }

    @Test
    @DisplayName("list - 정상적인 메뉴 전체 조회")
    void 정상적인_메뉴_전체_조회() {
        // when
        when(menuRepository.findAll()).thenReturn(Arrays.asList(양념치킨_콜라_1000원_1개));
        when(menuProductRepository.findAll()).thenReturn(양념치킨_콜라_1000원_1개_MenuProduct);

        MenuViewResponse resultMenu = menuQueryService.list().get(0);
        // then
        assertThat(resultMenu)
                .isEqualTo(MenuViewResponse.of(양념치킨_콜라_1000원_1개, 양념치킨_콜라_1000원_1개_MenuProduct));
    }

}