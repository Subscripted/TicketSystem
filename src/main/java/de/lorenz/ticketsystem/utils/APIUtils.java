package de.lorenz.ticketsystem.utils;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class APIUtils {

    public static String shortenName(String fullName) {
        if (fullName == null || fullName.isBlank()) return "";

        String[] piece = fullName.trim().split("\\s+");

        if (piece.length == 1) {
            return piece[0].substring(0, Math.min(2, piece[0].length())).toUpperCase();
        }

        char firstnameLetter = piece[0].charAt(0);
        char lastnameLetter = piece[1].charAt(0);

        return ("" + firstnameLetter + lastnameLetter).toUpperCase();
    }

    public static String cleanString(String param) {
        int maxLength = 1_000_000;
        if (param == null || param.isBlank()) return "";
        param = param.length() > maxLength ? param.substring(0, maxLength) : param;
        param = param.replaceAll("\\d+", "");
        return param;
    }


    public static Integer cleanInteger(Integer param) {


        return param;
    }

    public boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+\\.[A-Za-z]{2,}$");
        return pattern.matcher(email).matches();
    }
}

