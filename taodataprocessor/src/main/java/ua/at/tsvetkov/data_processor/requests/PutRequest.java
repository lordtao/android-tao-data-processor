/*******************************************************************************
 * Copyright (c) 2014 Alexandr Tsvetkov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 * <p/>
 * Contributors:
 * Alexandr Tsvetkov - initial API and implementation
 * <p/>
 * Project:
 * TAO Data Processor
 * <p/>
 * License agreement:
 * <p/>
 * 1. This code is published AS IS. Author is not responsible for any damage that can be
 * caused by any application that uses this code.
 * 2. Author does not give a garantee, that this code is error free.
 * 3. This code can be used in NON-COMMERCIAL applications AS IS without any special
 * permission from author.
 * 4. This code can be modified without any special permission from author IF AND ONLY IF
 * this license agreement will remain unchanged.
 ******************************************************************************/
package ua.at.tsvetkov.data_processor.requests;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;

import ua.at.tsvetkov.data_processor.helpers.Scheme;

/**
 * Put Request builder.
 *
 * @author lordtao
 */
public class PutRequest extends WebRequest {

    /**
     * Return new instance of PutRequest.
     *
     * @return
     */
    public static PutRequest newInstance() {
        return new PutRequest();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (!isBuild()) {
            throw new IllegalArgumentException(REQUEST_IS_NOT_BUILDED);
        }
        startTime = System.currentTimeMillis();

        httpURLConnection = (HttpURLConnection) getURL().openConnection();
        httpURLConnection.setRequestMethod("PUT");
        httpURLConnection.setReadTimeout(configuration.getTimeout());
        httpURLConnection.setConnectTimeout(configuration.getTimeout());

        printToLogUrl();

        InputStream stream;
        try {
            stream  = httpURLConnection.getInputStream();
        } catch (Exception e) {
            stream = httpURLConnection.getErrorStream();
        }
        return new BufferedInputStream(stream);
    }

    // ********************************************************************************

    /**
     * Directly assign full URL string. All other URL methods will be ignored
     *
     * @param url
     */
    public PutRequest setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * Set encoding
     *
     * @param encoding
     */
    public PutRequest setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    /**
     * Sets the scheme "http://".
     *
     * @return
     */
    public PutRequest setSchemeHttp() {
        this.scheme = Scheme.HTTP.toString();
        return this;
    }

    /**
     * Sets the scheme "https://".
     *
     * @return
     */
    public PutRequest setSchemeHttps() {
        this.scheme = Scheme.HTTPS.toString();
        return this;
    }

    /**
     * Sets the scheme "file://".
     *
     * @return
     */
    public PutRequest setSchemeFile() {
        this.scheme = Scheme.FILE.toString();
        return this;
    }

    /**
     * Sets your scheme.
     *
     * @param scheme
     * @return
     */
    public PutRequest setScheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    /**
     * Set User Info.
     *
     * @param username
     * @param password
     * @return
     */
    public PutRequest setUserInfo(String username, String password) {
        this.username = username;
        this.password = password;
        return this;
    }

    /**
     * Set host.
     *
     * @param host
     * @return
     */
    public PutRequest setHost(String host) {
        this.host = host;
        return this;
    }

    /**
     * Set port.
     *
     * @param port
     * @return
     */
    public PutRequest setPort(String port) {
        this.port = port;
        return this;
    }

    /**
     * Set path
     *
     * @param path
     * @return
     */
    public PutRequest setPath(String path) {
        this.path = path;
        return this;
    }

    public PutRequest setLogTag(String tag) {
        this.tag = tag;
        return this;
    }

    /**
     * Add to query GET parameter.
     *
     * @param key
     * @param value
     * @return
     */
    public PutRequest addGetParam(String key, String value) {
        if (queries == null) {
            queries = new HashMap<String, String>();
        }
        queries.put(key, value);
        return this;
    }

    /**
     * Add to query GET parameter.
     *
     * @param key
     * @param value
     * @return
     */
    public PutRequest addGetParam(String key, int value) {
        if (queries == null) {
            queries = new HashMap<String, String>();
        }
        queries.put(key, String.valueOf(value));
        return this;
    }

    /**
     * Add to query GET parameter.
     *
     * @param key
     * @param value
     * @return
     */
    public PutRequest addGetParam(String key, long value) {
        if (queries == null) {
            queries = new HashMap<String, String>();
        }
        queries.put(key, String.valueOf(value));
        return this;
    }

    /**
     * Add to query GET parameter.
     *
     * @param key
     * @param value
     * @return
     */
    public PutRequest addGetParam(String key, float value) {
        if (queries == null) {
            queries = new HashMap<String, String>();
        }
        queries.put(key, String.valueOf(value));
        return this;
    }

    /**
     * Add to query GET parameter.
     *
     * @param key
     * @param value
     * @return
     */
    public PutRequest addGetParam(String key, double value) {
        if (queries == null) {
            queries = new HashMap<String, String>();
        }
        queries.put(key, String.valueOf(value));
        return this;
    }

    /**
     * Add fragment.
     *
     * @param fragment
     * @return
     */
    public PutRequest addFragment(String fragment) {
        this.fragment = fragment;
        return this;
    }

    /**
     * Save received data to cache file. Skip it if exist.
     *
     * @param cacheFileName
     */
    public PutRequest saveToCacheFile(String cacheFileName) {
        this.cacheFileName = cacheFileName;
        this.isRewriteFile = false;
        return this;
    }

    /**
     * Save received data to cashe file. Rewrite it if exist.
     *
     * @param cacheFileName
     */
    public PutRequest rewriteCashedFile(String cacheFileName) {
        this.cacheFileName = cacheFileName;
        this.isRewriteFile = true;
        return this;
    }

    @Override
    public PutRequest addProgressDialog(Context context, String title, String message) {
        setupProgress(context, title, message);
        return this;
    }

}
