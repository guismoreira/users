package orion.users.util;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class ValidateEmail {

    public static boolean validateMail(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }


}