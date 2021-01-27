package kitchenpos.product;

import kitchenpos.application.ProductService;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductDao productDao;

    @DisplayName("상품을 생성한다.")
    @Test
    public void createProduct() {
        Product expected = new Product("케이크", new BigDecimal(1000));
        Product actual = productDao.save(expected);
        assertThat(expected.getName()).isEqualTo(actual.getName());
        assertThat(expected.getPrice()).isEqualTo(actual.getPrice());
        assertThat(actual.getId()).isNotNull();
    }


}
