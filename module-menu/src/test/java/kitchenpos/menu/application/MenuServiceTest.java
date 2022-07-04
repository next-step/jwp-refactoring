package kitchenpos.menu.application;

import static kitchenpos.menu.fixture.MenuFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.domain.MenuValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuValidator menuValidator;
    @InjectMocks
    private MenuService menuService;

    private MenuGroup 파스타메뉴;
    private Menu 봉골레파스타세트;

    @BeforeEach
    void setUp() {
        파스타메뉴 = 메뉴그룹_생성(1L, "파스타메뉴");
        봉골레파스타세트 = 메뉴_생성("봉골레파스타세트", new Price(15000), 1L, 파스타메뉴_상품_생성());
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void create() {
        //given
        given(menuRepository.save(any())).willReturn(봉골레파스타세트);
        MenuRequest 봉골레파스타세트_요청 = 메뉴요청_생성("봉골레파스타세트", BigDecimal.valueOf(15000), 파스타메뉴.getId(), 파스타메뉴_상품_생성());

        //when
        MenuResponse createdMenu = menuService.create(봉골레파스타세트_요청);

        //then
        assertThat(createdMenu).isNotNull();
        assertThat(createdMenu.getId()).isEqualTo(봉골레파스타세트.getId());
    }

    @DisplayName("메뉴 가격이 0보다 작은 경우, 메뉴 등록에 실패한다.")
    @Test
    void create_invalidPrice() {
        //given
        MenuRequest 봉골레파스타세트_요청 = 메뉴요청_생성("봉골레파스타세트", BigDecimal.valueOf(-1), 파스타메뉴.getId(), 파스타메뉴_상품_생성());

        //when & then
        assertThatThrownBy(() -> menuService.create(봉골레파스타세트_요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 메뉴 상품이 포함되면, 메뉴 등록에 실패한다.")
    @Test
    void create_invalidNotExistsMenuProduct() {
        //given
        MenuRequest 봉골레파스타세트_요청 = 메뉴요청_생성("봉골레파스타세트", BigDecimal.valueOf(20000), 파스타메뉴.getId(), 파스타메뉴_상품_생성());
        willThrow(new IllegalArgumentException("존재하지 않는 메뉴그룹입니다."))
                .given(menuValidator)
                .validate(봉골레파스타세트_요청);

        //when & then
        assertThatThrownBy(() -> menuService.create(봉골레파스타세트_요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 내 제품가격의 합보다 메뉴 가격이 크면, 메뉴 등록에 실패한다.")
    @Test
    void create_invalidPriceSum() {
        //given
        MenuRequest 봉골레파스타세트_요청 = 메뉴요청_생성("봉골레파스타세트", BigDecimal.valueOf(20000), 파스타메뉴.getId(), 파스타메뉴_상품_생성());
        willThrow(new IllegalArgumentException("메뉴 내 제품가격의 합보다 메뉴가격이 클 수 없습니다."))
                .given(menuValidator)
                .validate(봉골레파스타세트_요청);

        //when & then
        assertThatThrownBy(() -> menuService.create(봉골레파스타세트_요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        //given
        given(menuRepository.findAll()).willReturn(Arrays.asList(봉골레파스타세트));

        //when
        List<MenuResponse> menuResponses = menuService.list();

        //then
        assertThat(menuResponses).hasSize(1);
    }

}
