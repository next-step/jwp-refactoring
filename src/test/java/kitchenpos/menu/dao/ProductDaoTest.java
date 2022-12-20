package kitchenpos.menu.dao;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ProductDaoTest {

    @Autowired
    ProductDao productDao;

    @Test
    @DisplayName("productId in 검색 테스트")
    void findByIdIn(){
        // given
        Product savedProduct1 = productDao.save(new Product("상품1", new BigDecimal(1000)));
        Product savedProduct2 = productDao.save(new Product("상품2", new BigDecimal(1000)));

        // when
        List<Product> products = productDao.findProductByIdIn(Arrays.asList(
                savedProduct1.getId(), savedProduct2.getId()
        ));

        // then
        assertThat(products).hasSize(2);
        assertThat(products).containsExactly(
                savedProduct1, savedProduct2
        );
    }
}
