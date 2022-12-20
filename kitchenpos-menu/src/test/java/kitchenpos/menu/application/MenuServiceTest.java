package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuCreateValidator;
import kitchenpos.menu.domain.MenuName;
import kitchenpos.menu.domain.MenuProductBag;
import kitchenpos.menu.domain.MenuRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static kitchenpos.menu.domain.MenuGroupTestFixture.메뉴_그룹_추천_메뉴;
import static kitchenpos.menu.domain.MenuProductTestFixture.메뉴_상품;
import static kitchenpos.menu.domain.MenuTest.메뉴;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.lenient;
import static kitchenpos.product.domain.ProductTestFixture.상품_콜라;
import static kitchenpos.product.domain.ProductTestFixture.상품_통다리;

@DisplayName("메뉴 테스트 ")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    private static final Product 통다리 = 상품_통다리(1L);
    private static final Product 콜라 = 상품_콜라(2L);

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuCreateValidator menuCreateValidator;

    @InjectMocks
    private MenuService menuService;

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        //given:
        final Menu 메뉴 = 메뉴(MenuName.from("자메이카 통다리 1인 세트"),
                Price.from(BigDecimal.ONE),
                메뉴_그룹_추천_메뉴(1L).getId(),
                MenuProductBag.from(Arrays.asList(
                        메뉴_상품(통다리.getId(), 5),
                        메뉴_상품(콜라.getId(), 1))));
        //when:
        lenient().when(menuRepository.save(메뉴)).thenReturn(메뉴);
        lenient().doNothing().when(menuCreateValidator).validate(메뉴);
        final Menu 저장된_메뉴 = menuService.create(메뉴);
        //then:
        assertThat(저장된_메뉴).isEqualTo(메뉴);
    }

    @DisplayName("생성 예외 - 메뉴의 가격이 0보다 작은 경우")
    @Test
    void 생성_예외_메뉴의_가격이_0보다_작은_경우() {
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(메뉴(
                MenuName.from("자메이카 통다리 1인 세트"),
                Price.from(BigDecimal.valueOf(-1)),
                메뉴_그룹_추천_메뉴(1L).getId(),
                MenuProductBag.from(Arrays.asList(
                        메뉴_상품(통다리.getId(), 5),
                        메뉴_상품(콜라.getId(), 1))))));
    }

    @DisplayName("목록 조회 성공")
    @Test
    void 목록_조회_성공() {
        //given:
        final Menu 메뉴 = 메뉴(MenuName.from("자메이카 통다리 1인 세트"),
                Price.from(BigDecimal.ONE),
                메뉴_그룹_추천_메뉴(1L).getId(),
                MenuProductBag.from(Arrays.asList(
                        메뉴_상품(통다리.getId(), 5),
                        메뉴_상품(콜라.getId(), 1))));
        //when:
        lenient().when(menuRepository.findAll()).thenReturn(Collections.singletonList(메뉴));
        //then:
        assertThat(menuService.list()).contains(메뉴);
    }
}
