/*
 * Copyright (c) 2016 Red Hat, Inc. and/or its affiliates.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.jberet.samples.wildfly.restwriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import jakarta.servlet.ServletContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import org.jberet.samples.wildfly.common.Movie;

/**
 * REST resource class for {@link Movie}. Operations including getting all movies,
 * adding movies to specific group identified by {@code testName}, and removing
 * movies.
 */
@Path("/movies")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class MoviesResource {
    private static final String moviesKey = "movies-";

    /**
     * {@code javax.servlet.ServletContext} from which to retrieve all movie data.
     */
    @Context
    private ServletContext servletContext;

    /**
     * Returns all movies in group identified by {@code testName}.
     *
     * @param testName movie group name
     * @return all movies for {@code testName}
     */
    @SuppressWarnings("unchecked")
    @GET
    public List<Movie> getMovies(final @QueryParam("testName") String testName) {
        return (List<Movie>) servletContext.getAttribute(getMoviesKey(testName));
    }

    /**
     * Remove all movies in group identified by {@code testName}.
     *
     * @param testName movie group name
     */

    @DELETE
    public void removeMovies(final @QueryParam("testName") String testName) {
        servletContext.removeAttribute(getMoviesKey(testName));
    }

    /**
     * Adds an array of movies to the group identified by {@code testName}.
     *
     * @param testName movie group name
     * @param movies array of movies to add
     *
     * @see #addMoviesList(String, List)
     * @see #addMoviesCollection(String, Collection)
     */
    @POST
    public void addMovies(final @QueryParam("testName") String testName,
                          final Movie[] movies) {
        addMovie0(testName, Arrays.asList(movies));
        System.out.printf("Adding Movie[] for testName %s: %s elements%n%s%n",
                testName, movies.length, Arrays.toString(movies));
    }

    /**
     * Adds a list of movies to the group identified by {@code testName}.
     *
     * @param testName movie group name
     * @param movies list of movies to add
     *
     * @see #addMovies(String, Movie[])
     * @see #addMoviesCollection(String, Collection)
     */
    @Path("list")
    @POST
    public void addMoviesList(final @QueryParam("testName") String testName,
                              final List<Movie> movies) {
        addMovie0(testName, movies);
        System.out.printf("Adding List<Movie> for testName %s: %s elements%n%s%n",
                testName, movies.size(), movies);
    }

    /**
     * Adds a collection of movies to the group identified by {@code testName}.
     *
     * @param testName movie group name
     * @param movies collection of movies to add
     *
     * @see #addMovies(String, Movie[])
     * @see #addMoviesList(String, List)
     */
    @Path("collection")
    @POST
    public void addMoviesCollection(final @QueryParam("testName") String testName,
                                    final Collection<Movie> movies) {
        addMovie0(testName, movies);
        System.out.printf("Adding Collection<Movie> for testName %s: %s elements%n%s%n",
                testName, movies.size(), movies);
    }

    /**
     * Adds movies, using {@code ServletContext} attribute as a storage for testing purpose.
     *
     * @param testName the current test name, used as part of the key for {@code ServletContext} attribute
     * @param movies a collection of {@code Movie}
     */
    @SuppressWarnings("unchecked")
    private void addMovie0(final String testName, final Collection<Movie> movies) {
        final String key = getMoviesKey(testName);
        List<Movie> existingMovies = (List<Movie>) servletContext.getAttribute(key);
        if (existingMovies == null) {
            existingMovies = new ArrayList<Movie>();
            servletContext.setAttribute(key, existingMovies);
        }
        existingMovies.addAll(movies);
    }

    private static String getMoviesKey(final String testName) {
        return moviesKey + testName;
    }
}
