package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.ProductRepository;

@DataJpaTest
class MenuValidatorTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    private MenuValidator menuValidator;

    @BeforeEach
    void setUp() {
        menuValidator = new MenuValidator(menuGroupRepository, productRepository);
    }

    @Test
    void validateName() {
        // given
        final MenuRequest menuRequest = new MenuRequest(null, BigDecimal.ZERO, 0L, null);

        // when
        final Throwable throwable = catchThrowable(() -> menuValidator.validate(menuRequest));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void validatePrice() {
        // given
        final MenuRequest menuRequest = new MenuRequest("name", new BigDecimal(-1), 0L, null);

        // when
        final Throwable throwable = catchThrowable(() -> menuValidator.validate(menuRequest));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void validateMenuGroupId() {
        // given
        final MenuRequest menuRequest = new MenuRequest("name", BigDecimal.ZERO, 0L, null);

        // when
        final Throwable throwable = catchThrowable(() -> menuValidator.validate(menuRequest));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void validateMenuProduct() {
        // given
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("name"));
        final MenuRequest menuRequest = new MenuRequest("name", BigDecimal.ZERO, menuGroup.getId(),
            Collections.singletonList(new MenuProductRequest(99L, 1)));

        // when
        final Throwable throwable = catchThrowable(() -> menuValidator.validate(menuRequest));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void validateMenuPrice() {
        // given
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("name"));
        final MenuRequest menuRequest = new MenuRequest("name", new BigDecimal(999999L), menuGroup.getId(),
            Collections.singletonList(new MenuProductRequest(1L, 1)));

        // when
        final Throwable throwable = catchThrowable(() -> menuValidator.validate(menuRequest));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }
}
