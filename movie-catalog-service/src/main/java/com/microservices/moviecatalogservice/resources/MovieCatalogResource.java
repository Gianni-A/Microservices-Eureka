package com.microservices.moviecatalogservice.resources;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.microservices.moviecatalogservice.models.CatalogItem;
import com.microservices.moviecatalogservice.models.Movie;
import com.microservices.moviecatalogservice.models.Rating;
import com.microservices.moviecatalogservice.models.UserRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {
	
	//It was created by WebClient.Builder
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {
		
		//It was created because we didn't have a bean function created
		//RestTemplate restTemplate = new RestTemplate(); Deprecated (1)
		
		//Deprecate (2), using the follow code
		/*List<Rating> ratings = Arrays.asList(
				new Rating("1234", 10),
				new Rating("5678", 8)
			);*/
		
		//Substitution of the code deprecated (2)
		//UserRating ratings = restTemplate.getForObject("http://localhost:8083/ratingsdata/users/" + userId, UserRating.class);
		UserRating ratings = restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/" + userId, UserRating.class);
		
		return ratings.getUserRating().stream().map(rating -> {
			//For each movie ID, call info movie service and get details
			
			//Deprecated (1)
			//This one is deprecated for using beans, by using WebClient.Builder
			//Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);
			Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
			//Put them all together
			return new CatalogItem(movie.getName(), "Desc", rating.getRating());
		})
		.collect(Collectors.toList());	
		
		/*return Collections.singletonList(
					new CatalogItem("Transformers", "Test", 4)
				);*/
	}
	
}

/*Movie movie = webClientBuilder.build()
.get() // the method of the request
.uri("http://localhost:8082/movies/" + rating.getMovieId()) //Where do you need to request
.retrieve() //Go do the fetch
.bodyToMono(Movie.class) //Whatever you get, convert the data into the Movie.class
.block();*/


/*RestTemplate VS WebClient.Builder*/
/*
 * Both are for the same purpose but RestTemplate will be deprecated so it is recommendable to use WebClient.Builder*/
// RestTemplate - Deprecated in the future
// WebClient.Builder - Substitution of RestTemplate
