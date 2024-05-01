package com.textoit.raspberry.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="movies")
@NoArgsConstructor
@Getter
@Setter


public class Movie {

	 	@Id
		@GeneratedValue(strategy = GenerationType.UUID)
	    private UUID id;

	    private String title;

		private String studio;

	    @ManyToMany
	    @JoinTable(
	        name = "movie_producer",
	        joinColumns = @JoinColumn(name = "movie_id"),
	        inverseJoinColumns = @JoinColumn(name = "producer_id")
	    )
	    private List<Producer> producers = new ArrayList<>();

		@OneToOne(mappedBy = "movie")
	    private Recommendation recommendation;

}
