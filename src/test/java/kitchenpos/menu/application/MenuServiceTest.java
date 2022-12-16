package kitchenpos.menu.application;

import static java.util.Collections.singletonList;
import static kitchenpos.menu.domain.MenuGroupTestFixture.면류;
import static kitchenpos.menu.domain.MenuProductTestFixture.*;
import static kitchenpos.menu.domain.MenuTestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.exception.InvalidParameterException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 비즈니스 로직 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuValidator menuValidator;
    @Mock
    private MenuRepository menuRepository;
    @InjectMocks
    private MenuService menuService;

    @Test
    @DisplayName("가격이 없는 메뉴를 등록한다.")
    void createMenuByPriceIsNull() {
        // given
        MenuRequest 짜장1 = menuRequest("짜장1", null, 1L, singletonList(짜장면_1그릇_요청));

        // when & then
        assertThatThrownBy(() -> menuService.create(짜장1))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("가격은 필수입니다.");
    }

    @Test
    @DisplayName("가격이 0원 이하인 메뉴를 등록한다.")
    void createMenuByPriceLessThanZero() {
        // given
        MenuRequest 짜장1 = menuRequest("짜장1", BigDecimal.valueOf(-2_000L), 1L, singletonList(짜장면_1그릇_요청));

        // when & then
        assertThatThrownBy(() -> menuService.create(짜장1))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("가격은 0원 이상이어야 합니다.");
    }
    @Test
    @DisplayName("메뉴를 등록한다.")
    void createMenu() {
        // given
        Menu 짜장1_짬뽕2 = menu(1L, "짜장1_짬뽕2", BigDecimal.valueOf(23_000L),
                면류.id(), Arrays.asList(짜장면_1그릇, 짬뽕_2그릇));
        MenuRequest 짜장1_짬뽕2_요청 = menuRequest("짜장1_짬뽕2_요청", BigDecimal.valueOf(23_000L),
                면류.id(), Arrays.asList(짜장면_1그릇_요청, 짬뽕_2그릇_요청));
        given(menuRepository.save(any())).willReturn(짜장1_짬뽕2);

        // when
        MenuResponse actual = menuService.create(짜장1_짬뽕2_요청);

        // then
        assertAll(
                () -> assertThat(actual.getName()).isEqualTo("짜장1_짬뽕2"),
                () -> assertThat(actual.getPrice()).isEqualTo(BigDecimal.valueOf(23000))
        );
    }

    @Test
    @DisplayName("메뉴 목록을 조회하면 메뉴 목록이 반환된다.")
    void findMenus() {
        // given
        Menu 짜장1 = menu(1L, "짜장1", BigDecimal.valueOf(7000), 면류.id(), singletonList(짜장면_1그릇));
        Menu 짬뽕2 = menu(2L, "짬뽕2", BigDecimal.valueOf(16000), 면류.id(), singletonList(짬뽕_2그릇));
        List<Menu> menus = Arrays.asList(짜장1, 짬뽕2);
        given(menuRepository.findAll()).willReturn(menus);

        // when
        List<MenuResponse> actual = menuService.list();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual.stream()
                        .map(MenuResponse::getName)
                        .collect(Collectors.toList())).containsExactly("짜장1", "짬뽕2")
        );
    }
}
