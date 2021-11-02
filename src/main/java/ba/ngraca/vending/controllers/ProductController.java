package ba.ngraca.vending.controllers;

import ba.ngraca.vending.exceptions.NotFoundException;
import ba.ngraca.vending.models.Product;
import ba.ngraca.vending.payload.request.ProductRequest;
import ba.ngraca.vending.payload.response.MessageResponse;
import ba.ngraca.vending.repository.ProductRepository;
import ba.ngraca.vending.repository.UserRepository;
import ba.ngraca.vending.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {

        return ResponseEntity.ok(productRepository.findById(id));
    }

    @PreAuthorize("hasRole('ROLE_SELLER')")
    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductRequest productRequest) {

        final Product product = new Product(productRequest);

        final UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        final Long id = userDetails.getId();

        product.setSeller(userRepository.findById(id).orElseThrow(() -> new NotFoundException("user", id)));

        productRepository.save(product);

        return ResponseEntity.ok(new MessageResponse("Product saved successfully!"));
    }

    @PreAuthorize("hasRole('ROLE_SELLER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest productRequest) {

        final Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("product", id));

        final UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        final Long userId = userDetails.getId();

        if (!product.getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("Product of another seller!"));
        }

        product.setProductName(productRequest.getProductName());
        product.setAmountAvailable(productRequest.getAmountAvailable());
        product.setPrice(productRequest.getPrice());

        productRepository.save(product);

        return ResponseEntity.ok(new MessageResponse("Product updated successfully!"));
    }

    @PreAuthorize("hasRole('ROLE_SELLER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {

        final Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("product", id));

        final UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        final Long userId = userDetails.getId();

        if (!product.getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("Product of another seller!"));
        }

        productRepository.deleteById(id);

        return ResponseEntity.ok(new MessageResponse("Product deleted successfully!"));
    }
}
