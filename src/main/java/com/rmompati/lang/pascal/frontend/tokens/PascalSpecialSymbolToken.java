package com.rmompati.lang.pascal.frontend.tokens;

import com.rmompati.lang.frontend.Source;
import com.rmompati.lang.pascal.frontend.PascalToken;

import static com.rmompati.lang.pascal.frontend.PascalTokenType.ERROR;
import static com.rmompati.lang.pascal.frontend.PascalTokenType.SPECIAL_SYMBOLS;
import static com.rmompati.lang.pascal.frontend.error.PascalErrorCode.INVALID_CHARACTER;

public class PascalSpecialSymbolToken extends PascalToken {
    /**
     * Constructor.
     *
     * @param source the source from where to fetch the token's characters.
     * @throws Exception if an error occurred.
     */
    public PascalSpecialSymbolToken(Source source) throws Exception {
        super(source);
    }

    /**
     * Extracts a Pascal special symbol token from the source.
     *
     * @throws Exception if an error occurs.
     */
    @Override
    protected void extract() throws Exception {
        char currentChar = currentChar();

        text = Character.toString(currentChar);
        type = null;

        switch (currentChar) {
            // Single-Character symbols.
            case '+':
            case '-':
            case '*':
            case '/':
            case ',':
            case ';':
            case '\\':
            case '=':
            case '(':
            case ')':
            case '[':
            case ']':
            case '{':
            case '}':
            case '^': {
                nextChar();
                break;
            }
            case ':': // : or =
            case '>': { // > or >=
                currentChar = nextChar();

                if (currentChar == '=') {
                    text += currentChar;
                    nextChar();
                }
                break;
            }
            case '<': { // < or <= or <>
                currentChar = nextChar();

                if (currentChar == '=') {
                    text += currentChar;
                    nextChar();
                } else if (currentChar == '>') {
                    text += currentChar;
                    nextChar();
                }
                break;
            }
            case '.': { // . or ..
                currentChar = nextChar();
                if (currentChar == '.') {
                    text += currentChar;
                    nextChar();
                }
                break;
            }
            default: {
                nextChar();
                type = ERROR;
                value = INVALID_CHARACTER;
            }
        }
        // Set the type if it wasn't an error
        if (type == null) {
            type = SPECIAL_SYMBOLS.get(text);
        }
    }
}
