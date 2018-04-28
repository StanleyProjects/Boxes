package stan.boxes.json;

class Yytoken
{
    static final int TYPE_VALUE = 0;
    static final int TYPE_LEFT_BRACE = 1;
    static final int TYPE_RIGHT_BRACE = 2;
    static final int TYPE_LEFT_SQUARE = 3;
    static final int TYPE_RIGHT_SQUARE = 4;
    static final int TYPE_COMMA = 5;
    static final int TYPE_COLON = 6;
    static final int TYPE_EOF = -1;

    final int type;
    final Object value;

    Yytoken(int type, Object value)
    {
        this.type = type;
        this.value = value;
    }
}