package com.textoit.raspberry.models;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="recommendations")
@NoArgsConstructor
@Getter
@Setter

public class Recommendation {

		@Id
	    @GeneratedValue(strategy = GenerationType.UUID)
	    private UUID id;
		@Column(name="ano")
	    private Long year;
	    private boolean winner;
	    @OneToOne
	    @JoinColumn(name = "movie_id")
	    private Movie movie;
}
