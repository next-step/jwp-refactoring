package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductCreateRequest;
import kitchenpos.dto.ProductResponse;
import kitchenpos.mapper.ProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    public ProductResponse create(final ProductCreateRequest request) {
        final Product savedProduct = productDao.save(Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .build());

        return ProductMapper.toProductResponse(savedProduct);
    }

    @Transactional(readOnly = true)
    public List<Product> findProducts(List<Long> id) {
        return productDao.findByIdIn(id);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        final List<Product> products = productDao.findAll();

        return ProductMapper.toProductResponses(products);
    }
}
