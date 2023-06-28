package com.rmompati.lang.pascal.frontend.tokens;

import com.rmompati.lang.frontend.Source;
import com.rmompati.lang.pascal.frontend.PascalToken;

import static com.rmompati.lang.pascal.frontend.PascalTokenType.*;
import static com.rmompati.lang.pascal.frontend.error.PascalErrorCode.*;
import static java.lang.Double.MAX_EXPONENT;

public class PascalNumberToken extends PascalToken {
    /**
     * Constructor.
     *
     * @param source the source from where to fetch the token's characters.
     * @throws Exception if an error occurred.
     */
    public PascalNumberToken(Source source) throws Exception {
        super(source);
    }

    /**
     * Extracts a Pascal number token from the source.
     *
     * @throws Exception if an error occurs.
     */
    @Override
    protected void extract() throws Exception {
        StringBuilder textBuffer = new StringBuilder();
        extractNumber(textBuffer);
        text = textBuffer.toString();
    }

    /**
     * Extracts a Pascal number token from source.
     * @param textBuffer the buffer to append the token's characters
     * @throws Exception if an error occurs.
     */
    protected void extractNumber(StringBuilder textBuffer) throws Exception {
        String wholeDigits = null;
        String fractionalDigits = null;
        String exponentsDigits = null;
        char exponentSign = '+';
        boolean sawDotDot = false;
        char currentChar;

        type = INTEGER;

        // Extract the digits of the whole part of the number.
        wholeDigits = unsignedIntegerDigits(textBuffer);
        if (type == ERROR) {
            return;
        }

        currentChar = currentChar();
        if (currentChar == '.') {
            if (peekChar() == '.') {
                sawDotDot = true;
            } else {
                type = REAL;
                textBuffer.append(currentChar);
                currentChar = nextChar();

                // Collect the digits of the fraction part of the number.
                fractionalDigits = unsignedIntegerDigits(textBuffer);
                if (type == ERROR) {
                    return;
                }
            }
        }

        currentChar = currentChar();
        if (!sawDotDot && ((currentChar == 'E') || currentChar == 'e')) {
            type = REAL;
            textBuffer.append(currentChar);
            currentChar = nextChar();

            if ((currentChar == '+') || (currentChar == '-')) {
                textBuffer.append(currentChar);
                exponentSign = currentChar;
                currentChar = nextChar();
            }

            // Extract exponents digits.
            exponentsDigits = unsignedIntegerDigits(textBuffer);
        }
        
        if (type == INTEGER) {
            int integerValue = computeIntegerValue(wholeDigits);
            if (type != ERROR) {
                value = integerValue;
            }
        } else if (type == REAL) {
            float floatValue = computeFloatValue(wholeDigits, fractionalDigits, exponentsDigits, exponentSign);
            if (type != ERROR) {
                value = floatValue;
            }
        }
    }

    /**
     * Compute and return the float value of a real number.
     * @param wholeDigits a string of digits before the decimal point.
     * @param fractionalDigits a string of digits after the decimal point.
     * @param exponentsDigits a string of exponents digits.
     * @param exponentSign the exponent sign.
     * @return the float value;
     */
    private float computeFloatValue(String wholeDigits, String fractionalDigits, String exponentsDigits, char exponentSign) {
        double floatValue = 0.0;
        int exponentValue = computeIntegerValue(exponentsDigits);
        String digits = wholeDigits;
        if (exponentSign == '-') {
            exponentValue = -exponentValue;
        }

        // If there are any fractional digits, adjust the exponent value and append the fractional digits.
        if (fractionalDigits != null) {
            exponentValue -= fractionalDigits.length();
            digits += fractionalDigits;
        }

        // Check for a real number out of range error.
        if (Math.abs(exponentValue + wholeDigits.length()) > MAX_EXPONENT) {
            type = ERROR;
            value = RANGE_REAL;
            return 0.0f;
        }

        // Loop over the digits to compute the float value.
        int index = 0;
        while (index < digits.length()) {
            floatValue = 10 * floatValue + Character.getNumericValue(digits.charAt(index++));
        }

        // Adjust float value based on the exponent value.
        if (exponentValue != 0) {
            floatValue *= Math.pow(10, exponentValue);
        }

        return (float) floatValue;
    }

    /**
     * Compute and return the integer value of a string of digits.
     * Checks for overflow.
     * @param digits the string of digits.
     * @return the integer value.
     */
    private int computeIntegerValue(String digits) {
        if (digits == null) {
            return 0;
        }

        int integerValue = 0;
        int prevValue = -1;
        int index = 0;

        while ((index < digits.length()) && (integerValue >= prevValue)) {
            prevValue = integerValue;
            integerValue = 10 * integerValue + Character.getNumericValue(digits.charAt(index++));
        }

        // No overflow
        if (integerValue >= prevValue) {
            return integerValue;
        }

        // Overflow: Set the integer out of range error.
        type = ERROR;
        value = RANGE_INTEGER;
        return 0;
    }

    /**
     * Extracts and returns the digits of an unsigned integer,
     * @param textBuffer the buffer to append the token's characters.
     * @return the string of digits.
     * @throws Exception if an error occurs.
     */
    private String unsignedIntegerDigits(StringBuilder textBuffer) throws Exception {
        char currentChar = currentChar();

        if (!Character.isDigit(currentChar)) {
            type = ERROR;
            value = INVALID_NUMBER;
            return null;
        }

        // Extract digits
        StringBuilder digits = new StringBuilder();
        while (Character.isDigit(currentChar)) {
            textBuffer.append(currentChar);
            digits.append(currentChar);
            currentChar = nextChar();
        }
        return digits.toString();
    }
}
