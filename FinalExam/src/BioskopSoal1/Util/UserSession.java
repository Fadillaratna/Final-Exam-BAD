package BioskopSoal1.Util;

import java.util.Date;

public class UserSession {

    private static int id;
    private static String name;
    private static String email;
    private static String role;
    private static Date date_of_birth;

    public static void setUser(String name, int id, String email, String role, Date date_of_birth) {
        UserSession.name = name;
        UserSession.id = id;
        UserSession.email = email;
        UserSession.role = role;
        UserSession.date_of_birth = date_of_birth;
    }

    public static int getId() {
        return id;
    }

    public static String getName() {
        return name;
    }

    public static String getEmail() {
        return email;
    }

    public static String getRole() {
        return role;
    }

    public static Date getDateOfBirth() {
        return date_of_birth;
    }
    
    public static boolean isSessionValid() {
        return name != null && id != -1;
    }

    public static void clearSession() {
        name = null;
        id = -1;
        email = null;
        role = null;
        date_of_birth = null;
    }
}
