import org.mindrot.jbcrypt.BCrypt;
public class GenHash {
    public static void main(String[] args) {
        String password = "password123";
        String hash = BCrypt.hashpw(password, BCrypt.gensalt(10));
        System.out.println("Generated hash for 'password123':");
        System.out.println(hash);
        
        // Verify it works
        boolean verify = BCrypt.checkpw(password, hash);
        System.out.println("Verification test: " + (verify ? "PASSED" : "FAILED"));
    }
}
