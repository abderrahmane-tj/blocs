package com.jetris.core;

import java.awt.Color;

public class Shape {
    public Shape(int row, int column, String type, Color color) {
        this.row = row;
        this.column = column;
        this.type = type;
        grid = null;
        if (color == null && !type.equals("Blob")) {
            color = ColorConstants.DEFAULT_SQUARE_BACKGROUND;
        }
        int[][] coordinates = null;
        if (type.equals("L")) {
            grid = new Square[2][3];
            coordinates = new int[][]{
                    {0, 0},
                    {1, 0}, {1, 1}, {1, 2},
            };
        } else if (type.equals("LMirrored")) {
            grid = new Square[2][3];
            coordinates = new int[][]{
                    {0, 0}, {0, 1}, {0, 2},
                    {1, 0},
            };
        } else if (type.equals("Square")) {
            grid = new Square[2][2];
            coordinates = new int[][]{
                    {0, 0}, {0, 1},
                    {1, 0}, {1, 1},
            };
        } else if (type.equals("Z")) {
            grid = new Square[2][3];
            coordinates = new int[][]{
                    {0, 0}, {0, 1},
                    {1, 1}, {1, 2},
            };
        } else if (type.equals("ZMirrored")) {
            grid = new Square[2][3];
            coordinates = new int[][]{
                    {0, 1}, {0, 2},
                    {1, 0}, {1, 1},
            };
        } else if (type.equals("T")) {
            grid = new Square[2][3];
            coordinates = new int[][]{
                    {0, 0}, {0, 1}, {0, 2},
                    {1, 1},
            };
        } else if (type.equals("I")) {
            grid = new Square[1][4];
            coordinates = new int[][]{
                    {0, 0}, {0, 1}, {0, 2}, {0, 3},
            };
        }
        if (coordinates != null) {
            fillGrid(coordinates, color);
        }
    }

    public void fillGrid(int[][] coordinates, Color color) {
        for (int[] c : coordinates) {
            grid[c[0]][c[1]] = new Square(color);
        }
    }

    public boolean intersects(Shape shape) {
        if (shape == null) {
            return false;
        }

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] != null && shape.containsAndFull(row + i, column + j)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean containsAndFull(int row, int column) {
        int width = grid[0].length;
        int height = grid.length;
        int top = this.row;
        int left = this.column;
        int right = left + width - 1;
        int bottom = top + height - 1;

        if (row >= top && row <= bottom && column >= left && column <= right) {
            int localRow = row - top;
            int localColumn = column - left;

            if (grid[localRow][localColumn] != null) {
                return true;
            }
        }
        return false;
    }

    public Shape mergeWith(Shape shape) {
        if (shape == null) {
            shape = new Shape(row, column, "Blob", null);

            Square[][] grid = new Square[this.grid.length][this.grid[0].length];

            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    if (this.grid[i][j] != null) {
                        grid[i][j] = this.grid[i][j].clone();
                    }
                }
            }

            shape.setGrid(grid);

            return shape;
        } else {
            /*
             * since a lot of calculations has to be done i divided this section in two.
             *
             * first is metrics
             *  */
            int blobWidth = shape.grid[0].length;
            int blobHeight = shape.grid.length;
            int width = this.grid[0].length;
            int height = this.grid.length;

            int newTop = this.row - shape.row <= 0 ? this.row : shape.row;
            int newLeft = this.column - shape.column <= 0 ? this.column : shape.column;
            int newRight = (this.column + width) - (shape.column + blobWidth) >= 0
                    ? this.column + width - 1 : shape.column + blobWidth - 1;
            int diffHeight = newTop - shape.row >= 0 ? newTop - shape.row : shape.row - newTop;
            int newHeight = blobHeight + diffHeight;
            int newWidth = newRight - newLeft + 1;

            /*
             * second is filling out the blob with the squares from the old blob and the character
             * */
            Square[][] grid = new Square[newHeight][newWidth];

            int rowOffest = newTop < this.row ? this.row - newTop : 0;
            int columnOffest = newLeft < this.column ? this.column - newLeft : 0;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (this.grid[i][j] != null) {
                        grid[i + rowOffest][j + columnOffest] = this.grid[i][j].clone();
                    }
                }
            }

            rowOffest = newTop < shape.row ? shape.row - newTop : 0;
            columnOffest = newLeft < shape.column ? shape.column - newLeft : 0;
            for (int i = 0; i < blobHeight; i++) {
                for (int j = 0; j < blobWidth; j++) {
                    if (shape.grid[i][j] != null) {
                        grid[i + rowOffest][j + columnOffest] = shape.grid[i][j].clone();
                    }
                }
            }

            shape.setGrid(grid);
            shape.setRow(newTop);
            shape.setColumn(newLeft);
        }

        return shape;
    }

    public void turn(String direction) {
        int width = this.grid[0].length;
        int height = this.grid.length;
        Square[][] grid = null;

        if (direction.equals("clockwise")) {
            grid = new Square[width][height];

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    /*
                     * [0][0] --> [0][height-0]
                     * [0][1] --> [1][height-0]
                     * [0][2] --> [2][height-0]
                     * [1][0] --> [0][height-1]
                     * [1][1] --> [1][height-1]
                     * [1][2] --> [2][height-1]
                     * */
                    if (this.grid[i][j] != null) {
                        grid[j][height - i - 1] = this.grid[i][j].clone();
                    }
                }
            }
        } else {
            grid = new Square[width][height];

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    /*
                     * [0][0] --> [width-0][0]
                     * [0][1] --> [width-1][0]
                     * [0][2] --> [width-2][0]
                     * [0][3] --> [width-3][0]
                     * [0][4] --> [width-4][0]
                     * [1][0] --> [width-0][1]
                     * */
                    if (this.grid[i][j] != null) {
                        grid[width - j - 1][i] = this.grid[i][j].clone();
                    }
                }
            }
        }

        this.grid = grid;
    }

    private Square[][] grid;
    public int row;
    public int column;
    private String type;

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Square[][] getGrid() {
        return grid;
    }

    public void setGrid(Square[][] grid) {
        this.grid = grid;
    }
}

