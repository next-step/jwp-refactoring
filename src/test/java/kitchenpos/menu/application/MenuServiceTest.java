package kitchenpos.menu.application;

import static kitchenpos.fixture.MenuFixture.메뉴_생성;
import static kitchenpos.fixture.MenuFixture.메뉴_생성_요청;
import static kitchenpos.fixture.MenuFixture.메뉴그룹_생성;
import static kitchenpos.fixture.MenuFixture.메뉴상품_생성;
import static kitchenpos.fixture.ProductFixture.상품_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.repository.MenuProductRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.repository.ProductRepository;
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
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private MenuProductRepository menuProductRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private MenuService menuService;

    private MenuGroup 파스타메뉴;
    private Product 봉골레파스타;
    private Product 감자튀김;
    private Menu 봉골레파스타세트;

    @BeforeEach
    void setUp() {
        파스타메뉴 = 메뉴그룹_생성(1L, "파스타메뉴");
        봉골레파스타 = 상품_생성(1L, "봉골레파스타", 13000);
        감자튀김 = 상품_생성(2L, "감자튀김", 5000);
        봉골레파스타세트 = 메뉴_생성("봉골레파스타세트", new Price(15000), 파스타메뉴, 파스타메뉴_상품_생성());
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void create() {
        //given
        given(menuGroupRepository.findById(any())).willReturn(Optional.of(파스타메뉴));
        given(productRepository.findById(봉골레파스타.getId())).willReturn(Optional.of(봉골레파스타));
        given(productRepository.findById(감자튀김.getId())).willReturn(Optional.of(감자튀김));
        given(menuRepository.save(any())).willReturn(봉골레파스타세트);
        MenuRequest 봉골레파스타세트_요청 = 메뉴_생성_요청("봉골레파스타세트", BigDecimal.valueOf(15000), 파스타메뉴, 파스타메뉴_상품_생성());

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
        MenuRequest 봉골레파스타세트_요청 = 메뉴_생성_요청("봉골레파스타세트", BigDecimal.valueOf(-1), 파스타메뉴, 파스타메뉴_상품_생성());

        //when & then
        assertThatThrownBy(() -> menuService.create(봉골레파스타세트_요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 메뉴 상품이 포함되면, 메뉴 등록에 실패한다.")
    @Test
    void create_invalidNotExistsMenuProduct() {
        //given
        given(menuGroupRepository.findById(any())).willReturn(Optional.of(파스타메뉴));
        given(productRepository.findById(봉골레파스타.getId())).willReturn(Optional.empty());
        MenuRequest 봉골레파스타세트_요청 = 메뉴_생성_요청("봉골레파스타세트", BigDecimal.valueOf(20000), 파스타메뉴, 파스타메뉴_상품_생성());

        //when & then
        assertThatThrownBy(() -> menuService.create(봉골레파스타세트_요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 내 제품가격의 합보다 메뉴 가격이 크면, 메뉴 등록에 실패한다.")
    @Test
    void create_invalidPriceSum() {
        //given
        given(menuGroupRepository.findById(any())).willReturn(Optional.of(파스타메뉴));
        MenuRequest 봉골레파스타세트_요청 = 메뉴_생성_요청("봉골레파스타세트", BigDecimal.valueOf(20000), 파스타메뉴, 파스타메뉴_상품_생성());

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

    private List<MenuProduct> 파스타메뉴_상품_생성() {
        return Arrays.asList(
                메뉴상품_생성(1L, 봉골레파스타, 1L),
                메뉴상품_생성(2L, 감자튀김, 1L)
        );
    }
}
