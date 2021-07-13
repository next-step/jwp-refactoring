package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequestModel;
import kitchenpos.product.dto.ProductResponseModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponseModel create(final ProductRequestModel productRequestModel) {
        Product product = new Product(productRequestModel);
        return ProductResponseModel.of(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public List<ProductResponseModel> list() {
        return ProductResponseModel.ofList(productRepository.findAll());
    }
}
