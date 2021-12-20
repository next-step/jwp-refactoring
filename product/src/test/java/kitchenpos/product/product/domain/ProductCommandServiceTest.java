package kitchenpos.product.product.domain;

import static kitchenpos.product.product.sample.ProductSample.십원치킨;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("상품 커맨드 서비스")
@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
class ProductCommandServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductCommandService commandService;

    @Test
    @DisplayName("상품 저장")
    void save() {
        //given
        Product 십원치킨 = 십원치킨();

        //when
        commandService.save(십원치킨);

        //then
        verify(productRepository, only()).save(십원치킨);
    }
}
