package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("애플리케이션 테스트 보호 - 상품 서비스")
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품 등록")
    @Test
    void create() {
        Product request = new Product();
        request.setName("강정치킨");
        request.setPrice(BigDecimal.valueOf(17000));
        Product expected = new Product();
        expected.setId(1L);
        expected.setName(request.getName());
        expected.setPrice(request.getPrice());

        when(productDao.save(request)).thenReturn(expected);

        Product savedProduct = productService.create(request);

        assertThat(savedProduct).isEqualTo(expected);
    }

}
