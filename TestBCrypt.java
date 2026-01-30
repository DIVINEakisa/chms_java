import org.mindrot.jbcrypt.BCrypt;
public class TestBCrypt {
    public static void main(String[] args) {
        String password = "password123";
        String hash = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";
        
        System.out.println("Testing BCrypt...");
        System.out.println("Password: " + password);
        System.out.println("Hash: " + hash);
        
        boolean result = BCrypt.checkpw(password, hash);
        System.out.println("Result: " + result);
        
        if (result) {
            System.out.println(" Password verification PASSED");
        } else {
            System.out.println(" Password verification FAILED");
        }
    }
}
