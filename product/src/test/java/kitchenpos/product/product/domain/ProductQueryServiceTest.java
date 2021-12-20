package kitchenpos.product.product.domain;

import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@DisplayName("상품 쿼리 서비스")
@ExtendWith(MockitoExtension.class)
class ProductQueryServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductQueryService queryService;

    @Test
    @DisplayName("상품들 데이터 가져오기")
    void findAll() {
        //when
        queryService.findAll();

        //then
        verify(productRepository, only()).findAll();
    }
}
