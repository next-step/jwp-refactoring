package kitchenpos.menu.validator;

import kitchenpos.menu.dto.CreateMenuRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class MenuValidatorTest {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    MenuValidator menuValidator;

    private Product 데리버거;
    private Product 새우버거;

    @BeforeEach
    void setUp() {
        데리버거 = productRepository.save(Product.of("데리버거", BigDecimal.valueOf(1_000)));
        새우버거 = productRepository.save(Product.of("새우버거", BigDecimal.valueOf(1_000)));

    }

    @Test
    void validate() {
        // given
        final CreateMenuRequest 햄버거메뉴 = new CreateMenuRequest("햄버거메뉴", BigDecimal.valueOf(1_500), 1L,
                Arrays.asList(new MenuProductRequest(데리버거.getId(), 1), (new MenuProductRequest(새우버거.getId(), 1))));
        // then
        menuValidator.validate(햄버거메뉴);

    }
}