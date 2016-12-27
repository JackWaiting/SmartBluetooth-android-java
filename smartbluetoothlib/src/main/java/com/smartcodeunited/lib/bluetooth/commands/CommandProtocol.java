package com.smartcodeunited.lib.bluetooth.commands;

public class CommandProtocol {

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
