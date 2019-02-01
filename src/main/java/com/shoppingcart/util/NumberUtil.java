package com.shoppingcart.util;

import java.text.DecimalFormat;
import java.util.stream.IntStream;

public class NumberUtil {

	private NumberUtil() {

	}

	/**
	 * @param decimalSeperator
	 * @param noOfFractDigits
	 * @return
	 */
	public static final String getNumberDecimalPattern(final String decimalSeperator, final int noOfFractDigits) {

		final StringBuilder pattern = new StringBuilder(3 + noOfFractDigits);
		pattern.append("#0").append(decimalSeperator);

		IntStream.range(1, noOfFractDigits + 1).forEach(index -> pattern.append("0"));

		return pattern.toString();
	}

	/**
	 * @param pattern
	 * @return
	 */
	public static final DecimalFormat getNumberDecimalFormat(final String pattern) {
		return new DecimalFormat(pattern);
	}

	/**
	 * @param decimalSeperator
	 * @param noOfFractDigits
	 * @return
	 */
	public static final DecimalFormat getNumberDecimalFormat(final String decimalSeperator, final int noOfFractDigits) {
		final String pattern = getNumberDecimalPattern(decimalSeperator, noOfFractDigits);
		return getNumberDecimalFormat(pattern);
	}

	/**
	 * formats the input :: <code>number</code> to pattern :: <code>pattern</code>
	 * 
	 * @param number
	 * @param pattern
	 * @return String
	 */
	public static final String formatNumber(final String number, final String pattern, boolean returnAbsolute) {

		String retStr = null;
		try {
			final DecimalFormat decimalFormat = getNumberDecimalFormat(pattern);
			Double value = Double.valueOf(number);
			if (returnAbsolute) {
				value = Math.abs(value);
			}
			retStr = decimalFormat.format(value);
		} catch (Exception e) {
			retStr = number;
			System.err.println("NumberFormatException inside formatNumber while formatting number ::>> " + number
					+ " ::>> using pattern ::>> " + pattern);
			System.err.println(e.getMessage());
		}
		return retStr;
	}

	/**
	 * @param number
	 * @param decimalSeperator
	 * @param noOfFractDigits
	 * @return
	 */
	public static final String formatNumber(final String number, final String decimalSeperator,
			final int noOfFractDigits) {
		final String pattern = getNumberDecimalPattern(decimalSeperator, noOfFractDigits);
		return formatNumber(number, pattern, false);
	}

	/**
	 * @param number
	 * @param decimalSeperator
	 * @param noOfFractDigits
	 * @param returnAbsolute
	 * @return
	 */
	public static final String formatNumber(final String number, final String decimalSeperator,
			final int noOfFractDigits, boolean returnAbsolute) {
		final String pattern = getNumberDecimalPattern(decimalSeperator, noOfFractDigits);
		return formatNumber(number, pattern, returnAbsolute);
	}

	/**
	 * formats the input :: <code>number</code> to pattern :: <code>pattern</code>
	 * 
	 * @param number
	 * @param pattern
	 * @return String
	 */
	public static final String formatNumber(final String number, final String pattern) {
		return formatNumber(number, pattern, false);
	}

	/**
	 * @param number
	 * @param pattern
	 * @return
	 */
	public static final String formatNumber(final double number, final String pattern) {
		final DecimalFormat decimalFormat = getNumberDecimalFormat(pattern);
		return decimalFormat.format(number);
	}

	/**
	 * @param number
	 * @param pattern
	 * @param returnAbsolute
	 * @return
	 */
	public static final String formatNumber(final double number, final String pattern, boolean returnAbsolute) {
		double value = returnAbsolute ? Math.abs(number) : number ;
		return formatNumber(value, pattern);
	}

	/**
	 * @param number
	 * @param decimalSeperator
	 * @param noOfFractDigits
	 * @return
	 */
	public static final String formatNumber(final double number, final String decimalSeperator,
			final int noOfFractDigits) {
		String pattern = getNumberDecimalPattern(decimalSeperator, noOfFractDigits);
		return formatNumber(number, pattern);
	}

	/**
	 * @param number
	 * @param decimalSeperator
	 * @param noOfFractDigits
	 * @param returnAbsolute
	 * @return
	 */
	public static final String formatNumber(final double number, final String decimalSeperator,
			final int noOfFractDigits, boolean returnAbsolute) {
		double value = number;
		if (returnAbsolute) {
			value = Math.abs(value);
		}
		return formatNumber(value, decimalSeperator, noOfFractDigits);
	}

}
