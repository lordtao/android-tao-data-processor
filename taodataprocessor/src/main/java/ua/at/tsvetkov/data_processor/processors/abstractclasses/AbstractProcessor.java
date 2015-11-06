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
package ua.at.tsvetkov.data_processor.processors.abstractclasses;

import java.io.InputStream;

public abstract class AbstractProcessor<T> {

    private String statusMessage = "";

    /**
     * Parse the input stream with implemented parser.
     *
     * @param inputStream
     * @throws Exception
     */
    public abstract void parse(InputStream inputStream) throws Exception;

    /**
     * Returns the result of processing query.
     *
     * @return resulting object
     */
    public abstract T getResult();

    /**
     * Set the message about processing query.
     */
    public void setMessage(String message) {
        statusMessage = message;
    }

    /**
     * Returns the message about processing query.
     *
     * @return message
     */
    public String getMessage() {
        return statusMessage;
    }

}
