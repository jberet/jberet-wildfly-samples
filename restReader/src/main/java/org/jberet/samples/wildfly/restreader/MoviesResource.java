/*
 * Copyright (c) 2016 Red Hat, Inc. and/or its affiliates.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.jberet.samples.wildfly.restreader;

import java.util.Collection;
import java.util.List;

import jakarta.servlet.ServletContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import org.jberet.samples.wildfly.common.Movie;

/**
 * REST resource class for {@link Movie}.
 */
@Path("/movies")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class MoviesResource {
    /**
     * An exception message shared by the REST resource and client in order
     * to verify that exception message is preserved in step exception.
     */
    public static final String EXCEPTION_MESSAGE = "Designed to throw exception for testing purpose.";

    /**
     * {@code javax.servlet.ServletContext} from which to retrieve all movie data.
     */
    @Context
    private ServletContext servletContext;

    /**
     * Gets movies matching {@code offset} and {@code limit} criteria.
     *
     * @param offset where to start reading
     * @param limit maximum number of records to read
     * @return movies as array
     *
     * @see #getMoviesList(int, int)
     * @see #getMoviesCollection(int, int)
     */
    @GET
    public Movie[] getMovies(final @QueryParam("offset") int offset,
                             final @QueryParam("limit") int limit) {
        final List<Movie> resultList = getMoviesList0(offset, limit);
        System.out.printf("Returning Movie[]: %s elements%n%s%n", resultList.size(), resultList);
        return resultList.toArray(new Movie[resultList.size()]);
    }

    /**
     * Gets movies matching {@code offset} and {@code limit} criteria.
     *
     * @param offset where to start reading
     * @param limit maximum number of records to read
     * @return movies as list
     *
     * @see #getMovies(int, int)
     * @see #getMoviesCollection(int, int)
     */
    @Path("list")
    @GET
    public List<Movie> getMoviesList(final @QueryParam("offset") int offset,
                                     final @QueryParam("limit") int limit) {
        final List<Movie> resultList = getMoviesList0(offset, limit);
        System.out.printf("Returning List<Movie>: %s elements%n%s%n", resultList.size(), resultList);
        return resultList;
    }

    /**
     * Gets movies matching {@code offset} and {@code limit} criteria.
     *
     * @param offset where to start reading
     * @param limit maximum number of records to read
     * @return movies as collection
     *
     * @see #getMovies(int, int)
     * @see #getMoviesList(int, int)
     */
    @Path("collection")
    @GET
    public Collection<Movie> getMoviesCollection(final @QueryParam("offset") int offset,
                                                 final @QueryParam("limit") int limit) {
        final List<Movie> resultList = getMoviesList0(offset, limit);
        System.out.printf("Returning Collection<Movie>: %s elements%n%s%n", resultList.size(), resultList);
        return resultList;
    }

    /**
     * Throws {@code RuntimeException} for testing purpsoe.
     *
     * @return always throws {@code RuntimeException}
     */
    @Path("error")
    @GET
    public Movie[] error() {
        throw new RuntimeException(EXCEPTION_MESSAGE);
    }

    @SuppressWarnings("unchecked")
    private List<Movie> getMoviesList0(final int offset, final int limit) {
        final List<Movie> allMovies = (List<Movie>) servletContext.getAttribute(ServletContextListener1.moviesKey);
        return allMovies.subList(offset, Math.min(offset + limit, allMovies.size()));
    }
}
