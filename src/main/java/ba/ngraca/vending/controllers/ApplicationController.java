package ba.ngraca.vending.controllers;

import ba.ngraca.vending.dtos.ProductDTO;
import ba.ngraca.vending.enums.CoinEnum;
import ba.ngraca.vending.exceptions.DepositException;
import ba.ngraca.vending.exceptions.NotFoundException;
import ba.ngraca.vending.models.Product;
import ba.ngraca.vending.models.User;
import ba.ngraca.vending.payload.request.BuyRequest;
import ba.ngraca.vending.payload.request.DepositRequest;
import ba.ngraca.vending.payload.response.BuyResponse;
import ba.ngraca.vending.payload.response.MessageResponse;
import ba.ngraca.vending.repository.ProductRepository;
import ba.ngraca.vending.repository.UserRepository;
import ba.ngraca.vending.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.EnumMap;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class ApplicationController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @PreAuthorize("hasRole('ROLE_BUYER')")
    @PutMapping("/deposit/{id}")
    public ResponseEntity<?> deposit(@PathVariable Long id, @Valid @RequestBody DepositRequest depositRequest) {

        final User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("user", id));

        final int depositRequestTotal = depositRequest.getTotal();

        if (depositRequestTotal < 0) {
            throw new DepositException("Deposit value invalid. Negative number.");
        }

        if (depositRequestTotal == 0) {
            throw new DepositException("Deposit value invalid. No deposit.");
        }

        user.setDeposit(user.getDeposit() + depositRequestTotal);

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Successfully deposited " + depositRequestTotal + " cents"));
    }

    @PreAuthorize("hasRole('ROLE_BUYER')")
    @PatchMapping("/reset/{id}")
    public ResponseEntity<?> reset(@PathVariable final Long id) {

        final User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("user", id));

        user.setDeposit(0);

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Deposit reset successful!"));
    }

    @PreAuthorize("hasRole('ROLE_BUYER')")
    @PostMapping("/buy")
    public ResponseEntity<?> buy(@Valid @RequestBody final BuyRequest buyRequest) {

        final Long productId = buyRequest.getProductId();

        final Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("product", productId));

        final Integer amount = buyRequest.getAmount();
        if (product.getAmountAvailable() < amount) {
            return ResponseEntity.badRequest().body(new MessageResponse("Not enough products available!"));
        }

        final UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        final Long userId = userDetails.getId();
        final User buyer = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user", userId));

        final int totalPrice = product.getPrice() * buyRequest.getAmount();
        final int deposit = buyer.getDeposit();
        if (totalPrice > deposit) {
            return ResponseEntity.badRequest().body(new MessageResponse("Not enough deposit!"));
        }

        final int totalChange = deposit - totalPrice;
        final EnumMap<CoinEnum, Integer> change = calculateChangeV2(totalChange);
        buyer.setDeposit(0);
        userRepository.save(buyer);

        final User seller = userRepository.findById(product.getSeller().getId()).orElseThrow(() -> new NotFoundException("user", userId));

        seller.setDeposit(seller.getDeposit() + totalPrice);
        userRepository.save(seller);

        final ProductDTO productDTO = new ProductDTO();
        productDTO.setProductName(product.getProductName());
        productDTO.setSeller(seller.getUsername());
        productDTO.setBoughtAmount(buyRequest.getAmount());

        final BuyResponse buyResponse = new BuyResponse();
        buyResponse.setBoughtProducts(productDTO);
        buyResponse.setChange(change);
        buyResponse.setSpent(totalPrice);

        return ResponseEntity.ok(buyResponse);
    }

    private EnumMap<CoinEnum, Integer> calculateChangeV2(final int totalChange) {
        final EnumMap<CoinEnum, Integer> change = new EnumMap<>(CoinEnum.class);

        int rest = totalChange;

        rest = calculateChange(rest, change, CoinEnum.HUNDRED_CENTS);
        rest = calculateChange(rest, change, CoinEnum.FIFTY_CENTS);
        rest = calculateChange(rest, change, CoinEnum.TWENTY_CENTS);
        rest = calculateChange(rest, change, CoinEnum.TEN_CENTS);
        rest = calculateChange(rest, change, CoinEnum.FIVE_CENTS);

        return change;
    }

    private int calculateChange(final int totalChange, final EnumMap<CoinEnum, Integer> change, CoinEnum coinEnum) {
        final int wholes = totalChange - (totalChange % coinEnum.getValue());
        change.put(coinEnum, wholes / coinEnum.getValue());

        return totalChange - wholes;
    }
}
