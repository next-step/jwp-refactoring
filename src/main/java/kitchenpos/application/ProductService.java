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
        return ProductMapper.toProductResponse(productDao.save(Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .build()));
    }

    @Transactional(readOnly = true)
    public List<Product> findProducts(List<Long> id) {
        return productDao.findByIdIn(id);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return ProductMapper.toProductResponses(productDao.findAll());
    }
}
