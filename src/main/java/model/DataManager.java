package model;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import java.math.BigDecimal;

public class DataManager {
    private static DataManager instance;
    private final Map<YearMonth, BudgetManager> monthlyBudgets;

    private DataManager() {
        monthlyBudgets = new HashMap<>();
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public BudgetManager getBudgetForMonth(YearMonth month) {
        if (!monthlyBudgets.containsKey(month)) {
            // Default budget of 0 when creating a new month
            monthlyBudgets.put(month, new BudgetManager(BigDecimal.ZERO));
        }
        return monthlyBudgets.get(month);
    }
}
