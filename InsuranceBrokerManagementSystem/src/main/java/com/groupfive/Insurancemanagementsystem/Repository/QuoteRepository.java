package com.groupfive.Insurancemanagementsystem.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.groupfive.Insurancemanagementsystem.Model.Quote;
import com.groupfive.Insurancemanagementsystem.Util.DatabaseUtility;

public class QuoteRepository {
    public boolean saveQuote(Quote quote) {
        String sql = "INSERT INTO Quote (customerName, age, policyType, coverageAmount, termLength, premium) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = new DatabaseUtility().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
             
            statement.setString(1, quote.getCustomerName());
            statement.setInt(2, quote.getAge());
            statement.setString(3, quote.getPolicyType());
            statement.setDouble(4, quote.getCoverageAmount());
            statement.setInt(5, quote.getTermLength());
            statement.setDouble(6, quote.getPremium());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
