/*
 * Copyright (C) 2016 SmartCodeUnited http://www.smartcodeunited.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.smartcodeunited.lib.bluetooth.commands;

public class CommandProtocol {

	/**
	 * <p>
	 * Bluetooth scan timeout
	 * </p>
	 */
	public static  int SCAN_TIMEOUT = 10*1000;
	/**
	 * <p>
	 * Minimum length。
	 * </p>
	 * 
	 * @since 1.0.0
	 */
	public static final int COMMAND_LENGTH_MINIMUM = 7;


	/**
	 * <p>
	 * Command type: verification, inquery, control, feedback.
	 * </p>
	 * 
	 * @since 1.0.0
	 */
	public static class Type
	{
		/**
		 * <p>
		 * verification
		 * </p>
		 * 
		 * @since 1.0.0
		 */
		public static final int VERIFICATION = 0x01;

		/**
		 * <p>
		 * inquery
		 * </p>
		 * 
		 * @since 1.0.0
		 */
		public static final int INQUIRY = 0x02;

		/**
		 * <p>
		 * control
		 * </p>
		 * 
		 * @since 1.0.0
		 */
		public static final int CONTROL = 0x03;

		/**
		 * <p>
		 * feedback
		 * </p>
		 * 
		 * @since 1.0.0
		 */
		public static final int FEEDBACK_CONTROL = 0x04;

	}

}
