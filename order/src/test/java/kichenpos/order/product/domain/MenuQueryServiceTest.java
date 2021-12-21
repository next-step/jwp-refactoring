package kichenpos.order.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kichenpos.common.domain.Name;
import kichenpos.common.domain.Price;
import kichenpos.order.product.infrastructure.MenuClient;
import kichenpos.order.product.infrastructure.dto.MenuDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 쿼리 서비스")
@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuQueryServiceTest {

    @Mock
    private MenuClient menuClient;

    @InjectMocks
    private MenuQueryService menuQueryService;

    @Test
    @DisplayName("id 로 메뉴 목록 가져오기")
    void findAllById() {
        //given
        long menuId = 1L;
        BigDecimal 십원 = BigDecimal.TEN;
        String 치킨세트 = "치킨세트";
        when(menuClient.list(anyList()))
            .thenReturn(Collections.singletonList(new MenuDto(menuId, 십원, 치킨세트)));

        //when
        List<Menu> menus = menuQueryService.findAllById(Collections.singletonList(1L));

        //then
        assertThat(menus).first()
            .extracting(Menu::id, Menu::price, Menu::name)
            .containsExactly(menuId, Price.from(십원), Name.from(치킨세트));
    }
}
