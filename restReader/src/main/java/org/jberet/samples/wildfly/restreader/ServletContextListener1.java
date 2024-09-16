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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.jberet.samples.wildfly.common.Movie;

public class ServletContextListener1 implements ServletContextListener {
    static final String moviesKey = "movies";

    private static final String moviesUrl =
    "https://raw.githubusercontent.com/jberet/jberet-support/master/src/test/resources/movies-2012.json";

    // -------------------------------------------------------
    // ServletContextListener implementation
    // -------------------------------------------------------
    public void contextInitialized(ServletContextEvent sce) {
      /* This method is called when the servlet context is
         initialized(when the Web application is deployed). 
         You can initialize servlet context related data here.
      */
        final ServletContext servletContext = sce.getServletContext();
        JsonFactory jsonFactory = null;
        ObjectMapper mapper = null;
        InputStream inputStream = null;
        try {
            final URL url = new URL(moviesUrl);
            inputStream = url.openStream();
            jsonFactory = new JsonFactory();
            mapper = new ObjectMapper(jsonFactory);
            final Movie[] movies = mapper.readValue(inputStream, Movie[].class);
            servletContext.setAttribute(moviesKey, Collections.unmodifiableList(Arrays.asList(movies)));
        } catch (final MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (final IOException ioe) {
                    //ignore
                }
            }
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
      /* This method is invoked when the Servlet Context 
         (the Web application) is undeployed or 
         Application Server shuts down.
      */
        final ServletContext servletContext = sce.getServletContext();
        servletContext.removeAttribute(moviesKey);
    }
}
