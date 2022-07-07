package kitchenpos.menu.appliaction;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.validator.MenuValidator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.menu.fixture.MenuFixture.기본_메뉴;
import static kitchenpos.menu.fixture.MenuFixture.추천_기본_메뉴;
import static kitchenpos.menugroup.fixture.MenuGroupFixture.추천_메뉴;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuValidator menuValidator;

    @Test
    @DisplayName("메뉴의 정상적인 데이터를 입력한다면 정상 상품 등록이 된다")
    void create() {
        MenuProductRequest 양념_치킨_요청값 = MenuProductRequest.of(1L, 1);

        MenuRequest 추천_기본_메뉴_요청값 = MenuRequest.of("추천 기본 메뉴", BigDecimal.valueOf(17_000), 추천_메뉴.getId(), Arrays.asList(양념_치킨_요청값));
        when(menuRepository.save(any())).thenReturn(추천_기본_메뉴);

        MenuResponse 메뉴_등록_결과 = menuService.create(추천_기본_메뉴_요청값);

        Assertions.assertThat(메뉴_등록_결과).isEqualTo(MenuResponse.of(추천_기본_메뉴));
    }

    @Test
    @DisplayName("메뉴의 가격을 0원보다 미만으로 등록한다면 에러가 발생된다")
    void create2() {
        MenuProductRequest 양념_치킨_요청값 = MenuProductRequest.of(1L, 1);

        MenuRequest 잘못된_금액_요청값 = MenuRequest.of(
                "추천 기본 메뉴",
                BigDecimal.valueOf(-1000),
                추천_메뉴.getId(),
                Arrays.asList(양념_치킨_요청값)
        );

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> menuService.create(잘못된_금액_요청값));
    }

    @Test
    @DisplayName("메뉴 조회 테스트")
    void list() {
        when(menuRepository.findAll()).thenReturn(Arrays.asList(기본_메뉴, 추천_기본_메뉴));

        List<MenuResponse> 메뉴_조회_테스트_결과 = menuService.list();

        assertAll(
                () -> Assertions.assertThat(메뉴_조회_테스트_결과).hasSize(2),
                () -> Assertions.assertThat(메뉴_조회_테스트_결과).containsExactly(
                        MenuResponse.of(기본_메뉴),
                        MenuResponse.of(추천_기본_메뉴)
                )
        );
    }
}
