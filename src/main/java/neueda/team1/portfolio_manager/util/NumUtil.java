package neueda.team1.portfolio_manager.util;

public class NumUtil {
    public static int randomChangeInt(int num, double percentage) {
        if (num < 100) {
            return 100;
        }
        Double random = (Math.random() * 2 - 1) * (num * percentage);
        return num + random.intValue();
    }

    public static int randomInt(int low, int high) {
        Double result = low + Math.random() * (high - low);
        return result.intValue();
    }
}
