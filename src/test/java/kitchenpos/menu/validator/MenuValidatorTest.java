package kitchenpos.menu.validator;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductTestFixture;
import kitchenpos.menu.domain.MenuTestFixture;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupTestFixture;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MenuValidatorTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuValidator menuValidator;

    private Product 하와이안피자;
    private MenuProduct 하와이안피자상품;
    private Menu 하와이안피자세트;
    private MenuGroup 피자;
    private List<MenuProductRequest> 상품요청 = new ArrayList<>();

    @BeforeEach
    void setUp() {
        하와이안피자 = ProductTestFixture.create(1L, "하와이안피자", BigDecimal.valueOf(15_000));
        피자 = MenuGroupTestFixture.create(1L, "피자");
        하와이안피자상품 = MenuProductTestFixture.create(1L, null, 하와이안피자, 1L);
        하와이안피자세트 = MenuTestFixture.create(1L, "하와이안피자세트", BigDecimal.valueOf(15_000L), 피자, Arrays.asList(하와이안피자상품));
        상품요청.add(MenuProductRequest.of(하와이안피자상품.getSeq(), 1L));
    }

    @DisplayName("존재하는 메뉴 그룹에 속하지 않는 메뉴는 생성할 수 없다.")
    @Test
    void validateMenuRequestThrowErrorWhenMenuGroupIsNotExists() {
        MenuRequest menuRequest = MenuRequest.of(하와이안피자세트.getName().value(), BigDecimal.valueOf(15_000), 10L, 상품요청);
        when((menuGroupRepository.findById(10L))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> menuValidator.validate(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
