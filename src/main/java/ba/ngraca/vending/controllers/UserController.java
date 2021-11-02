package ba.ngraca.vending.controllers;

import ba.ngraca.vending.exceptions.NotFoundException;
import ba.ngraca.vending.payload.response.MessageResponse;
import ba.ngraca.vending.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable final Long id) {
        return ResponseEntity.ok(userRepository.findById(id).orElseThrow(() -> new NotFoundException("user", id)));
    }

    @PutMapping
    public ResponseEntity<?> updateUser() {
        // TODO update
        return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable final Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("User deleted successfully!"));
    }
}
