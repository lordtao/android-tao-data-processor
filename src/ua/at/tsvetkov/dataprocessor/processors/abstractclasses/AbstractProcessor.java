/*******************************************************************************
 * Copyright (c) 2014 Alexandr Tsvetkov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors:
 *     Alexandr Tsvetkov - initial API and implementation
 *
 * Project:
 *     TAO Data Processor
 *
 * License agreement:
 *
 * 1. This code is published AS IS. Author is not responsible for any damage that can be
 *    caused by any application that uses this code.
 * 2. Author does not give a garantee, that this code is error free.
 * 3. This code can be used in NON-COMMERCIAL applications AS IS without any special
 *    permission from author.
 * 4. This code can be modified without any special permission from author IF AND ONLY IF
 *    this license agreement will remain unchanged.
 ******************************************************************************/
package ua.at.tsvetkov.dataprocessor.processors.abstractclasses;

import java.io.InputStream;

public abstract class AbstractProcessor {

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
	public abstract Object getResult();

}
