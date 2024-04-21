package com.textoit.raspberry.services;

import com.textoit.raspberry.models.Award;
import com.textoit.raspberry.repositories.AwardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class AwardService {

    @Autowired
    AwardRepository ar;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AwardService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Award> findMax() {
        List<Award> listAward = new ArrayList<>();
        String sql = "SELECT producers, " +
                "       MAX(intervalo) AS \"interval\", " +
                "       ano AS \"previousWin\", " +
                "       prox_ano AS \"followingWin\" " +
                "FROM ( " +
                "    SELECT producers, " +
                "           ano, " +
                "           LEAD(ano) OVER (PARTITION BY producers ORDER BY ano) AS prox_ano, " +
                "           LEAD(ano) OVER (PARTITION BY producers ORDER BY ano) - ano AS intervalo, " +
                "           COUNT(*) OVER (PARTITION BY producers) AS premios " +
                "    FROM movielist " +
                "    WHERE winner = TRUE " +
                ") subquery " +
                "WHERE premios >= 2 AND prox_ano IS NOT NULL " +
                "GROUP BY producers, ano, prox_ano " +
                "HAVING MAX(intervalo) = ( " +
                "    SELECT MAX(intervalo) " +
                "    FROM ( " +
                "        SELECT LEAD(ano) OVER (PARTITION BY producers ORDER BY ano) - ano AS intervalo " +
                "        FROM movielist " +
                "        WHERE winner = TRUE " +
                "        GROUP BY producers, ano " +
                "    ) subquery " +
                ")";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Award award = new Award();
            award.setProducer(rs.getString("producers"));
            award.setInterval(rs.getLong("interval"));
            award.setPreviousWin(rs.getLong("previousWin"));
            award.setFollowingWin(rs.getLong("followingWin"));
            return award;
        });


    }

    public List<Award> findMin() {
        List<Award> listAward = new ArrayList<>();
        String sql = "SELECT producers, " +
                "       MIN(intervalo) AS \"interval\", " +
                "       ano AS \"previousWin\", " +
                "       prox_ano AS \"followingWin\" " +
                "FROM ( " +
                "    SELECT producers, " +
                "           ano, " +
                "           LEAD(ano) OVER (PARTITION BY producers ORDER BY ano) AS prox_ano, " +
                "           LEAD(ano) OVER (PARTITION BY producers ORDER BY ano) - ano AS intervalo, " +
                "           COUNT(*) OVER (PARTITION BY producers) AS premios " +
                "    FROM movielist " +
                "    WHERE winner = TRUE " +
                ") subquery " +
                "WHERE premios >= 2 AND prox_ano IS NOT NULL " +
                "GROUP BY producers, ano, prox_ano " +
                "HAVING MIN(intervalo) = ( " +
                "    SELECT MIN(intervalo) " +
                "    FROM ( " +
                "        SELECT LEAD(ano) OVER (PARTITION BY producers ORDER BY ano) - ano AS intervalo " +
                "        FROM movielist " +
                "        WHERE winner = TRUE " +
                "        GROUP BY producers, ano " +
                "    ) subquery " +
                ")";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Award award = new Award();
            award.setProducer(rs.getString("producers"));
            award.setInterval(rs.getLong("interval"));
            award.setPreviousWin(rs.getLong("previousWin"));
            award.setFollowingWin(rs.getLong("followingWin"));
            return award;
        });


    }
}
