package com.textoit.raspberry.repositories;

import com.textoit.raspberry.models.Award;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AwardRepository {

    private final JdbcTemplate jt;
    @Autowired
    public AwardRepository(JdbcTemplate jdbcTemplate) {
        this.jt = jdbcTemplate;
    }

    public List<Award> findMax() {
        String sql = "SELECT producers as producer, MAX(intervalo) AS intervalo, ano as previousWin, prox_ano as followingWin " +
                "FROM ( " +
                "    SELECT producers, ano, " +
                "        LEAD(ano) OVER (PARTITION BY producers ORDER BY ano) AS prox_ano, " +
                "        LEAD(ano) OVER (PARTITION BY producers ORDER BY ano) - ano AS intervalo, " +
                "        COUNT(*) OVER (PARTITION BY producers) AS premios " +
                "    FROM movielist WHERE WINNER = TRUE " +
                ") subquery " +
                "WHERE premios >= 2 AND prox_ano IS NOT NULL " +
                "GROUP BY producers, ano, prox_ano";

        return jt.query(sql, (rs, rowNum) -> {
            Award aw = new Award();
            aw.setProducer( rs.getString("producer"));
            aw.setInterval(rs.getLong("intervalo"));
            aw.setPreviousWin(rs.getLong("previousWin"));
            aw.setFollowingWin(rs.getLong("followingWin"));
            return aw;
        });
    }
}
