package com._604robotics.robotnik.prefabs.devices;

public interface AbsoluteEncoder {

	/**
	 * Sets the zero of the encoder to its current value.
	 */
	void setZero();

	/**
	 * Sets the zero of an encoder.
	 * @param zero Zero value to set.
	 */
	void setZero(double zero);

	/**
	 * Sets the zero of the encoder to an angle.
	 * @param zeroAngle Zero angle to set.
	 */
	void setZeroAngle(double zeroAngle);

	/**
	 * Gets the (raw, non-zeroed) voltage value of the encoder.
	 * @return The (raw, non-zeroed) voltage value of the encoder.
	 */
	double getRawVoltage();

	/**
	 * Gets the (zeroed) voltage value of the encoder.
	 * @return The (zeroed) voltage value of the encoder.
	 */
	double getVoltage();

	/**
	 * Gets the (zeroed) angle of the encoder.
	 * @return The (zeroed) angle of the encoder.
	 */
	double getAngle();

	/**
	 * Gets the (raw, non-zeroed) angle value of the encoder.
	 * @return The (raw, non-zeroed) angle of the encoder.
	 */
	double getRawAngle();

}