package ba.ngraca.vending.models;

import ba.ngraca.vending.payload.request.ProductRequest;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(0)
    private int amountAvailable;

    @NotNull
    @Min(0)
    private int price;

    @NotBlank
    private String productName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User seller;

    public Product() {
    }

    public Product(int amountAvailable, int price, String productName, User seller) {
        this.amountAvailable = amountAvailable;
        this.price = price;
        this.productName = productName;
        this.seller = seller;
    }

    public Product(ProductRequest productRequest) {
        this.amountAvailable = productRequest.getAmountAvailable();
        this.price = productRequest.getPrice();
        this.productName = productRequest.getProductName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAmountAvailable() {
        return amountAvailable;
    }

    public void setAmountAvailable(int amountAvailable) {
        this.amountAvailable = amountAvailable;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }
}
