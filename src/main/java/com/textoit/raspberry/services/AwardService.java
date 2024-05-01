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
        String sql = "SELECT producer_name, " +
                "       MAX(max_interval) AS max_interval, " +
                "       MIN(previousWin) AS previousWin, " +
                "       MAX(followingWin) AS followingWin " +
                "FROM ( " +
                "    SELECT producer_name, " +
                "           max_interval, " +
                "           previousWin, " +
                "           followingWin " +
                "    FROM ( " +
                "        SELECT producer_name, " +
                "               MAX(next_year - ano) AS max_interval, " +
                "               MIN(ano) AS previousWin, " +
                "               MAX(ano) AS followingWin " +
                "        FROM ( " +
                "            SELECT p.name AS producer_name, " +
                "                   r.ano, " +
                "                   LEAD(r.ano) OVER (PARTITION BY mp.producer_id ORDER BY r.ano) AS next_year " +
                "            FROM producers p " +
                "            JOIN movie_producer mp ON p.id = mp.producer_id " +
                "            JOIN recommendations r ON mp.movie_id = r.movie_id AND r.winner = TRUE " +
                "        ) AS subquery " +
                "        GROUP BY producer_name " +
                "        ORDER BY max_interval DESC " +
                "    ) AS max_interval_subquery " +
                "    WHERE max_interval = ( " +
                "        SELECT MAX(max_interval) " +
                "        FROM ( " +
                "            SELECT MAX(next_year - ano) AS max_interval " +
                "            FROM ( " +
                "                SELECT p.name AS producer_name, " +
                "                       r.ano, " +
                "                       LEAD(r.ano) OVER (PARTITION BY mp.producer_id ORDER BY r.ano) AS next_year " +
                "                FROM producers p " +
                "                JOIN movie_producer mp ON p.id = mp.producer_id " +
                "                JOIN recommendations r ON mp.movie_id = r.movie_id AND r.winner = TRUE " +
                "            ) AS subquery " +
                "            GROUP BY producer_name " +
                "        ) AS max_interval_subquery " +
                "    ) " +
                ") AS final_results " +
                "GROUP BY producer_name;";


        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Award award = new Award();
            award.setProducer(rs.getString("producer_name"));
            award.setInterval(rs.getLong("max_interval"));
            award.setPreviousWin(rs.getLong("previousWin"));
            award.setFollowingWin(rs.getLong("followingWin"));
            return award;
        });


    }

    public List<Award> findMin() {
        List<Award> listAward = new ArrayList<>();
        String sql = "SELECT producer_name, " +
                "       MIN(min_interval) AS min_interval, " +
                "       MIN(previousWin) AS previousWin, " +
                "       MAX(followingWin) AS followingWin " +
                "FROM ( " +
                "    SELECT producer_name, " +
                "           min_interval, " +
                "           previousWin, " +
                "           followingWin " +
                "    FROM ( " +
                "        SELECT producer_name, " +
                "               MIN(next_year - ano) AS min_interval, " +
                "               MIN(ano) AS previousWin, " +
                "               MAX(ano) AS followingWin " +
                "        FROM ( " +
                "            SELECT p.name AS producer_name, " +
                "                   r.ano, " +
                "                   LEAD(r.ano) OVER (PARTITION BY mp.producer_id ORDER BY r.ano) AS next_year " +
                "            FROM producers p " +
                "            JOIN movie_producer mp ON p.id = mp.producer_id " +
                "            JOIN recommendations r ON mp.movie_id = r.movie_id AND r.winner = TRUE " +
                "        ) AS subquery " +
                "        GROUP BY producer_name " +
                "        ORDER BY min_interval ASC " +
                "    ) AS min_interval_subquery " +
                "    WHERE min_interval = ( " +
                "        SELECT MIN(MIN_interval) " +
                "        FROM ( " +
                "            SELECT MIN(next_year - ano) AS min_interval " +
                "            FROM ( " +
                "                SELECT p.name AS producer_name, " +
                "                       r.ano, " +
                "                       LEAD(r.ano) OVER (PARTITION BY mp.producer_id ORDER BY r.ano) AS next_year " +
                "                FROM producers p " +
                "                JOIN movie_producer mp ON p.id = mp.producer_id " +
                "                JOIN recommendations r ON mp.movie_id = r.movie_id AND r.winner = TRUE " +
                "            ) AS subquery " +
                "            GROUP BY producer_name " +
                "        ) AS min_interval_subquery " +
                "    ) " +
                ") AS final_results " +
                "GROUP BY producer_name;";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Award award = new Award();
            award.setProducer(rs.getString("producer_name"));
            award.setInterval(rs.getLong("min_interval"));
            award.setPreviousWin(rs.getLong("previousWin"));
            award.setFollowingWin(rs.getLong("followingWin"));
            return award;
        });


    }
}
