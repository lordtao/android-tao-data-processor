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
package ua.at.tsvetkov.data_processor;

import android.os.Looper;
import android.support.v4.util.LruCache;

import java.util.List;

import ua.at.tsvetkov.data_processor.processors.Processor;
import ua.at.tsvetkov.data_processor.processors.Processor.Callback;
import ua.at.tsvetkov.data_processor.requests.Request;
import ua.at.tsvetkov.data_processor.threads.DataProcessorThreadPool;
import ua.at.tsvetkov.util.Log;

/**
 * @author lordtao
 */
public class DataProcessor {

   private static final String ERROR_NOT_INIT              = "DataProcessor must be init with configuration before using.";
   private static final String ERROR_INIT_CONFIG_WITH_NULL = "DataProcessor configuration can not be initialized with null.";
   private static final String LOG_INIT_CONFIG             = "Initialize DataProcessor with configuration.";
   private static final String WARNING_RE_INIT_CONFIG      = "Try to initialize DataProcessor which had already been initialized before.";
   private static final String AR_R                        = "\u2192";

   private static DataProcessor instance;

   private DataProcessorConfiguration configuration;
   private DataProcessorThreadPool    threadPool;
   private ProcessorsCache            processors;

   public static DataProcessor getInstance() {
      if (instance == null) {
         synchronized (DataProcessor.class) {
            if (instance == null) {
               instance = new DataProcessor();
            }
         }
      }
      return instance;
   }

   /**
    * Return current data processor configuration
    *
    * @return configuration
    */
   public DataProcessorConfiguration getConfiguration() {
      return configuration;
   }

   /**
    * Build new data processor with given configuration
    *
    * @param configuration new configuration
    */
   public synchronized void init(DataProcessorConfiguration configuration) {
      if (configuration == null) {
         throw new IllegalArgumentException(ERROR_INIT_CONFIG_WITH_NULL);
      }
      if (this.configuration == null) {
         if (configuration.isLogEnabled) {
            Log.d(LOG_INIT_CONFIG);
         }
         this.configuration = configuration;
      } else {
         Log.w(WARNING_RE_INIT_CONFIG);
      }
      if (configuration.isThreadPoolEnabled) {

         threadPool = new DataProcessorThreadPool();
      }
      if (configuration.isCacheEnabled()) {
         processors = new ProcessorsCache(configuration.getCacheSize());
      }
   }

   private synchronized void checkConfiguration() {
      if (configuration == null || configuration.httpUserAgent == null) {
         throw new IllegalStateException(ERROR_NOT_INIT);
      }
   }

   /**
    * Return data processor thread pool
    *
    * @return thread pool
    */
   public DataProcessorThreadPool getThreadPool() {
      return threadPool;
   }

   /**
    * Initiates an orderly shutdown in which previously submitted tasks are executed, but no new tasks will be accepted. Invocation has no
    * additional effect if already shut down.
    */
   public void shutdown() {
      threadPool.shutdown();
      Log.v("Thread pool will shutdown.");
   }

   /**
    * Attempts to stop all actively executing tasks, halts the processing of waiting tasks, and returns a list of the tasks that were
    * awaiting execution. These tasks are drained (removed) from the task queue upon return from this method.
    *
    * @return list of tasks that never commenced execution
    */
   public List<Runnable> shutdownNow() {
      Log.v("Thread pool will shutdown now.");
      return threadPool.shutdownNow();
   }

   // ******************************** Execution methods ********************************

   /**
    * Execute the request, process the results in instance of <b>clazz</b> and return result object
    *
    * @param <T>     class implements one of the abstract interfaces
    *                {@link ua.at.tsvetkov.data_processor.interfaces.InputStreamDataInterface InputStreamDataInterface} or
    *                {@link ua.at.tsvetkov.data_processor.interfaces.StringDataInterface StringDataInterface}
    * @param request prepared request
    * @param clazz   class to generate
    * @return generated object
    */
   public synchronized <T> T execute(Request request, Class<T> clazz) {
      checkConfiguration();
      return new Processor<T>(this, request, clazz).execute();
   }

   /**
    * Execute async request, process the results in instance of <b>clazz</b> and return created clazz in callback
    *
    * @param <T>     class implements one of the abstract interfaces
    *                {@link ua.at.tsvetkov.data_processor.interfaces.InputStreamDataInterface InputStreamDataInterface} or
    *                {@link ua.at.tsvetkov.data_processor.interfaces.StringDataInterface StringDataInterface}
    * @param request prepared request
    * @param clazz   class to generate
    */
   public synchronized <T> void executeAsync(Request request, Class<T> clazz) {
      checkConfiguration();
      new Processor<T>(this, request, clazz, null).executeAsync();
   }

