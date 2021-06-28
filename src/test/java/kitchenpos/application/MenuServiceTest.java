package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    private MenuService menuService;

    @BeforeEach
    void setUp() {
        this.menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
    }

    @Test
    @DisplayName("create - 메뉴의 가격이 비어 있거나, 0원보다 적을경우 IllegalArgumentException 이 발생한다.")
    void 메뉴의_가격이_비어_있거나_0원보다_적을경우_IllegalArgumentException이_발생한다() {
        // given
        Menu menu = new Menu(null, null, BigDecimal.valueOf(-1), null, null);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @Test
    @DisplayName("create - 메뉴의 메뉴 그룹이 DB에 없을경우 IllegalArgumetException 이 발생한다.")
    void 메뉴의_메뉴_그룹이_DB에_없을경우_IllegalArgumentException이_발생한다() {
        // given
        Long menuGroupId = 1L;
        Menu menu = new Menu(null, null, BigDecimal.valueOf(0), menuGroupId, null);

        given(menuGroupDao.existsById(menuGroupId))
                .willReturn(false);

        // when & then

        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
        verify(menuGroupDao, VerificationModeFactory.only())
                .existsById(menuGroupId);
    }

    @Test
    @DisplayName("create - 메뉴 상품이 DB에 있는지 확인하고, 없으면 IllegalArgumentException 이 발생한다.")
    void 메뉴_상품이_DB에_있는지_확인하고_없으면_IllegalArgumentException이_발생한다() {
    }

    @Test
    @DisplayName("create - 메뉴 상품의 금액 합계가 메뉴 가격보다 크면 IllegalArgumentException 이 발생한다.")
    void 메뉴_상품의_금액_합계가_메뉴_가격보다_크면_IllegalArgumentException이_발생한다() {
    }

    @Test
    @DisplayName("create - 정상정인 메뉴 등록")
    void 정상적인_메뉴_등록() {
    }

    @Test
    @DisplayName("list - 정상적인 메뉴 전체 조회")
    void 정상적인_메뉴_전체_조회() {
    }

}