package com.emmanuel.chesser;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Coordinate {
    public final char column;
    public final int row;

    public Coordinate(char column, int row) {
        this.column = column;
        this.row = row;
    }

    /**
     * @param coordinate e.g a1
     */
    public Coordinate(String coordinate) {
        Pattern p = Pattern.compile("([a-h])([1-8])");
        Matcher m = p.matcher(coordinate);

        if (m.matches()) {
            this.column = m.group(1).charAt(0);
            this.row = Integer.parseInt(m.group(2));
        } else {
            this.column = 'a';
            this.row = 1;
        }
    }

    @Override
    public String toString() {
        return String.format("%c%d", column, row);
    }
}