   /**
    * Execute async request, process the results in instance of <b>clazz</b> and return created clazz in callback
    *
    * @param <T>      class implements one of the abstract interfaces
    *                 {@link ua.at.tsvetkov.data_processor.interfaces.InputStreamDataInterface InputStreamDataInterface} or
    *                 {@link ua.at.tsvetkov.data_processor.interfaces.StringDataInterface StringDataInterface}
    * @param request  prepared request
    * @param clazz    class to generate
    * @param callback return result of request and the generated object (if success) in this callback
    */
   public synchronized <T> void executeAsync(Request request, Class<T> clazz, Callback<T> callback) {
      checkConfiguration();
      new Processor<T>(this, request, clazz, callback).executeAsync();
   }

   /**
    * Execute async request, process the results in instance of <b>clazz</b> and return created clazz in callback. Use LruCache for store
    * result. If the result was loaded earlier then returns it.
    *
    * @param key      for identification of the Request for saving in LruCache
    * @param <T>      class implements one of the abstract interfaces
    *                 {@link ua.at.tsvetkov.data_processor.interfaces.InputStreamDataInterface InputStreamDataInterface} or
    *                 {@link ua.at.tsvetkov.data_processor.interfaces.StringDataInterface StringDataInterface}
    * @param request  prepared request
    * @param clazz    class to generate
    * @param callback return result of request and the generated object (if success) in this callback
    */
   public synchronized <T> void executeCachedAsync(int key, Request request, Class<T> clazz, Callback<T> callback) {
      executeCachedAsync(key, request, clazz, callback, false);
   }

   /**
    * Execute async request, process the results in instance of <b>clazz</b> and return created clazz in callback. Use LruCache for store
    * result. Ignore the previous result if it was loaded. Forcing the download.
    *
    * @param key      for identification of the Request for saving in LruCache
    * @param <T>      class implements one of the abstract interfaces
    *                 {@link ua.at.tsvetkov.data_processor.interfaces.InputStreamDataInterface InputStreamDataInterface} or
    *                 {@link ua.at.tsvetkov.data_processor.interfaces.StringDataInterface StringDataInterface}
    * @param request  prepared request
    * @param clazz    class to generate
    * @param callback return result of request and the generated object (if success) in this callback
    */
   public synchronized <T> void executeCachedAsyncForce(int key, Request request, Class<T> clazz, Callback<T> callback) {
      executeCachedAsync(key, request, clazz, callback, true);
   }

   /**
    * Execute async request, process the results in instance of <b>clazz</b> and return created clazz in callback. Use LruCache for store
    * result. <code>isForce</code> specifies whether to force the download.
    *
    * @param key      for identification of the Request for saving in LruCache
    * @param <T>      class implements one of the abstract interfaces
    *                 {@link ua.at.tsvetkov.data_processor.interfaces.InputStreamDataInterface InputStreamDataInterface} or
    *                 {@link ua.at.tsvetkov.data_processor.interfaces.StringDataInterface StringDataInterface}
    * @param request  prepared request
    * @param clazz    class to generate
    * @param callback return result of request and the generated object (if success) in this callback
    * @param isForce  is need to force reload data
    */
   public synchronized <T> void executeCachedAsync(int key, Request request, Class<T> clazz, Callback<T> callback, boolean isForce) {
      if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
         throw new IllegalStateException("Must be executed from UI thread.");
      }
      checkConfiguration();
      if (!configuration.isCacheEnabled()) {
         throw new IllegalArgumentException("Wrong cache settings: cache is not enabled");
      }
      if (!configuration.isCacheEnabled() || configuration.getCacheSize() <= 0) {
         throw new IllegalArgumentException("Wrong cache settings: cacheSize = " + configuration.getCacheSize());
      }
      Processor<?> pr = processors.get(key);
      if (pr != null) {
         @SuppressWarnings("unchecked")
         Processor<T> processor = (Processor<T>) pr;
         if (isForce) {
            processor.setCallback(null);
            processor = new Processor<T>(this, request, clazz, callback);
            processors.put(key, processor);
            processor.executeAsync();
            Log.v(AR_R + " Forced execute: " + request);
         } else if (processor.isFinished()) {
            processor.setCallback(callback);
            processor.redelivery();
            Log.v(AR_R + " Redelivery data: " + request);
         } else {
            Log.v(AR_R + " Still running: " + request);
         }
      } else {
         Processor<T> processor = new Processor<T>(this, request, clazz, callback);
         processors.put(key, processor);
         processor.executeAsync();
      }
   }

   private class ProcessorsCache extends LruCache<Integer, Processor<?>> {

      /**
       * @param maxSize max cache size
       */
      public ProcessorsCache(int maxSize) {
         super(maxSize);
      }

   }


}
