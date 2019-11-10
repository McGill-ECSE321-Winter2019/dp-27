package ca.mcgill.cooperator.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServiceUtils {

    /**
     * Converts an Iterable to a List
     *
     * @param <T>
     * @param iterable
     * @return Iterable in List format
     */
    public static <T> List<T> toList(Iterable<T> iterable) {
        List<T> resultList = new ArrayList<T>();
        for (T t : iterable) {
            resultList.add(t);
        }
        return resultList;
    }

    /**
     * Checks if an email is valid
     *
     * @param email
     * @return true if email is valid
     */
    public static boolean isValidEmail(String email) {
        String regex =
                "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
    
    /**
     * Checks if an phone number is valid (just if all characters are numbers)
     *
     * @param email
     * @return true if email is valid
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^[0-9]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);

        return matcher.matches();
    }
}
