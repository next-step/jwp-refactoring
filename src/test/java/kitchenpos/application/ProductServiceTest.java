package kitchenpos.application;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품 생성 성공")
    @Test
    void createProductSuccess() {

        // given
        Product entity = new Product();
        entity.setName("entity");
        entity.setPrice(new BigDecimal(1000));

        Product expect = new Product();
        expect.setId(1L);
        expect.setName("entity");
        expect.setPrice(new BigDecimal(1000));

        given(productRepository.save(entity)).willReturn(expect);

        // when
        productService.create(entity);

        // then
        verify(productRepository).save(entity);
    }

    @DisplayName("상품 생성 실패 - 상품 가격이 음수")
    @Test
    void createProductFail01() {

        // given
        Product entity = new Product();
        entity.setName("entity");
        entity.setPrice(new BigDecimal(-1000));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(entity));

        // then
        verify(productRepository, never()).save(any());
    }

    @DisplayName("상품 생성 실패 - 상품 가격 설정 안됨")
    @Test
    void createProductFail02() {

        // given
        Product entity = new Product();
        entity.setName("entity");

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(entity));

        // then
        verify(productRepository, never()).save(any());
    }
}
